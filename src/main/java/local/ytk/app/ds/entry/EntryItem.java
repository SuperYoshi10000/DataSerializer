package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;
import local.ytk.app.ds.val.c.ContainerValue;

public interface EntryItem<C extends ContainerValue<C, K, V, ? extends EntryItem<C, K, V>>, K, V extends DataValue> extends Entry<K, V> {
    C container();
}
