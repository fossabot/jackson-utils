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

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;
import ru.olegcherednik.jackson.utils.data.Book;
import ru.olegcherednik.jackson.utils.data.Data;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 03.01.2021
 */
@Test
public class InputStreamJacksonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(JacksonUtils.readValue((InputStream)null, Object.class)).isNull();
        assertThat(JacksonUtils.readList((InputStream)null)).isNull();
        assertThat(JacksonUtils.readList((InputStream)null, Object.class)).isNull();
        assertThat(JacksonUtils.readListLazy((InputStream)null)).isNull();
        assertThat(JacksonUtils.readListLazy((InputStream)null, Object.class)).isNull();
        assertThat(JacksonUtils.readListOfMap((InputStream)null)).isNull();
        assertThat(JacksonUtils.readListOfMapLazy((InputStream)null)).isNull();
        assertThat(JacksonUtils.readMap((InputStream)null)).isNull();
        assertThat(JacksonUtils.readMap((InputStream)null, String.class)).isNull();
        assertThat(JacksonUtils.readMap((InputStream)null, String.class, String.class)).isNull();
    }

    public void shouldRetrieveDeserializedObjectWhenReadValue() throws IOException {
        InputStream in = getResourceAsInputStream("/data.json");
        Data actual = JacksonUtils.readValue(in, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyValue() {
        InputStream in = convertToInputStream("{}");
        Data actual = JacksonUtils.readValue(in, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data());
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumeric() {
        InputStream in = convertToInputStream("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]");
        List<Object> actual = JacksonUtils.readList(in);

        assertThat(actual).hasSize(5);
        assertThat(actual.get(0)).isEqualTo(1);
        assertThat(actual.get(1)).isEqualTo(2.0);
        assertThat(actual.get(2)).isEqualTo(3.1);
        assertThat(actual.get(3)).isEqualTo(12345678912L);
        assertThat(actual.get(4)).isEqualTo(new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveDeserializedListWhenReadAsList() throws IOException {
        InputStream in = getResourceAsInputStream("/data_list.json");
        List<Data> actual = JacksonUtils.readList(in, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadAsLazyList() throws IOException {
        Map<String, Object> expected1 = MapUtils.of(
                "title", "Thinking in Java",
                "date", "2017-07-23T13:57:14.225Z",
                "year", 1998,
                "authors", ListUtils.of("Bruce Eckel"));
        Map<String, Object> expected2 = MapUtils.of(
                "title", "Ready for a victory",
                "date", "2020-07-23T13:57:14.225Z",
                "year", 2020,
                "authors", ListUtils.of("Oleg Cherednik"));

        InputStream in = getResourceAsInputStream("/books.json");
        Iterator<Object> it = JacksonUtils.readListLazy(in);
        assertThat(it.hasNext()).isTrue();

        Object actual1 = it.next();
        assertThat(actual1).isNotNull();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(it.hasNext()).isTrue();

        Object actual2 = it.next();
        assertThat(actual2).isNotNull();
        assertThat(actual2).isEqualTo(expected2);
        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadValueLazyList() throws IOException {
        Book expected1 = new Book(
                "Thinking in Java",
                ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                1998,
                ListUtils.of("Bruce Eckel"));
        Book expected2 = new Book(
                "Ready for a victory",
                ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                2020,
                ListUtils.of("Oleg Cherednik"));

        InputStream in = getResourceAsInputStream("/books.json");
        Iterator<Book> it = JacksonUtils.readListLazy(in, Book.class);
        assertThat(it.hasNext()).isTrue();

        Book actual1 = it.next();
        assertThat(actual1).isNotNull();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(it.hasNext()).isTrue();

        Book actual2 = it.next();
        assertThat(actual2).isNotNull();
        assertThat(actual2).isEqualTo(expected2);
        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveListOfMapWhenRead() throws IOException {
        InputStream in = getResourceAsInputStream("/data_list.json");
        List<Map<String, Object>> actual = JacksonUtils.readListOfMap(in);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).hasSize(2);
        assertThat(actual.get(0)).containsEntry("intVal", 555);
        assertThat(actual.get(0)).containsEntry("strVal", "victory");
        assertThat(actual.get(1)).hasSize(2);
        assertThat(actual.get(1)).containsEntry("intVal", 666);
        assertThat(actual.get(1)).containsEntry("strVal", "omen");
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadByteBufferAsListOfMapLazy() throws IOException {
        Map<String, Object> expected1 = MapUtils.of(
                "title", "Thinking in Java",
                "date", "2017-07-23T13:57:14.225Z",
                "year", 1998,
                "authors", ListUtils.of("Bruce Eckel"));
        Map<String, Object> expected2 = MapUtils.of(
                "title", "Ready for a victory",
                "date", "2020-07-23T13:57:14.225Z",
                "year", 2020,
                "authors", ListUtils.of("Oleg Cherednik"));

        InputStream in = getResourceAsInputStream("/books.json");
        Iterator<Map<String, Object>> it = JacksonUtils.readListOfMapLazy(in);
        assertThat(it.hasNext()).isTrue();

        Object actual1 = it.next();
        assertThat(actual1).isNotNull();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(it.hasNext()).isTrue();

        Object actual2 = it.next();
        assertThat(actual2).isNotNull();
        assertThat(actual2).isEqualTo(expected2);
        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveDataMapWhenReadAsMapWithStringKey() throws IOException {
        InputStream in = getResourceAsInputStream("/variable_value_map.json");
        Map<String, Object> actual = JacksonUtils.readMap(in);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual).containsEntry("sample", ListUtils.of("one, two", "three"));
        assertThat(actual).containsEntry("order", MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadAsMapWithStringKeyAndType() throws IOException {
        InputStream in = getResourceAsInputStream("/string_value_map_s.json");
        Map<String, String> actual = JacksonUtils.readMap(in, String.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(MapUtils.of("auto", "Audi", "model", "RS3"));
    }

    public void shouldRetrieveDeserializedMapWhenReadAsMapListWithStringKeyAndBookType() throws IOException {
        Map<String, Book> expected = MapUtils.of(
                "one", new Book(
                        "Thinking in Java",
                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                        1998,
                        ListUtils.of("Bruce Eckel")),
                "two", new Book(
                        "Ready for a victory",
                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                        2020,
                        ListUtils.of("Oleg Cherednik"))
        );

        InputStream in = getResourceAsInputStream("/books_dict_string_key.json");
        Map<String, Book> actual = JacksonUtils.readMap(in, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveIntegerValueMapWhenReadAsMapWithIntKeyAndBookType() throws IOException {
        Map<Integer, Book> expected = MapUtils.of(
                1, new Book(
                        "Thinking in Java",
                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                        1998,
                        ListUtils.of("Bruce Eckel")),
                2, new Book(
                        "Ready for a victory",
                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                        2020,
                        ListUtils.of("Oleg Cherednik"))
        );

        InputStream in = getResourceAsInputStream("/books_dict_int_key.json");
        Map<Integer, Book> actual = JacksonUtils.readMap(in, Integer.class, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyListWhenReadEmptyByteBufferAsList() {
        assertThat(JacksonUtils.readList(convertToInputStream("[]"))).isEmpty();
        assertThat(JacksonUtils.readList(convertToInputStream("[]"), Data.class)).isEmpty();
        assertThat(JacksonUtils.readListOfMap(convertToInputStream("[]"))).isEmpty();
        assertThat(JacksonUtils.readMap(convertToInputStream("{}"))).isEmpty();
        assertThat(JacksonUtils.readMap(convertToInputStream("{}"), Data.class)).isEmpty();
        assertThat(JacksonUtils.readMap(convertToInputStream("{}"), String.class, Data.class)).isEmpty();
    }

    public void shouldThrowJacksonUtilsExceptionWhenReadIncorrectByteBuffer() {
        InputStream in = convertToInputStream("incorrect");

        assertThatThrownBy(() -> JacksonUtils.readValue(in, Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap(in))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap(in, Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap(in, String.class, Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
    }

    private static InputStream getResourceAsInputStream(String name) throws IOException {
        return InputStreamJacksonUtilsTest.class.getResourceAsStream(name);
    }

    private static InputStream convertToInputStream(String str) {
        return IOUtils.toInputStream(str, StandardCharsets.UTF_8);
    }

}


