package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;
import local.ytk.app.ds.val.c.ContainerValue;

public record ListItemImpl<C extends ContainerValue<C, Integer, V, ? extends EntryItem<C, Integer, V>>, V extends DataValue<?>>(C container, int index, V value) implements ListItem<C, V> {
    @Override
    public Integer key() {
        return index;
    }
}
