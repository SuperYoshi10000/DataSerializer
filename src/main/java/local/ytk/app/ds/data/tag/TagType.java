// This file is a mess. I should probably clean it up.

package local.ytk.app.ds.data.tag;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Map.entry;

public class TagType<V, T extends Tag, A extends SequenceTag<V, T, ?, A>> {
    static final BiMap<Byte, Class<? extends Tag>> BY_ID = HashBiMap.create(Map.ofEntries(
        entry((byte) 0x00, Tag.Null.class),
        entry((byte) 0x01, ByteTag.class),
        entry((byte) 0x02, ShortTag.class),
        entry((byte) 0x03, IntTag.class),
        entry((byte) 0x04, LongTag.class),
        entry((byte) 0x05, FloatTag.class),
        entry((byte) 0x06, DoubleTag.class),
        entry((byte) 0x07, BinaryTag.class),
        entry((byte) 0x08, StringTag.class),
        entry((byte) 0x09, ListTag.class),
        entry((byte) 0x0A, CompoundTag.class),
        // 0B and 0C reserved
        entry((byte) 0x0D, TypedMapTag.class),
        entry((byte) 0x0E, BigIntTag.class),
        entry((byte) 0x0F, BigDecimalTag.class),

        entry((byte) 0x10, TypedListTag.class),
        entry((byte) 0x11, ByteArrayTag.class),
        entry((byte) 0x12, ShortArrayTag.class),
        entry((byte) 0x13, IntArrayTag.class),
        entry((byte) 0x14, LongArrayTag.class),
        entry((byte) 0x15, FloatArrayTag.class),
        entry((byte) 0x16, DoubleArrayTag.class),
        entry((byte) 0x17, BinaryTag.class),
        entry((byte) 0x18, StringArrayTag.class),
        entry((byte) 0x19, ListListTag.class),
        entry((byte) 0x1A, CompoundListTag.class),
        
        entry((byte) 0x1D, WrapperTag.class),
        entry((byte) 0x1E   , WrapperTag.TypeDefined.class),

        entry((byte) 0xFF, Tag.End.class)
    ));
    
    public static byte getId(Tag tag) {
        return BY_ID.inverse().get(tag.getClass());
    }
    public static byte getType(Tag tag) {
        return BY_ID.inverse().get(tag.getClass());
    }
    
    public TagType(byte id, String name, String code, Class<V> valueClass, V value, T tag, A array) {
        this(id, name, code, valueClass, () -> tag, obj -> tag, str -> tag, buf -> value, buf -> tag, (Class<A>) array.getClass(), () -> array);
    }
    
    // Empty tag types
    public static final TagType<TypedTag.Null, TypedTag.Null, SequenceTag.Empty<TypedTag.Null, TypedTag.Null>> NULL = new TagType<>(Tag.NULL, "null", "null", TypedTag.Null.class, null, TypedTag.Null.INSTANCE, SequenceTag.empty());
    public static final TagType<TypedTag.End, TypedTag.End, SequenceTag.Empty<TypedTag.End, TypedTag.End>> END = new TagType<>(Tag.END, "end", "end", TypedTag.End.class, null, TypedTag.End.INSTANCE, SequenceTag.empty());
    
    // Number tag types
    public static final TagType<Byte, ByteTag, ByteArrayTag> BYTE = new TagType<>(Tag.BYTE, "byte", "b", Byte.class, () -> ByteTag.ZERO, ByteTag::of, ByteTag::of, ByteBuf::readByte, ByteTag::deserialize, ByteArrayTag.class, ByteArrayTag::new);
    public static final TagType<Short, ShortTag, ShortArrayTag> SHORT = new TagType<>(Tag.SHORT, "short", "s", Short.class, () -> ShortTag.ZERO, ShortTag::of, ShortTag::of, ByteBuf::readShort, ShortTag::deserialize, ShortArrayTag.class, ShortArrayTag::new);
    public static final TagType<Integer, IntTag, IntArrayTag> INT = new TagType<>(Tag.INT, "int", "i", Integer.class, () -> IntTag.ZERO, IntTag::of, IntTag::of, ByteBuf::readInt, IntTag::deserialize, IntArrayTag.class, IntArrayTag::new);
    public static final TagType<Long, LongTag, LongArrayTag> LONG = new TagType<>(Tag.LONG, "long", "L", Long.class, () -> LongTag.ZERO, LongTag::of, LongTag::of, ByteBuf::readLong, LongTag::deserialize, LongArrayTag.class, LongArrayTag::new);
    public static final TagType<Float, FloatTag, FloatArrayTag> FLOAT = new TagType<>(Tag.FLOAT, "float", "f", Float.class, () -> FloatTag.ZERO, FloatTag::of, FloatTag::of, ByteBuf::readFloat, FloatTag::deserialize, FloatArrayTag.class, FloatArrayTag::new);
    public static final TagType<Double, DoubleTag, DoubleArrayTag> DOUBLE = new TagType<>(Tag.DOUBLE, "double", "d", Double.class, () -> DoubleTag.ZERO, DoubleTag::of, DoubleTag::of, ByteBuf::readDouble, DoubleTag::deserialize, DoubleArrayTag.class, DoubleArrayTag::new);
    
