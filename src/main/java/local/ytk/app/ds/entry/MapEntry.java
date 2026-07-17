package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;
import local.ytk.app.ds.val.c.MapValue;

public interface MapEntry<M extends MapValue<M, String, V, MapEntry<M, V>>, V extends DataValue<?>> extends EntryItem<M, String, V> {
}
