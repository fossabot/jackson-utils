/*
 * Copyright © 2016 Oleg Cherednik
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
final class JacksonObjectMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        DefaultSettingsConsumer.INSTANCE.accept(OBJECT_MAPPER);
    }

    public static ObjectMapper mapper() {
        return OBJECT_MAPPER;
    }

    private JacksonObjectMapper() {
    }

}
