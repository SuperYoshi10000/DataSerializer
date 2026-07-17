package local.ytk.app.ds.data.tag;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public record RepeatedTag<T extends TypedTag<T, T>, S extends RepeatedTag<T, S>>(T tag, int count) implements SequenceTag<T, T, S> {
    @Override
    public byte getId() {
        return TypedListTag.ARRAY_TYPE_OFFSET;
    }
    
    @Override
    public byte getItemId() {
        return tag.getId();
    }
    @Override
    public String toTagString() {
        if (count == 0) return "[]";
        return "[" + count + "*" + tag.toTagString() + "]";
    }
    
    @Override
    public List<T> toTagList() {
        return List.of();
    }
    
    @Override
    public void addTag(T tag) {
    
    }
    
    @Override
    public int size() {
        return count;
    }
    
    @Override
    public boolean isEmpty() {
        return count == 0;
    }
    
    @Override
    public boolean contains(Object o) {
        return tag.equals(o);
    }
    
    @Override
    public @NotNull Iterator<T> iterator() {
        return listIterator();
    }
    
    @Override
    public @NotNull Object[] toArray() {
        return new Object[0];
    }
    
    @Override
    public @NotNull <T1> T1[] toArray(@NotNull T1 @NotNull [] a) {
        return null;
    }
    
    @Override
    public boolean add(T t) {
        return false;
    }
    
    @Override
    public boolean remove(Object o) {
        return false;
    }
    
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return c.isEmpty() || c.size() == 1 && contains(c.iterator().next());
    }
    
    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return false;
    }
    
    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return false;
    }
    
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }
    
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }
    
    @Override
    public void clear() {
    
    }
    
    @Override
    public T get(int index) {
        return index < count ? tag : null;
    }
    
    @Override
    public T set(int index, T element) {
        return null;
    }
    
    @Override
    public void add(int index, T element) {
    
    }
    
    @Override
    public T remove(int index) {
        return null;
    }
    
    @Override
    public int indexOf(Object o) {
        return tag.equals(o) ? 0 : -1;
    }
    
    @Override
    public int lastIndexOf(Object o) {
        return tag.equals(o) ? count - 1 : -1;
    }
    
    @Override
    public @NotNull ListIterator<T> listIterator() {
        return new RepeatedTagIterator(0);
    }
    
    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return new RepeatedTagIterator(index);
    }
    
    @Override
    public @NotNull List<T> subList(int fromIndex, int toIndex) {
        return new RepeatedTag<>(tag, toIndex - fromIndex);
    }
    
    private class RepeatedTagIterator implements ListIterator<T> {
        private int index;
        
        public RepeatedTagIterator(int index) {
            this.index = index;
        }
        
        @Override
        public boolean hasNext() {
            return index < count;
        }
        
        @Override
        public T next() {
            return index++ < count ? tag : null;
        }
        
        @Override
        public boolean hasPrevious() {
            return index > 0;
        }
        
        @Override
        public T previous() {
            return index-- > 0 ? tag : null;
        }
        
        @Override
        public int nextIndex() {
            return index + 1;
        }
        
        @Override
        public int previousIndex() {
            return index - 1;
        }
        
        @Override
        public void remove() {}
        
        @Override
        public void set(T t) {}
        
        @Override
        public void add(T t) {}
    }
}
