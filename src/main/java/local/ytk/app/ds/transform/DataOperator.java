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
    default T mergeMap(T first, T second) {
        if (first instanceof Map && second instanceof Map) return merge(first, second);
        return null;
    }
    default T mergeList(T first, T second) {
        if (first instanceof List && second instanceof List) return merge(first, second);
        return null;
    }
    T addAll(T first, T second);
    default T addAllMap(T first, T second) {
        if (first instanceof Map && second instanceof Map) return addAll(first, second);
        return null;
    }
    default T addAllList(T first, T second) {
        if (first instanceof List && second instanceof List) return addAll(first, second);
        return null;
    }
    
    T copy(T input);
    
    Class<T> getOperatorClass();
}
