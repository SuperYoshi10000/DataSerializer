package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;
import local.ytk.app.ds.val.c.ContainerValue;
import local.ytk.app.ds.val.c.MapValue;

public interface MapItem<M extends MapValue<M, String, V, MapEntry<M, V>>, V extends DataValue<?>> extends MapEntry<M, V>, EntryItem<M, String, V> {
}
