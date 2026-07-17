package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;

public record ListItemImpl<C, V extends DataValue>(C container, int index, V value) implements ListItem<C, V> {
    @Override
    public Integer key() {
        return index;
    }
}