    // String and collection tag types
    public static final TagType<BinaryTag, BinaryTag, TypedListTag.Generic<BinaryTag>> BINARY = new TagType<BinaryTag, BinaryTag, TypedListTag.Generic<BinaryTag>>((byte) 7, "bin", "B", BinaryTag.class, () -> BinaryTag.EMPTY, BinaryTag::of, BinaryTag::of, BinaryTag::readString, BinaryTag::deserialize,
            (Class<TypedListTag.Generic<BinaryTag>>) (Class<?>) TypedListTag.Generic.class,
            TypedListTag::generic);
    public static final TagType<String, StringTag, StringArrayTag> STRING = new TagType<>(Tag.STRING, "string", "S", String.class, () -> StringTag.EMPTY, StringTag::of, StringTag::of, StringTag::readString, StringTag::deserialize, StringArrayTag.class, StringArrayTag::new);
    public static final TagType<ListTag, ListTag, ListListTag> LIST = new TagType<>(Tag.LIST, "list", "[]", ListTag.class, () -> ListTag.EMPTY, obj -> new ListTag(), ListTag::fromString, ListTag::deserialize, ListTag::deserialize, ListListTag.class, ListListTag::new);
    public static final TagType<CompoundTag, CompoundTag, CompoundListTag> COMPOUND = new TagType<>(Tag.COMPOUND, "compound", "{}", CompoundTag.class, () -> CompoundTag.EMPTY, obj -> new CompoundTag(), CompoundTag::fromString, CompoundTag::deserialize, CompoundTag::deserialize, CompoundListTag.class, CompoundListTag::new);
    
    static final Byte2ObjectOpenHashMap<TagType<?, ?, ?>> TAG_TYPES = new Byte2ObjectOpenHashMap<>(256) {{
        put(Tag.NULL, NULL);
        put(Tag.BYTE, BYTE);
        put(Tag.SHORT, SHORT);
        put(Tag.INT, INT);
        put(Tag.LONG, LONG);
        put(Tag.FLOAT, FLOAT);
        put(Tag.DOUBLE, DOUBLE);
        put(Tag.STRING, STRING);
        put(Tag.LIST, LIST);
        put(Tag.COMPOUND, COMPOUND);
        put(Tag.END, END);
    }};
    static final ObjectCollection<TagType<?, ?, ?>> VALUES = TAG_TYPES.values();
    static final TagType<?, ?, ?>[] VALUES_ARRAY = VALUES.toArray(new TagType<?, ?, ?>[256]);
    private final byte id;
    private final String name;
    private final String code;
    private final Class<V> valueClass;
    private final Supplier<T> emptySupplier;
    private final Function<Object, T> constructor;
    private final Function<String, T> stringConstructor;
    private final Function<ByteBuf, V> bufferConstructor;
    private final Function<ByteBuf, T> bufferTagConstructor;
    private final Class<A> arrayClass;
    private final Supplier<A> arrayConstructor;
    
    public TagType(
            byte id,
            String name,
            String code,
            Class<V> valueClass,
            Supplier<T> emptySupplier,
            Function<Object, T> constructor,
            Function<String, T> stringConstructor,
            Function<ByteBuf, V> bufferConstructor,
            Function<ByteBuf, T> bufferTagConstructor,
            Class<A> arrayClass,
            Supplier<A> arrayConstructor
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.valueClass = valueClass;
        this.emptySupplier = emptySupplier;
        this.constructor = constructor;
        this.stringConstructor = stringConstructor;
        this.bufferConstructor = bufferConstructor;
        this.bufferTagConstructor = bufferTagConstructor;
        this.arrayClass = arrayClass;
        this.arrayConstructor = arrayConstructor;
    }
    
    public static @Nullable TagType<?, ?, ?> fromName(@NotNull String name) {
        return TAG_TYPES.values().stream().filter(type -> type.name().equals(name)).findFirst().orElse(null);
    }
    
    public static @Nullable TagType<?, ?, ?> fromCode(@NotNull String code) {
        return TAG_TYPES.values().stream().filter(type -> type.code().equals(code)).findFirst().orElse(null);
    }
    
