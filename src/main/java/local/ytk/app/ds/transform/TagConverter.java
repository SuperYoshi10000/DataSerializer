package local.ytk.app.ds.transform;

import local.ytk.app.ds.data.tag.*;
import local.ytk.app.ds.entry.ListEntry;
import local.ytk.app.ds.entry.MapEntry;
import local.ytk.util.Result;
import local.ytk.app.ds.val.DataValue;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Map;

public class TagConverter implements DataConverter<Tag> {
    public static final TagConverter INSTANCE = new TagConverter();
    
    private TagConverter() {}
    
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
        return mergeListOrMap(first, second);
    }
    
    @Override
    public Tag mergeMap(Tag first, Tag second) {
        return first instanceof ObjectTag<?, ?> o ? addAllMap(o.copy(), second) : null;
    }
    
    @Override
    public Tag mergeList(Tag first, Tag second) {
        return first instanceof ObjectTag<?, ?> o ? addAllList(o.copy(), second) : null;
    }
    
    @Override
    public Tag addAll(Tag first, Tag second) {
        return addAllListOrMap(first, second);
    }
    
    @Override
    public Tag addAllMap(Tag first, Tag second) {
        return first instanceof Map<?, ?> d1 && second instanceof Map<?, ?> d2 && d1.getClass() == d2.getClass() ? (Tag) _addAllMap(d1, d2) : null;
    }
    private <K, V> Map<K, V> _addAllMap(Map<K, V> first, Map<?, ?> second) {
        first.putAll((Map<K, V>) second);
        return first;
    }
    
    @Override
    public Tag addAllList(Tag first, Tag second) {
        return first instanceof SequenceTag<?,?,?,?> s1 && second instanceof SequenceTag<?,?,?,?> s2 ? (Tag) _addAllList(s1, s2) : null;
    }
    private <T> List<T> _addAllList(List<T> first, List<?> second) {
        first.addAll((List<T>) second);
        return first;
    }
    
    @Override
    public Tag copy(Tag input) {
        return input instanceof Cloneable ? ObjectUtils.clone(input) : input;
    }
    
    @Override
    public Class<Tag> getOperatorClass() {
        return Tag.class;
    }
    
    @Override
    public Tag create(Object input) {
        return DataConverter.super.create(input);
    }
    
    @Override
    public Tag createBoolean(boolean input) {
        return BooleanTag.of(input);
    }
    
    @Override
    public Tag createByte(byte input) {
        return ByteTag.of(input);
    }
    
    @Override
    public Tag createShort(short input) {
        return ShortTag.of(input);
    }
    
    @Override
    public Tag createInt(int input) {
        return IntTag.of(input);
    }
    
    @Override
    public Tag createLong(long input) {
        return LongTag.of(input);
    }
    
    @Override
    public Tag createFloat(float input) {
        return FloatTag.of(input);
    }
    
    @Override
    public Tag createDouble(double input) {
        return DoubleTag.of(input);
    }
    
    @Override
    public Tag createChar(char input) {
        return CharTag.of(input);
    }
    
    @Override
    public Tag createNumber(Number input) {
        return DoubleTag.of(input);
    }
    
    @Override
    public Tag createString(String input) {
        return new StringTag(input);
    }
    
    @Override
    public Tag createList(List<? extends Tag> input) {
        ListTag tag = new ListTag();
        tag.addAll(input);
        return tag;    }
    
    @Override
    public Tag createMap(Map<String, ? extends Tag> input) {
        CompoundTag tag = new CompoundTag();
        tag.putAll(input);
        return tag;
    }
    
    @Override
    public Tag createNull() {
        return Tag.Null.INSTANCE;
    }
    
    @Override
    public Tag createEnd() {
        return Tag.End.INSTANCE;
    }
    
    @Override
    public Tag createEmpty() {
        return Tag.Null.INSTANCE;
    }
    
    @Override
    public Tag createEmptyList() {
        return new ListTag();
    }
    
    @Override
    public Tag createEmptyMap() {
        return new CompoundTag();
    }
    
    @Override
    public Class<Tag> getCreatorClass() {
        return Tag.class;
    }
    
    @Override
    public Result<Object> read(Tag input) {
        return Result.success(input.objectValue());
    }
    
    @Override
    public Result<Boolean> readBoolean(Tag input) {
        return input instanceof BooleanTag b ? Result.success(b.getBoolean()) : input instanceof NumericTag<?, ?> n ? Result.success(!n.isZero()) :
                input instanceof StringTag(String value) ? Result.success(!value.isEmpty()) : Result.failure();
    }
    
    @Override
    public Result<Byte> readByte(Tag input) {
        return readNumber(input).map(Number::byteValue);
    }
    
    @Override
    public Result<Short> readShort(Tag input) {
        return readNumber(input).map(Number::shortValue);
    }
    
    @Override
    public Result<Integer> readInt(Tag input) {
        return readNumber(input).map(Number::intValue);
    }
    
    @Override
    public Result<Long> readLong(Tag input) {
        return readNumber(input).map(Number::longValue);
    }
    
    @Override
    public Result<Float> readFloat(Tag input) {
        return readNumber(input).map(Number::floatValue);
    }
    
    @Override
    public Result<Double> readDouble(Tag input) {
        return readNumber(input).map(Number::doubleValue);
    }
    
    @Override
    public Result<Character> readChar(Tag input) {
        return null;
    }
    
    @Override
    public Result<Number> readNumber(Tag input) {
        return input instanceof NumericTag<?,?> n ? Result.success(n) : Result.failure();
    }
    
    @Override
    public Result<String> readString(Tag input) {
        return Result.success(input.toString());
    }
    
    @Override
    public Result<List<Tag>> readList(Tag input) {
        return input instanceof SequenceTag<?,?,?,?> s ? Result.success((List<Tag>) s.toTagList()) : Result.failure();
    }
    
    @Override
    public Result<Map<String, Tag>> readMap(Tag input) {
        return input instanceof MapTag<?, ?> m ? Result.success((MapTag<Tag, ?>) m) : Result.failure();
    }
    
    @Override
    public Class<Tag> getReaderClass() {
        return Tag.class;
    }
    
    @Override
    public boolean isMap(Tag input) {
        return input instanceof DictionaryTag;
    }
    
    @Override
    public boolean isList(Tag input) {
        return input instanceof SequenceTag;
    }
}
