package local.ytk.app.ds.data.tag;

import local.ytk.app.ds.data.codecs.Ops;
import local.ytk.util.Result;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class TagOps implements Ops<Tag> {
    public static final TagOps INSTANCE = new TagOps();
    
    private TagOps() {}
    
    @Override
    public <U> U convert(Tag tag, Ops<U> outOps) {
        return null;
    }
    @Override
    public TypedTag.Null ofEmpty() {
        return TypedTag.Null.INSTANCE;
    }
    @Override
    public NumericTag<?, ?> ofNumber(Number n) {
        return null;
    }
    //todo numbers...
    public ByteTag ofByte(byte b) {
        return ByteTag.of(b);
    }
    public ShortTag ofShort(short s) {
        return ShortTag.of(s);
    }
    public IntTag ofInt(int i) {
        return IntTag.of(i);
    }
    public LongTag ofLong(long l) {
        return LongTag.of(l);
    }
    public FloatTag ofFloat(float f) {
        return FloatTag.of(f);
    }
    public DoubleTag ofDouble(double d) {
        return DoubleTag.of(d);
    }
    @Override
    public Tag ofString(String s) {
        return StringTag.of(s);
    }
    @Override
    public Tag ofChar(char c) {
        return ShortTag.of(c);
    }
    @Override
    public Tag ofBool(boolean b) {
        return ByteTag.of(b);
    }
    @Override
    public Result<Number> getNumber(Tag tag) {
        return null;
    }
    public Result<Byte> getByte(Tag t) {
        return Result.success(t instanceof NumericTag<?, ?> n ? n.byteValue() : Byte.parseByte(t.toString()));
    }
    public Result<Short> getShort(Tag t) {
        return Result.success(t instanceof NumericTag<?, ?> n ? n.shortValue() : Short.parseShort(t.toString()));
    }
    public Result<Integer> getInt(Tag t) {
        return Result.success(t instanceof NumericTag<?, ?> n ? n.intValue() : Integer.parseInt(t.toString()));
    }
    public Result<Long> getLong(Tag t) {
        return Result.success(t instanceof NumericTag<?, ?> n ? n.longValue() : Long.parseLong(t.toString()));
    }
    public Result<Float> getFloat(Tag t) {
        return Result.success(t instanceof NumericTag<?, ?> n ? n.floatValue() : Float.parseFloat(t.toString()));
    }
    public Result<Double> getDouble(Tag t) {
        return Result.success(t instanceof NumericTag<?, ?> n ? n.doubleValue() : Double.parseDouble(t.toString()));
    }
    @Override
    public Result<String> getString(Tag tag) {
        return Result.success(tag.toString());
    }
    @Override
    public Result<Character> getChar(Tag tag) {
        return Result.success(tag instanceof NumericTag<?, ?> n ? (char)n.shortValue() : tag.toString().charAt(0));
    }
    @Override
    public Result<Boolean> getBool(Tag tag) {
        return Result.success(tag instanceof NumericTag<?, ?> n ? !n.isZero() : !(tag instanceof StringTag s) || s.truthy());
    }
    @Override
    public Tag ofStream() {
        return SequenceTag.empty();
    }
    private <V, T extends Tag, U extends TypedTag<V, U>, S extends SequenceTag<V, T, ?, S>> S toArray(TypedTag<V, U> item) {
        S sequence = (S) item.getType().array();
        sequence.addTag((T) item);
        return sequence;
    }
    @Override
    public <E extends Tag> SequenceTag<?, ?, ?, ?> ofStream(E item) {
        return toArray((TypedTag<?, ?>) item);
    }
    @Override
    public <E extends Tag> Tag ofStream(Stream<E> s) {
        return null;
    }
    @Override
    public <E extends Tag> E mergeStream(Tag stream, E item) {
        return null;
    }
    @Override
    public <E extends Tag> Tag mergeStreams(Tag stream, Stream<E> items) {
        return null;
    }
    @Override
    public Tag mergeStreams(Tag stream1, Tag stream2) {
        return null;
    }
    @Override
    public <E> Result<Stream<E>> getStream(Tag tag) {
        return null;
    }
    @Override
    public ListTag ofList() {
        return new ListTag();
    }
    @Override
    public <E extends Tag> Tag ofList(Collection<E> l) {
        return new ListTag();
    }
    @Override
    public <E extends Tag> Tag mergeList(Tag list, E item) {
        return null;
    }
    @Override
    public <E extends Tag> Tag mergeLists(Tag list, List<E> items) {
        return null;
    }
    @Override
    public Tag mergeLists(Tag list1, Tag list2) {
        return null;
    }
    @Override
    public <E extends Tag> E add(Tag list) {
        return null;
    }
    @Override
    public <E extends Tag> E add(Tag list, int index) {
        return null;
    }
    @Override
    public <E extends Tag> E set(Tag list, int index, E value) {
        return null;
    }
    @Override
    public <E extends Tag> E get(Tag list, int index) {
        return null;
    }
    @Override
    public <E extends Tag> E remove(Tag list, int index) {
        return null;
    }
    @Override
    public <E> Result<List<E>> getList(Tag tag) {
        return null;
    }
    @Override
    public Tag ofMap() {
        return null;
    }
    @Override
    public <K extends Tag, V extends Tag> Tag toMap(K k, V v) {
        return null;
    }
    @Override
    public <K extends Tag, V extends Tag> Tag ofMap(Map<K, V> m) {
        return null;
    }
    @Override
    public <K extends Tag, V extends Tag> Tag mergeMap(Tag map, K k, V v) {
        return null;
    }
    @Override
    public <K extends Tag, V extends Tag> Tag mergeMap(Tag map, Map<K, V> items) {
        return null;
    }
    @Override
    public Tag mergeMaps(Tag map1, Tag map2) {
        return null;
    }
    @Override
    public <K extends Tag, V extends Tag> V set(Tag map, K k, V v) {
        return null;
    }
    @Override
    public <K extends Tag, V extends Tag> V get(Tag map, K k) {
        return null;
    }
    @Override
    public <K extends Tag, V extends Tag> V remove(Tag map, K k) {
        return null;
    }
    @Override
    public <K, V> Result<Map<K, V>> getMap(Tag tag) {
        return null;
    }
}
