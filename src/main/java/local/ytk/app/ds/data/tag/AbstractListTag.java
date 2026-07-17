package local.ytk.app.ds.data.tag;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public abstract class AbstractListTag<T extends Tag, S extends AbstractListTag<T, S>> extends AbstractArrayTag<T, T, S> {
    public AbstractListTag() {}
    @SafeVarargs
    public AbstractListTag(T... tags) {
        this(List.of(tags));
    }
    public AbstractListTag(Collection<T> tags) {
        super(tags);
    }
    
    @Override
    public S objectValue() {
        return (S) this;
    }
    
    @Override
    public AbstractListTag<T, S> toTagList() {
        return this;
    }
    
    @Override
    public Tag @NotNull [] toArray() {
        Tag[] array = new Tag[size()];
        for (int i = 0; i < size(); i++) array[i] = get(i);
        return array;
    }
    
    @Override
    public void addTag(T tag) {
        add(tag);
    }
}
