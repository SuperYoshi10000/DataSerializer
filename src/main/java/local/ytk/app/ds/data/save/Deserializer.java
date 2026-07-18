package local.ytk.app.ds.data.save;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;

@FunctionalInterface
public interface Deserializer<T> {
    Deserializer<ByteBuf> BUFFER = b -> b;
    Deserializer<File> FILE = f -> {
        try (FileInputStream fis = new FileInputStream(f)) {
            return Unpooled.wrappedBuffer(fis.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    
    ByteBuf deserialize(T value);
}
