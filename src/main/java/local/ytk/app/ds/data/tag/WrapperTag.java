package local.ytk.app.ds.data.tag;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.type.TypeKey;

public class WrapperTag<T extends Tag> implements ObjectTag<T, WrapperTag<T>> {
    public final T tag;
    
    public WrapperTag(T tag) {
        this.tag = tag;
    }
    
    @Override
    public T get() {
        return tag;
    }
    
    @Override
    public String toTagString() {
        return "(" + tag.toTagString() + ")";
    }
    
    @Override
    public StringTag toStringTag() {
        return tag.toStringTag();
    }
    
    @Override
    public boolean serialize(ByteBuf buffer) {
        return Tag.serializeEntry(tag, buffer);
    }
    
    public static class WithMetadata<T extends Tag, D> extends WrapperTag<T> {
        final D metadata;
        
        public WithMetadata(T tag, D metadata) {
            super(tag);
            this.metadata = metadata;
        }
        
        public D metadata() {
            return metadata;
        }
    }
    
    public static class TypeDefined<V, D, T extends TypedTag<? extends D, T>> extends WithMetadata<T, TypeKey<V, D>> {
        public TypeDefined(T tag, TypeKey<V, D> type) {
            super(tag, type);
        }
        
        @Override
        public boolean serialize(ByteBuf buffer) {
            buffer.writeByte(tag.getId());
            return metadata.serializer().applyAsBoolean(buffer, tag.get()) && Tag.serialize(tag, buffer);
        }
        
        @Override
        public String toTagString() {
            return "(" + metadata + ": " + tag.toTagString() + ")";
        }
    }
    
    public static class TypeDefinedBinary<V> extends TypeDefined<V, BinaryTag, BinaryTag> {
        public TypeDefinedBinary(BinaryTag tag, TypeKey<V, BinaryTag> type) {
            super(tag, type);
        }
    }
}
