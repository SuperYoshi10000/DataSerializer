package local.ytk.app.ds.val.c;

import local.ytk.app.ds.entry.EntryItem;
import local.ytk.app.ds.val.DataValue;

import java.util.Collection;

public interface ContainerValue<C extends ContainerValue<C, K, V, E>, K, V extends DataValue<?>, E extends EntryItem<C, K, V>> extends DataValue<C> {
    Collection<E> entries();
}
