package local.ytk.app.ds.data.tag;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StringArrayTag extends AbstractArrayTag<String, StringTag, String[], StringArrayTag> {
    public String @NotNull [] toArray() {
        return this.toArray(String[]::new);
    }
    
    @Override
    public byte getId() {
        return StringTag.TYPE + ARRAY_TYPE_OFFSET;
    }
    
    @Override
    public String[] get() {
        return toArray();
    }
    
    @Override
    public byte getItemId() {
        return StringTag.TYPE;
    }
    
    @Override
    public List<StringTag> toTagList() {
        return List.of();
    }
    
    @Override
    public void addTag(StringTag tag) {
    
    }
}
