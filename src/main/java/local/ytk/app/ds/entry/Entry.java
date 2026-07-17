package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;

public interface Entry<K, V extends DataValue> {
    K key();
    V value();
}
