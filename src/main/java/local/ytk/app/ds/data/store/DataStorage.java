package local.ytk.app.ds.data.store;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Function;
import java.util.stream.IntStream;

public record DataStorage(ByteBuf buffer) implements Data {
    public DataStorage(int buffer) {
        this(Unpooled.wrappedBuffer(new byte[buffer]));
    }
    
    @Override
    public ByteBuf getData() {
        return buffer.asReadOnly();
    }
    
    @Override
    public ByteBuf getData(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getData'");
    }
    
    @Override
    public byte[] getBytes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBytes'");
    }
    
    @Override
    public ByteBuf read(int index, int length) {
        return Unpooled.copiedBuffer(buffer.slice(index, length).array());
    }
    
    @Override
    public ByteBuf slice(int index, int length) {
        return buffer.slice(index, length);
    }
    
    @Override
    public ByteBuf readRange(int start, int end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readRange'");
    }
    
    @Override
    public boolean readBoolean(int index, byte offset) {
        if (offset < 0 || offset > 7) throw new IllegalArgumentException("Offset must be between 0 and 7");
        return (buffer.getByte(index) >> 7 - offset) % 2 == 0;
    }
    
    @Override
    public byte readByte(int index) {
        return buffer.getByte(index);
    }
    
    @Override
    public short readShort(int index) {
        return buffer.getShort(index);
    }
    
    @Override
    public int readInt(int index) {
        return buffer.getInt(index);
    }
    
    @Override
    public long readLong(int index) {
        return buffer.getLong(index);
    }
    
    @Override
    public float readFloat(int index) {
        return buffer.getFloat(index);
    }
    
    @Override
    public double readDouble(int index) {
        return buffer.getDouble(index);
    }
    
    @Override
    public char readChar(int index) {
        return buffer.getChar(index);
    }
    
    @Override
    public boolean[] readBooleans(int index, int count, byte bitOffset) {
        return IntStream.of(readInts(index, Math.ceilDiv(count, 32) + 1)).mapToObj(n -> {
            boolean[] booleans = new boolean[32];
            for (int i = 0; i < 32; ++i) {
                booleans[i] = (n & (1 << i)) != 0;
            }
            return booleans;
        }).reduce(new boolean[0], ArrayUtils::addAll);
    }
    
    @Override
    public byte[] readBytes(int index, int count) {
        byte[] dst = new byte[count];
        buffer.getBytes(index, dst);
        return dst;
    }
    
    @Override
    public short[] readShorts(int index, int count) {
        short[] dst = new short[count];
        buffer.nioBuffer().asShortBuffer().get(index, dst);
        return dst;
    }
    
    @Override
    public int[] readInts(int index, int count) {
        int[] dst = new int[count];
        buffer.nioBuffer().asIntBuffer().get(index, dst);
        return dst;
    }
    
    @Override
    public long[] readLongs(int index, int count) {
        long[] dst = new long[count];
        buffer.nioBuffer().asLongBuffer().get(index, dst);
        return dst;
    }
    
    @Override
    public float[] readFloats(int index, int count) {
        float[] dst = new float[count];
        buffer.nioBuffer().asFloatBuffer().get(index, dst);
        return dst;
    }
    
    @Override
    public double[] readDoubles(int index, int count) {
        double[] dst = new double[count];
        buffer.nioBuffer().asDoubleBuffer().get(index, dst);
        return dst;
    }
    
    @Override
    public char[] readChars(int index, int count) {
        char[] dst = new char[count];
        buffer.nioBuffer().asCharBuffer().get(index, dst);
        return dst;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <D extends Data> D readData(int index, int length, D data) throws ClassCastException {
        return (D) data.put(readBytes(index, length));
    }
    
    @Override
    public <D extends Data> D[] readData(int index, int length, D[] data) {
        for (int i = 0; i < data.length; i++) {
            readData(index + i * length, length, data[i]);
        }
        return data;
    }
    
    @Override
    public <T> T readData(int index, int length, Function<byte[], T> converter) {
        return converter.apply(readBytes(index, length));
    }
    
    @Override
    public <T> T[] readData(int index, int length, Function<byte[], T> converter, T[] results) {
        for (int i = 0; i < results.length; i++) {
            readData(index + i * length, length, converter);
        }
        return results;
    }
    
    public DataStorage writeBit(int index, byte offset, boolean bit) {
        byte value = buffer.getByte(index);
        value |= (byte) ((bit ? 1 : 0) << (7 - offset));
        buffer.setByte(index, value);
        return this;
    }
    
    public DataStorage writeBits(int index, byte offset, boolean bit1, boolean bit2, boolean bit3, boolean bit4, boolean bit5, boolean bit6, boolean bit7, boolean bit8) {
        short value = buffer.getShort(index);
        value |= (short) ((bit1 ? 128 : 0) << (15 - offset));
        value |= (short) ((bit2 ? 64 : 0) << (15 - offset));
        value |= (short) ((bit3 ? 32 : 0) << (15 - offset));
        value |= (short) ((bit4 ? 16 : 0) << (15 - offset));
        value |= (short) ((bit5 ? 8 : 0) << (15 - offset));
        value |= (short) ((bit6 ? 4 : 0) << (15 - offset));
        value |= (short) ((bit7 ? 2 : 0) << (15 - offset));
        value |= (short) ((bit8 ? 1 : 0) << (15 - offset));
        buffer.setShort(index, value);
        return this;
    }
    
    public DataStorage writeBits(int index, byte bitOffset, boolean... bits) {
        int end = Math.floorDiv(bits.length, 8);
        for (int i = 0; i < end; i++) {
            writeBits(index + i, bitOffset, bits[i], bits[i + 1], bits[i + 2], bits[i + 3], bits[i + 4], bits[i + 5], bits[i + 6], bits[i + 7]);
        }
        return this;
    }
    
    public DataStorage write(int index, byte... data) {
        buffer.setBytes(index, data);
        return this;
    }
    
    public DataStorage write(int index, short... data) {
        for (short s : data) {
            buffer.setShort(index, s);
        }
        return this;
    }
    
    public DataStorage write(int index, int... data) {
        for (int i : data) {
            buffer.setInt(index, i);
        }
        return this;
    }
    
    public DataStorage write(int index, long... data) {
        for (long l : data) {
            buffer.setLong(index, l);
        }
        return this;
    }
    
    public DataStorage write(int index, float... data) {
        for (float f : data) {
            buffer.setFloat(index, f);
        }
        return this;
    }
    
    public DataStorage write(int index, double... data) {
        for (double d : data) {
            buffer.setDouble(index, d);
        }
        return this;
    }
    
    public DataStorage write(int index, Data data) {
        buffer.setBytes(index, data.getBytes());
        return this;
    }
    
    @Override
    public DataStorage put(byte[] bytes) {
        buffer.setBytes(0, bytes);
        return this;
    }
}
