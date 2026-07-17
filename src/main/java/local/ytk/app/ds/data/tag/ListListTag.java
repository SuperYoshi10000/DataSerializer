package local.ytk.app.ds.data.tag;

import org.jetbrains.annotations.NotNull;

public class ListListTag extends TypedListTag<ListTag, ListListTag> {
    @Override
    public ListTag @NotNull [] toArray() {
        ListTag[] array = new ListTag[size()];
        for (int i = 0; i < size(); i++) array[i] = get(i);
        return array;
    }
}
