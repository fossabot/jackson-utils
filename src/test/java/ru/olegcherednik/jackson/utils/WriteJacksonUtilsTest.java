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
package ru.olegcherednik.jackson.utils;

import org.testng.annotations.Test;
import ru.olegcherednik.jackson.utils.data.Data;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
public class WriteJacksonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(JacksonUtils.writeValue(null)).isNull();
//        assertThat(JacksonUtils.writeValue(null, new BufferedOutputStream())).isNull();
    }

    public void shouldRetrieveJsonWhenWriteObject() {
        Data data = new Data(555, "victory");
        String actual = JacksonUtils.writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"intVal\":555,\"strVal\":\"victory\"}");
    }

    public void shouldRetrieveJsonWhenWriteMapObject() {
        Map<String, Data> map = MapUtils.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        String actual = JacksonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"},\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}");
    }

    public void shouldRetrieveJsonWhenWriteListObject() {
        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
        String actual = JacksonUtils.writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
    }

    public void shouldRetrieveEmptyJsonWhenWriteEmptyCollection() {
        assertThat(JacksonUtils.writeValue(Collections.emptyList())).isEqualTo("[]");
        assertThat(JacksonUtils.writeValue(Collections.emptyMap())).isEqualTo("{}");
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            Data data = new Data(666, "omen");
            JacksonUtils.writeValue(data, out);
            assertThat(out.toString()).isEqualTo("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

    public void shouldWriteNullJsonWhenWriteNullToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            JacksonUtils.writeValue(null, out);
            assertThat(out.toString()).isEqualTo("null");
        }
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteObjectWithPrettyPrint() {
        Data data = new Data(555, "victory");
        String actual = JacksonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"intVal\" : 555," + System.lineSeparator() +
                "  \"strVal\" : \"victory\"" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteMapObjectWithPrettyPrint() {
        Map<String, Data> data = MapUtils.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        String actual = JacksonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"victory\" : {" + System.lineSeparator() +
                "    \"intVal\" : 555," + System.lineSeparator() +
                "    \"strVal\" : \"victory\"" + System.lineSeparator() +
                "  }," + System.lineSeparator() +
                "  \"omen\" : {" + System.lineSeparator() +
                "    \"intVal\" : 666," + System.lineSeparator() +
                "    \"strVal\" : \"omen\"" + System.lineSeparator() +
                "  }" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteListObjectWithPrettyPrint() {
        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
        String actual = JacksonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("[ {" + System.lineSeparator() +
                "  \"intVal\" : 555," + System.lineSeparator() +
                "  \"strVal\" : \"victory\"" + System.lineSeparator() +
                "}, {" + System.lineSeparator() +
                "  \"intVal\" : 666," + System.lineSeparator() +
                "  \"strVal\" : \"omen\"" + System.lineSeparator() +
                "} ]");
    }

    public void shouldWritePrettyPrintJsonToStreamWhenWriteObjectWithPrettyPrintToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            Data data = new Data(666, "omen");
            JacksonUtils.prettyPrint().writeValue(data, out);
            assertThat(out.toString()).isEqualTo('{' + System.lineSeparator() +
                    "  \"intVal\" : 666," + System.lineSeparator() +
                    "  \"strVal\" : \"omen\"" + System.lineSeparator() +
                    '}');
        }
    }

    public void shouldWriteNullJsonWhenWriteNullWithPrettyPrintToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            JacksonUtils.prettyPrint().writeValue(null, out);
            assertThat(out.toString()).isEqualTo("null");
        }
    }

}
