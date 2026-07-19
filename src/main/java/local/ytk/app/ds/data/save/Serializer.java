package local.ytk.app.ds.data.save;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

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
    
    default T serialize(String name, ByteBuf serializable) {
        return serialize(serializable);
    }
    T serialize(ByteBuf serializable);
    
    
    static <T, S extends Serializable> T serialize(Serializer<T, S> serializer, String name, S serializable) {
        return serialize(serializer, name, serializeToBuffer(serializer, serializable));
    }
    static <T, S extends Serializable> T serialize(Serializer<T, S> serializer, S serializable) {
        return serialize(serializer, serializeToBuffer(serializer, serializable));
    }
    static <T, S extends Serializable> T serialize(Serializer<T, S> serializer, String name, ByteBuf serializable) {
        return serializer.serialize(name, serializable);
    }
    static <T, S extends Serializable> T serialize(Serializer<T, S> serializer, ByteBuf serializable) {
        return serializer.serialize(serializable);
    }
    
    static <T, S extends Serializable> T serialize(Serializer<T, S> serializer, String name, S value, ByteBuf buf) {
        return serialize(serializer, name, buf);
    }
    static <T, S extends Serializable> T serialize(Serializer<T, S> serializer, S value, ByteBuf buf) {
        return serialize(serializer, buf);
    }
    
    static <T, S extends Serializable> ByteBuf serializeToBuffer(Serializer<T, S> serializer, S serializable) {
        return serializer.serializeToBuffer(serializable);
    }
    
    
    
    private static <S extends Serializable> @Nullable ByteBuf _serializeToBuffer(S serializable, ByteBuf buffer) {
        boolean success = serializable.serialize(buffer);
        if (!success) {
            buffer.release();
            return null;
        }
        return buffer;
    }
    
    default ByteBuf initBuffer(S serializable) {
        return Unpooled.buffer();
    }
    default ByteBuf serializeToBuffer(S serializable) {
        ByteBuf buffer = initBuffer(serializable);
        return _serializeToBuffer(serializable, buffer);
    }
    
    static <S extends Serializable> ByteBuf serializeToBufferDefault(S serializable) {
        ByteBuf buffer = Unpooled.buffer();
        return _serializeToBuffer(serializable, buffer);
    }
    
    default <U extends S> Serializer<T, U> before(BufferBefore<U> initializer) {
        Serializer<T, S> serializer = this;
        return new Serializer<>() {
            @Override
            public T serialize(ByteBuf serializable) {
                return serializer.serialize(serializable);
            }
            
            @Override
            public T serialize(String name, ByteBuf buf) {
                return serializer.serialize(name, buf);
            }
            
            @Override
            public ByteBuf initBuffer(U serializable) {
                ByteBuf buf = serializer.initBuffer(serializable);
                initializer.accept(buf, serializable);
                return buf;
            }
            
            @Override
            public ByteBuf serializeToBuffer(U serializable) {
                ByteBuf buffer = initBuffer(serializable);
                return _serializeToBuffer(serializable, buffer);
            }
        };
    }
    
    default Serializer<T, S> after(BufferAfter<S> mapper) {
        Serializer<T, S> serializer = this;
        return new Serializer<>() {
            @Override
            public T serialize(ByteBuf serializable) {
                return serializer.serialize(serializable);
            }
            
            @Override
            public T serialize(String name, ByteBuf buf) {
                return serializer.serialize(name, buf);
            }
            
            @Override
            public ByteBuf initBuffer(S serializable) {
                return serializer.initBuffer(serializable);
            }
            
            @Override
            public ByteBuf serializeToBuffer(S serializable) {
                ByteBuf buffer = initBuffer(serializable);
                return mapper.apply(_serializeToBuffer(serializable, buffer), serializable);
            }
        };
    }
    default <R> Serializer<R, S> finalize(BufferFinal<T, R> finalizer) {
        Serializer<T, S> serializer = this;
        return new Serializer<>() {
            @Override
            public R serialize(ByteBuf serializable) {
                return finalizer.apply(serializable, serializer.serialize(serializable));
            }
            
            @Override
            public R serialize(String name, ByteBuf buf) {
                return finalizer.apply(buf, serializer.serialize(name, buf));
            }
            
            @Override
            public ByteBuf initBuffer(S serializable) {
                return serializer.initBuffer(serializable);
            }
            
            @Override
            public ByteBuf serializeToBuffer(S serializable) {
                return serializer.serializeToBuffer(serializable);
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
        @Override
        default T serialize(ByteBuf serializable) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        T serialize(String name, ByteBuf serializable);
    }
    
    interface BufferBefore<S extends Serializable> extends BiConsumer<ByteBuf, S> {}
    interface BufferAfter<S> extends BiFunction<ByteBuf, S, @Nullable ByteBuf> {}
    interface BufferFinal<T, R> extends BiFunction<ByteBuf, T, R> {}
}