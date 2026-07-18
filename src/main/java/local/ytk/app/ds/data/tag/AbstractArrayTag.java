package local.ytk.app.ds.data.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractArrayTag<V, T extends Tag, O, S extends AbstractArrayTag<V, T, O, S>> extends ArrayList<V> implements SequenceTag<V, T, O, S> {
    public AbstractArrayTag() {}
    @SafeVarargs
    public AbstractArrayTag(V... tags) {
        this(List.of(tags));
    }
    public AbstractArrayTag(Collection<V> tags) {
        super(tags);
    }
    
    @Override
    public String toString() {
        return toTagString();
    }
    
    @Override
    public String toTagString() {
        return "[:" + toString().substring(1);
    }
}
