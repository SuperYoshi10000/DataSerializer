package local.ytk.app.ds.data.tag;

import java.util.LinkedHashMap;

public abstract class MapTag<T extends Tag, M extends MapTag<T, M>> extends LinkedHashMap<String, T> implements DictionaryTag<String, T, M>/*, MapValue<M, String, T, MapEntry<M, T>>*/ {
    
    @Override
    @SuppressWarnings("unchecked")
    public MapTag<T, M> clone() {
        return (MapTag<T, M>) super.clone();
    }
}