    @SuppressWarnings("unchecked")
    public static @Nullable <V> TagType<V, ?, ?> fromClass(@NotNull Class<V> valueClass) {
        return (TagType<V, ?, ?>) TAG_TYPES.values().stream().filter(type -> type.valueClass().equals(valueClass)).findFirst().orElse(null);
    }
    
    public static TagType<?, ?, ?> fromId(byte id) {
        return TAG_TYPES.get(id);
    }
    
    @SuppressWarnings("unchecked")
    public static <V, T extends Tag, O, S extends SequenceTag<V, T, O, S>> TagType<V, T, S> of(Tag tag) {
        return (TagType<V, T, S>) fromId(tag.getId());
    }
    
    public V deserialize(ByteBuf buffer) {
        return bufferConstructor.apply(buffer);
    }
    
    public A deserializeArray(ByteBuf buffer) {
        int size = buffer.readInt();
        A array = arrayConstructor.get();
        for (int i = 0; i < size; i++) array.add(deserialize(buffer));
        return array;
    }
    
    public A array() {
        return arrayConstructor.get();
    }
    
    public A array(V value) {
        A array = array();
        array.add(value);
        return array;
    }
    
    @SafeVarargs
    public final A array(V... values) {
        A array = array();
        Collections.addAll(array, values);
        return array;
    }
    
    public byte id() {
        return id;
    }
    
    public String name() {
        return name;
    }
    
    public String code() {
        return code;
    }
    
    public Class<V> valueClass() {
        return valueClass;
    }
    
    public Supplier<T> emptySupplier() {
        return emptySupplier;
    }
    
    public Function<Object, T> constructor() {
        return constructor;
    }
    
    public Function<String, T> stringConstructor() {
        return stringConstructor;
    }
    
    public Function<ByteBuf, V> bufferConstructor() {
        return bufferConstructor;
    }
    
    public Function<ByteBuf, T> bufferTagConstructor() {
        return bufferTagConstructor;
    }
    
    public Supplier<A> arrayConstructor() {
        return arrayConstructor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TagType) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.valueClass, that.valueClass) &&
                Objects.equals(this.emptySupplier, that.emptySupplier) &&
                Objects.equals(this.constructor, that.constructor) &&
                Objects.equals(this.stringConstructor, that.stringConstructor) &&
                Objects.equals(this.bufferConstructor, that.bufferConstructor) &&
                Objects.equals(this.bufferTagConstructor, that.bufferTagConstructor) &&
                Objects.equals(this.arrayConstructor, that.arrayConstructor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, valueClass, emptySupplier, constructor, stringConstructor, bufferConstructor, bufferTagConstructor, arrayConstructor);
    }
    
    @Override
    public String toString() {
        return "TagType[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "code=" + code + ", " +
                "valueClass=" + valueClass + ", " +
                "emptySupplier=" + emptySupplier + ", " +
                "constructor=" + constructor + ", " +
                "stringConstructor=" + stringConstructor + ", " +
                "bufferConstructor=" + bufferConstructor + ", " +
                "bufferTagConstructor=" + bufferTagConstructor + ", " +
                "arrayConstructor=" + arrayConstructor + ']';
    }
    
    public WithTypedArray<A> arrayType() {
        return new WithTypedArray<>(
                BY_ID.containsValue(arrayClass) ? BY_ID.inverse().get(arrayClass) : 16,
                name + "[]",
                code + "[]",
                arrayClass,
                arrayConstructor,
                o -> arrayConstructor.get(),
                s -> arrayConstructor.get(),
                b -> arrayConstructor.get(),
                b -> arrayConstructor.get()
        );
    }
    
    public static class WithTypedArray<T extends Tag> extends TagType<T, T, TypedListTag.Generic<T>> {
        public WithTypedArray(byte id, String name, String code, Class<T> valueClass, Supplier<T> emptySupplier, Function<Object, T> constructor, Function<String, T> stringConstructor, Function<ByteBuf, T> bufferConstructor, Function<ByteBuf, T> bufferTagConstructor) {
            super(id, name, code, valueClass, emptySupplier, constructor, stringConstructor, bufferConstructor, bufferTagConstructor,
                    (Class<TypedListTag.Generic<T>>) (Class<?>) TypedListTag.Generic.class, TypedListTag::generic);
        }
        
        @SuppressWarnings("unchecked")
        public WithTypedArray<TypedListTag.Generic<T>> arrayType() {
            return new WithTypedArray<>(
                    (byte) 16, name() + "[]", code() + "[]",
                    (Class<TypedListTag.Generic<T>>) (Class<?>) TypedListTag.Generic.class,
                    TypedListTag::generic,
                    o -> TypedListTag.generic(),
                    s -> TypedListTag.generic(),
                    b -> TypedListTag.generic(),
                    b -> TypedListTag.generic()
            );
        }
    }
}

