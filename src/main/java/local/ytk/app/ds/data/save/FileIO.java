package local.ytk.app.ds.data.save;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;

public class FileIO implements IOSerializer.Named<File, Serializable> {
    public static final FileIO DEFAULT = new FileIO();
    
    @Override
    public ByteBuf deserialize(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return Unpooled.wrappedBuffer(fis.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public File serialize(String name, ByteBuf buf) {
        File f = FileSystems.getDefault().getPath(name).toFile();
        try (FileOutputStream fos = new FileOutputStream(f)) {
            FileChannel channel = fos.getChannel();
            channel.write(buf.nioBuffer());
            return f;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
