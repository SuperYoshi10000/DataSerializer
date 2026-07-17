package local.ytk.app.ds.val.c;

import local.ytk.app.ds.entry.EntryItem;
import local.ytk.app.ds.entry.ListItem;
import local.ytk.app.ds.val.DataValue;

import java.util.Collection;

public interface ListValue<C extends ListValue<C, V, E>, V extends DataValue<?>, E extends ListItem<C, V>> extends ContainerValue<C, Integer, V, E> {
    Collection<V> items();
}
