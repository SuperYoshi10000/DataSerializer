package local.ytk.app.ds.data.tag;

import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.List;

public class TypedListTag<T extends Tag, S extends TypedListTag<T, S>> extends AbstractListTag<T, S> {
    public TypedListTag() {}
    @SafeVarargs
    public TypedListTag(T... tags) {
        this(List.of(tags));
    }
    public TypedListTag(Collection<T> tags) {
        super(tags);
    }
    
    public byte getId() {
        return (byte)(getItemId() + ARRAY_TYPE_OFFSET);
    }
    
    @Override
    public S objectValue() {
        return (S) this;
    }
    
    public byte getItemId() {
        return size() > 0 ? getFirst().getId() : 0;
    }
    public String toTagString() {
        if (size() < 1) return "[:]";
        return "[" + TAG_CODES[getItemId()] + ":" + toString().substring(1);
    }
    
    @Override
    public TypedListTag<T, S> toTagList() {
        return this;
    }
    
    @Override
    public boolean serialize(ByteBuf buffer) {
        buffer.writeByte(getItemId());
        buffer.writeInt(size());
        for (T tag : this) if (!tag.serialize(buffer)) return false;
        buffer.writeByte(END);
        return true;
    }
    
    
    
    public static <T extends Tag> Generic<T> generic() {
        return new Generic<>();
    }
    @SafeVarargs
    public static <T extends Tag> Generic<T> generic(T... tags) {
        return new Generic<>(tags);
    }
    public static <T extends Tag> Generic<T> generic(Collection<T> tags) {
        return new Generic<>(tags);
    }
    
    public static class Generic<T extends Tag> extends TypedListTag<T, Generic<T>> implements SequenceTag<T, T, Generic<T>, Generic<T>> {
        public Generic() {}
        @SafeVarargs
        public Generic(T... tags) {
            super(tags);
        }
        public Generic(Collection<T> tags) {
            super(tags);
        }
    }
}
