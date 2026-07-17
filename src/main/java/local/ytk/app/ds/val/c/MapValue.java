package local.ytk.app.ds.val.c;

import local.ytk.app.ds.entry.EntryItem;
import local.ytk.app.ds.val.DataValue;

import java.util.Collection;
import java.util.Map;

public interface MapValue<C extends MapValue<C, K, V, E>, K, V extends DataValue<?>, E extends EntryItem<C, K, V>> extends ContainerValue<C, K, V, E> {
    Map<K, V> asMap();
}
