package local.ytk.app.ds.data.save;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;

@FunctionalInterface
public interface Serializer<T> {
    Serializer<ByteBuf> BUFFER = b -> b;
    Serializer<File> FILE = named((name, b) -> {
        File f = FileSystems.getDefault().getPath(name).toFile();
        try (FileOutputStream fos = new FileOutputStream(f)) {
            FileChannel channel = fos.getChannel();
            channel.write(b.nioBuffer());
            return f;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });
    
    default T serialize(String name, Serializable serializable) {
        return serialize(name, serializeToBuffer(serializable));
    }
    default T serialize(Serializable serializable) {
        return serialize(serializeToBuffer(serializable));
    }
    default T serialize(String name, ByteBuf serializable) {
        return serialize(serializable);
    }
    T serialize(ByteBuf serializable);
    
    static ByteBuf serializeToBuffer(Serializable serializable) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(0);
        boolean success = serializable.serialize(buffer);
        if (!success) {
            buffer.release();
            return null;
        }
        return buffer;
    }
    
    static void checkReadable(ByteBuf buffer) {
        if (!buffer.isReadable()) throw new IllegalArgumentException("Buffer is empty");
    }
    
    static void checkReadable(ByteBuf buffer, int length) {
        if (buffer.readableBytes() < length) throw new IllegalArgumentException("Buffer is too short (must be at least " + length + " bytes, but is " + buffer.readableBytes() + " bytes)");
    }
    
    static void checkReadable(ByteBuf buffer, String msg) {
        if (!buffer.isReadable()) throw new IllegalArgumentException(msg + " - Buffer is empty");
    }
    
    static void checkReadable(ByteBuf buffer, int length, String msg) {
        if (buffer.readableBytes() < length) throw new IllegalArgumentException(msg + " - Buffer is too short (must be at least " + length + " bytes, but is " + buffer.readableBytes() + " bytes)");
    }
    
    static <T> Serializer.Named<T> named(Serializer.Named<T> s) {
        return s;
    }
    
    @FunctionalInterface
    interface Named<T> extends Serializer<T> {
        default T serialize(ByteBuf serializable) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        T serialize(String name, ByteBuf serializable);
    }
}