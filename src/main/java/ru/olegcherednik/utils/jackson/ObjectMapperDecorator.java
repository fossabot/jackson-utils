package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
public class ObjectMapperDecorator {

    protected final Supplier<ObjectMapper> supplier;

    public ObjectMapperDecorator(ObjectMapper mapper) {
        this(() -> mapper);
    }

    public ObjectMapperDecorator(Supplier<ObjectMapper> supplier) {
        this.supplier = supplier;
    }

    public <T> T readValue(String json, Class<T> valueType) {
        Objects.requireNonNull(valueType, "'valueType' should not be null");

        if (json == null)
            return null;

        return withRuntimeException(() -> supplier.get().readValue(json, valueType));
    }

    public <T> List<T> readList(String json, Class<T> valueType) {
        Objects.requireNonNull(valueType, "'valueType' should not be null");

        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyList();

        return withRuntimeException(() -> {
            ObjectReader reader = supplier.get().readerFor(valueType);
            return reader.<T>readValues(json).readAll();
        });
    }

    public Map<String, ?> readMap(String json) {
        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = supplier.get();
            MapType mapType = mapper.getTypeFactory().constructRawMapType(LinkedHashMap.class);
            return mapper.readValue(json, mapType);
        });
    }

    public <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = supplier.get();
            MapType mapType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, keyClass, valueClass);
            return mapper.readValue(json, mapType);
        });
    }

    public <T> String writeValue(T obj) {
        if (obj == null)
            return null;

        return withRuntimeException(() -> supplier.get().writeValueAsString(obj));
    }

    public <T> void writeValue(T obj, OutputStream out) {
        Objects.requireNonNull(out, "'out' should not be null");

        withRuntimeException(() -> {
            supplier.get().writeValue(out, obj);
            return null;
        });
    }

    private static <T> T withRuntimeException(Callable<T> task) {
        try {
            return task.call();
        } catch(Exception e) {
            throw new JacksonUtilsException(e);
        }
    }

    private static boolean isEmpty(String json) {
        json = json.trim();
        return "{}".equals(json) || "[]".equals(json);
    }

}
