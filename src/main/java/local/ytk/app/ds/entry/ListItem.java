package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;

public interface ListItem<C, V extends DataValue<?>> extends ListEntry<V>, EntryItem<C, Integer, V> {
}
