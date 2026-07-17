package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;
import local.ytk.app.ds.val.c.ContainerValue;

public interface ListItem<C extends ContainerValue<C, Integer, V, ? extends EntryItem<C, Integer, V>>, V extends DataValue<?>> extends ListEntry<V>, EntryItem<C, Integer, V> {
}
