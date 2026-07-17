package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;
import local.ytk.app.ds.val.c.MapValue;

public record MapItemImpl<C extends MapValue<C, String, V, MapEntry<C, V>>, V extends DataValue<?>>(C container, String key, V value) implements MapItem<C, V> {
}
