/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ru.olegcherednik.jackson.utils.enumid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import ru.olegcherednik.jackson.utils.JacksonUtilsException;
import ru.olegcherednik.utils.reflection.MethodUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Oleg Cherednik
 * @since 27.02.2022
 */
final class EnumIdDeserializers extends SimpleDeserializers {

    private static final long serialVersionUID = -6984119149643932744L;

    public static final EnumIdDeserializers INSTANCE = new EnumIdDeserializers();

    private EnumIdDeserializers() {
    }

    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc)
            throws JsonMappingException {
        Function<String, ?> read = createReadFunc(type);

        return new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser in, DeserializationContext ctxt) throws IOException {
                String id = in.getText();
                return read.apply(id);
            }

            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return read.apply(null);
            }
        };
    }

    private static <T> Function<String, T> createReadFunc(Class<T> rawType) {
        List<Method> methods = getJsonCreateMethods(rawType);

        if (methods.size() > 1) {
            return id -> {
                throw new JacksonUtilsException("Multiple methods with '" + JsonCreator.class.getSimpleName()
                        + "' annotation was found in '" + rawType.getSimpleName() + "' class");
            };
        }

        if (methods.size() == 1)
            return createFunc(methods.get(0));

        Method method = getParseIdMethod(rawType);

        if (method == null) {
            return id -> {
                throw new JacksonUtilsException("Factory method for EnumIs '"
                        + rawType.getSimpleName() + "' was not found");
            };
        }

        return createFunc(method);
    }

    private static <T> Function<String, T> createFunc(Method method) {
        return id -> {
            try {
                return MethodUtils.invokeStaticMethod(method, id);
            } catch (Exception e) {
                throw new JacksonUtilsException(e.getCause());
            }
        };
    }

    private static List<Method> getJsonCreateMethods(final Class<?> rawType) {
        List<Method> res = new ArrayList<>();
        Class<?> type = rawType;

        while (type != Object.class) {
            for (Method method : type.getDeclaredMethods())
                if (isValidFactoryMethod(method))
                    res.add(method);

            type = type.getSuperclass();
        }

        return res;
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    private static Method getParseIdMethod(Class<?> rawType) {
        while (rawType != Object.class) {
            try {
                return rawType.getDeclaredMethod("parseId", String.class);
            } catch (NoSuchMethodException ignore) {
                // ignore
            }

            rawType = rawType.getSuperclass();
        }

        return null;
    }

    private static boolean isValidFactoryMethod(Method method) {
        return Modifier.isStatic(method.getModifiers())
                && method.isAnnotationPresent(JsonCreator.class)
                && method.getParameterCount() == 1
                && method.getParameterTypes()[0] == String.class;
    }

}
