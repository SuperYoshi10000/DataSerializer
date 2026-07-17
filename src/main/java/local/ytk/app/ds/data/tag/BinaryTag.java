package local.ytk.app.ds.data.tag;

import io.netty.buffer.*;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import org.jetbrains.annotations.NotNull;

import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.RandomAccess;
import java.util.stream.IntStream;

public class BinaryTag extends WrappedByteBuf implements SelfObjectTag<BinaryTag> {
    public static final byte TYPE = 7;
    
    public static final BinaryTag EMPTY = new BinaryTag(Unpooled.EMPTY_BUFFER);
    
    public BinaryTag() {
        super(ByteBufAllocator.DEFAULT.directBuffer(0, Integer.MAX_VALUE));
    }
    public BinaryTag(ByteBuf buffer) {
        super(buffer);
    }
    
    public static BinaryTag of(byte... array) {
        return new BinaryTag(Unpooled.wrappedBuffer(array));
    }
    public static BinaryTag of(@NotNull ByteBuf buffer) {
        return new BinaryTag(buffer);
    }
    public static BinaryTag of(@NotNull ByteBuffer buffer) {
        return of(Unpooled.wrappedBuffer(buffer));
    }
    public static BinaryTag of(@NotNull String s) {
        return of(s.getBytes());
    }
    public static BinaryTag of(@NotNull Object o) {
        return switch (o) {
            case byte[] a -> of(a);
            case ByteBufConvertible b -> of(b);
            case ByteBuffer b -> of(b);
            case CharSequence s -> of(s.toString());
            case ShortBuffer b -> of(ByteBuffer.allocate(b.capacity() * 4).asShortBuffer().put(b));
            case IntBuffer b -> of(ByteBuffer.allocate(b.capacity() * 4).asIntBuffer().put(b));
            case LongBuffer b -> of(ByteBuffer.allocate(b.capacity() * 4).asLongBuffer().put(b));
            case FloatBuffer b -> of(ByteBuffer.allocate(b.capacity() * 4).asFloatBuffer().put(b));
            case DoubleBuffer b -> of(ByteBuffer.allocate(b.capacity() * 4).asDoubleBuffer().put(b));
            case short[] a -> of(ShortBuffer.wrap(a));
            case int[] a -> of(IntBuffer.wrap(a));
            case long[] a -> of(LongBuffer.wrap(a));
            case float[] a -> of(FloatBuffer.wrap(a));
            case double[] a -> of(DoubleBuffer.wrap(a));
            case char[] a -> of(CharBuffer.wrap(a));
            default -> of(o.toString());
        };
    }
    
    @Override
    public boolean serialize(ByteBuf buffer) {
        buffer.writeInt(capacity());
        buffer.writeBytes(byteArray());
        return true;
    }
    
    public static BinaryTag readString(ByteBuf buffer) {
        return of(buffer.readBytes(buffer.readInt()));
    }
    
    public static BinaryTag deserialize(ByteBuf buffer) {
        int size = buffer.readInt();
        ByteBuf newBuffer = buffer.readBytes(size);
        return new BinaryTag(newBuffer);
    }
    
    @Override
    public String toTagString() {
        return "BIN\"" + StringTag.encodeEscapes(new String(bytes())) + "\"";
    }
    
    @Override
    public BinaryTag get() {
        return this;
    }
    
    @Override
    public byte getId() {
        return TYPE;
    }
    
    public byte[] bytes() {
        byte[] array = new byte[writerIndex()];
        getBytes(0, array);
        return array;
    }
    public byte[] byteArray() {
        if (hasArray()) return array();
        return bytes();
    }
    public ByteList byteList() {
        return ByteArrayList.wrap(byteArray());
    }
    
    public int size() {
        return buf.writerIndex(); // assume it will not be moved backwards
    }
    
    
}
