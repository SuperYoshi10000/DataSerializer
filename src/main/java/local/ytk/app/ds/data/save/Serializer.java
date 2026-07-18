package local.ytk.app.ds.data.save;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface Serializer<T, S extends Serializable> {
    Serializer<ByteBuf, ?> BUFFER = b -> b;
    Serializer<File, ?> FILE = named((name, b) -> {
        File f = FileSystems.getDefault().getPath(name).toFile();
        try (FileOutputStream fos = new FileOutputStream(f)) {
            FileChannel channel = fos.getChannel();
            channel.write(b.nioBuffer());
            return f;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });
    
    default T serialize(String name, S serializable) {
        return serialize(name, serializeToBuffer(serializable));
    }
    default T serialize(S serializable) {
        return serialize(serializeToBuffer(serializable));
    }
    default T serialize(String name, ByteBuf serializable) {
        return serialize(serializable);
    }
    T serialize(ByteBuf serializable);
    
    static <S extends Serializable> ByteBuf serializeToBuffer(S serializable, BiConsumer<ByteBuf, S> initializer) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(0);
        initializer.accept(buffer, serializable);
        return _serializeToBuffer(serializable, buffer);
    }
    
    private static <S extends Serializable> @Nullable ByteBuf _serializeToBuffer(S serializable, ByteBuf buffer) {
        boolean success = serializable.serialize(buffer);
        if (!success) {
            buffer.release();
            return null;
        }
        return buffer;
    }
    
    static <S extends Serializable> ByteBuf serializeToBuffer(S serializable) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(0);
        return _serializeToBuffer(serializable, buffer);
    }
    
    default <U extends S> Serializer<T, U> onInit(Initializer<U> initializer) {
        return new Serializer<>() {
            @Override
            public T serialize(ByteBuf serializable) {
                return Serializer.this.serialize(serializable);
            }
            
            @Override
            public T serialize(U serializable) {
                return Serializer.this.serialize(serializeToBuffer(serializable, initializer));
            }
        };
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
    
    static <T, S extends Serializable> Serializer.Named<T, S> named(Serializer.Named<T, S> s) {
        return s;
    }
    
    @FunctionalInterface
    interface Named<T, S extends Serializable> extends Serializer<T, S> {
        default T serialize(ByteBuf serializable) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        T serialize(String name, ByteBuf serializable);
        
        default <U extends S> Named<T, U> onInit(Initializer<U> initializer) {
            return new Named<>() {
                @Override
                public T serialize(String name, ByteBuf serializable) {
                    return Named.this.serialize(name, serializable);
                }
                
                @Override
                public T serialize(String name, U serializable) {
                    return Named.this.serialize(name, serializeToBuffer(serializable, initializer));
                }
            };
        }
    }
    
    interface Initializer<S extends Serializable> extends BiConsumer<ByteBuf, S> {}
}