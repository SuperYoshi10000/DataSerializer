package local.ytk.app.ds.transform;

import local.ytk.app.ds.entry.ListEntry;
import local.ytk.app.ds.entry.MapEntry;
import local.ytk.app.ds.val.DataValue;

import java.util.List;
import java.util.Map;

public interface DataOperator<T> {
    T get(T input, String key);
    default T getOrDefault(T input, String key, T defaultValue) {
        T value = get(input, key);
        return value != null ? value : defaultValue;
    }
    T get(T input, int index);
    default T getOrDefault(T input, int index, T defaultValue) {
        T value = get(input, index);
        return value != null ? value : defaultValue;
    }
    
    T set(T input, String key, T value);
    T set(T input, int index, T value);
    
    T insert(T input, int index, T value);
    T add(T input, T value);
    
    T with(T input, String key, T value);
    
    default T set(T input, MapEntry<?, DataValue<T>> entry) {
        return set(input, entry.key(), entry.value().get());
    }
    default T set(T input, ListEntry<DataValue<T>> entry) {
        return set(input, entry.key(), entry.value().get());
    }
    
    T merge(T first, T second);
    
    // second as return value = replace first with second
    default T mergeListOrMap(T first, T second) {
        if (isList(first) && isList(second)) return mergeList(first, second);
        if (isMap(first) && isMap(second)) return mergeMap(first, second);
        return second;
    }
    default T mergeMap(T first, T second) {
        if (isMap(first) && isMap(second)) return merge(first, second);
        return second;
    }
    default T mergeList(T first, T second) {
        if (isList(first) && isList(second)) return merge(first, second);
        return second;
    }
    
    T addAll(T first, T second);
    default T addAllListOrMap(T first, T second) {
        if (isList(first) && isList(second)) return addAllList(first, second);
        if (isMap(first) && isMap(second)) return addAllMap(first, second);
        return second;
    }
    default T addAllMap(T first, T second) {
        if (isMap(first) && isMap(second)) return addAll(first, second);
        return null;
    }
    default T addAllList(T first, T second) {
        if (isList(first) && isList(second)) return addAll(first, second);
        return null;
    }
    
    T copy(T input);
    
    Class<T> getOperatorClass();
    
    default boolean isList(T input) {
        return input instanceof List || input.getClass().isArray();
    }
    default boolean isMap(T input) {
        return input instanceof Map;
    }
    default <U extends T> U cast(T input) {
        return (U) input;
    }
    default <U extends T> U cast(T input, Class<U> cls) {
        return input != null && cls.isAssignableFrom(input.getClass()) ? cls.cast(input) : null;
    }
}
