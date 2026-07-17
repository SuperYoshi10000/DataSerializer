package local.ytk.app.ds.entry;

import local.ytk.app.ds.val.DataValue;

public record MapItemImpl<C, V extends DataValue>(C container, String key, V value) implements MapItem<C, V> {
}
