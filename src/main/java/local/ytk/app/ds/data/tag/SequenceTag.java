package local.ytk.app.ds.data.tag;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;

public interface SequenceTag<V, T extends Tag, O, S extends SequenceTag<V, T, O, S>> extends List<V>, ObjectTag<O, S>/*, ListValue<S, T, ListItem<S, T>>*/ {
    byte ARRAY_TYPE_OFFSET = 16;
    Object[] EMPTY_ARRAY = new Object[0];
    
    @SuppressWarnings("unchecked")
    static <V, T extends Tag, S extends Empty<V, T>> Empty<V, T> empty() {
        return (Empty<V, T>) Empty.INSTANCE;
    }
    
    byte getId();
    byte getItemId();
    String toTagString();
    List<T> toTagList();
    
    void addTag(T tag);
    
    @Override
    default boolean serialize(ByteBuf buffer) {
        buffer.writeByte(getId());
        int sizeIndex = buffer.writerIndex();
        buffer.writeInt(0);
        int startIndex = buffer.writerIndex();
        for (V item : this) if (!(item instanceof Tag tag && tag.serialize(buffer))) return false;
        int endIndex = buffer.writerIndex();
        int size = endIndex - startIndex;
        buffer.setInt(sizeIndex, size);
        return true;
    }
    
    @Override
    int size();
    
    class Empty<V, T extends Tag> extends ObjectLists.EmptyList<V> implements SequenceTag<V, T, Empty<V, T>, Empty<V, T>>, RandomAccess {
        public static final Empty<?, ?> INSTANCE = new Empty<>();
        
        private Empty() {}
        
        public byte getId() {
            return ARRAY_TYPE_OFFSET;
        }
        
        @Override
        public @NotNull Object @NotNull [] toArray() {
            return EMPTY_ARRAY;
        }
        
//        @Override
//        public V objectValue() {
//            return this;
//        }
        
        @Override
        public boolean serialize(ByteBuf buffer) {
            buffer.writeByte(getId());
            buffer.writeInt(0);
            return true;
        }
        
        public byte getItemId() {
            return 0;
        }
        
        @Override
        public String toTagString() {
            return "[]";
        }
        
        @Override
        public List<T> toTagList() {
            return List.of();
        }
        
        @Override
        public V get(int index) {
            return null;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public void addTag(T tag) {
            throw new UnsupportedOperationException("Cannot modify empty sequence");
        }
        
        @Override
        public Empty<V, T> get() {
            return null;
        }
    }
}
