package local.ytk.app.ds.transform;

import local.ytk.app.ds.data.tag.MapTag;
import local.ytk.app.ds.data.tag.Tag;
import local.ytk.app.ds.entry.ListEntry;
import local.ytk.app.ds.entry.MapEntry;
import local.ytk.util.Result;
import local.ytk.app.ds.val.DataValue;

import java.util.List;
import java.util.Map;

public class TagConverter implements DataConverter<Tag> {
    @Override
    public Tag get(Tag input, String key) {
        return input instanceof MapTag<?, ?> mapTag ? mapTag.get(key) : null;
    }
    
    @Override
    public Tag getOrDefault(Tag input, String key, Tag defaultValue) {
        return DataConverter.super.getOrDefault(input, key, defaultValue);
    }
    
    @Override
    public Tag get(Tag input, int index) {
        return null;
    }
    
    @Override
    public Tag getOrDefault(Tag input, int index, Tag defaultValue) {
        return DataConverter.super.getOrDefault(input, index, defaultValue);
    }
    
    @Override
    public Tag set(Tag input, String key, Tag value) {
        return null;
    }
    
    @Override
    public Tag set(Tag input, int index, Tag value) {
        return null;
    }
    
    @Override
    public Tag insert(Tag input, int index, Tag value) {
        return null;
    }
    
    @Override
    public Tag add(Tag input, Tag value) {
        return null;
    }
    
    @Override
    public Tag with(Tag input, String key, Tag value) {
        return null;
    }
    
    @Override
    public Tag set(Tag input, MapEntry<?, DataValue<Tag>> entry) {
        return DataConverter.super.set(input, entry);
    }
    
    @Override
    public Tag set(Tag input, ListEntry<DataValue<Tag>> entry) {
        return DataConverter.super.set(input, entry);
    }
    
    @Override
    public Tag merge(Tag first, Tag second) {
        return null;
    }
    
    @Override
    public Tag mergeMap(Tag first, Tag second) {
        return DataConverter.super.mergeMap(first, second);
    }
    
    @Override
    public Tag mergeList(Tag first, Tag second) {
        return DataConverter.super.mergeList(first, second);
    }
    
    @Override
    public Tag addAll(Tag first, Tag second) {
        return null;
    }
    
    @Override
    public Tag addAllMap(Tag first, Tag second) {
        return DataConverter.super.addAllMap(first, second);
    }
    
    @Override
    public Tag addAllList(Tag first, Tag second) {
        return DataConverter.super.addAllList(first, second);
    }
    
    @Override
    public Tag copy(Tag input) {
        return null;
    }
    
    @Override
    public Class<Tag> getOperatorClass() {
        return null;
    }
    
    @Override
    public Tag create(Object input) {
        return DataConverter.super.create(input);
    }
    
    @Override
    public Tag createBoolean(boolean input) {
        return null;
    }
    
    @Override
    public Tag createByte(byte input) {
        return null;
    }
    
    @Override
    public Tag createShort(short input) {
        return null;
    }
    
    @Override
    public Tag createInt(int input) {
        return null;
    }
    
    @Override
    public Tag createLong(long input) {
        return null;
    }
    
    @Override
    public Tag createFloat(float input) {
        return null;
    }
    
    @Override
    public Tag createDouble(double input) {
        return null;
    }
    
    @Override
    public Tag createChar(char input) {
        return null;
    }
    
    @Override
    public Tag createNumber(Number input) {
        return null;
    }
    
    @Override
    public Tag createString(String input) {
        return null;
    }
    
    @Override
    public Tag createList(List<? extends Tag> input) {
        return null;
    }
    
    @Override
    public Tag createMap(Map<String, ? extends Tag> input) {
        return null;
    }
    
    @Override
    public Tag createNull() {
        return null;
    }
    
    @Override
    public Tag createEnd() {
        return null;
    }
    
    @Override
    public Tag createEmpty() {
        return null;
    }
    
    @Override
    public Tag createEmptyList() {
        return null;
    }
    
    @Override
    public Tag createEmptyMap() {
        return null;
    }
    
    @Override
    public Class<Tag> getCreatorClass() {
        return null;
    }
    
    @Override
    public Result<Object> read(Tag input) {
        return null;
    }
    
    @Override
    public Result<Boolean> readBoolean(Tag input) {
        return null;
    }
    
    @Override
    public Result<Byte> readByte(Tag input) {
        return null;
    }
    
    @Override
    public Result<Short> readShort(Tag input) {
        return null;
    }
    
    @Override
    public Result<Integer> readInt(Tag input) {
        return null;
    }
    
    @Override
    public Result<Long> readLong(Tag input) {
        return null;
    }
    
    @Override
    public Result<Float> readFloat(Tag input) {
        return null;
    }
    
    @Override
    public Result<Double> readDouble(Tag input) {
        return null;
    }
    
    @Override
    public Result<Character> readChar(Tag input) {
        return null;
    }
    
    @Override
    public Result<Number> readNumber(Tag input) {
        return null;
    }
    
    @Override
    public Result<String> readString(Tag input) {
        return null;
    }
    
    @Override
    public Result<List<Tag>> readList(Tag input) {
        return null;
    }
    
    @Override
    public Result<Map<String, Tag>> readMap(Tag input) {
        return null;
    }
    
    @Override
    public Class<Tag> getReaderClass() {
        return null;
    }
}
