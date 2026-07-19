package local.ytk.app.ds.data.save;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufConvertible;

public interface Serializable extends ByteBufConvertible {
    boolean serialize(ByteBuf buffer);
    
    @Override
    default ByteBuf asByteBuf() {
        return Serializer.serializeToBufferDefault(this);
    }
}
