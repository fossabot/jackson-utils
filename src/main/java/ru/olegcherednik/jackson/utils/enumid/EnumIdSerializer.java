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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.olegcherednik.jackson.utils.EnumId;

import java.io.IOException;

/**
 * Custom serializer for all {@link EnumId}. It just invoke {@link EnumId#getId()}. Since that, standard {@link
 * com.fasterxml.jackson.databind.deser.std.EnumDeserializer} will not be working for these types, because by default {@link EnumId#getId()} returns
 * lower-case enum constant name (instead of upper-case by default). Therefore you have to define {@link com.fasterxml.jackson.annotation.JsonCreator}
 * for every {@link EnumId} instance.
 */
final class EnumIdSerializer extends JsonSerializer<EnumId> {

    public static final EnumIdSerializer INSTANCE = new EnumIdSerializer();

    private EnumIdSerializer() {
    }

    @Override
    public void serialize(EnumId enumId, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeString(enumId.getId());
    }

}
