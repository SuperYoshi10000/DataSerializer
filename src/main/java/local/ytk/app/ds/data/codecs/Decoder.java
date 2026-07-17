package local.ytk.app.ds.data.codecs;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.tag.Tag;

public interface Decoder<T> {
    <O> T decode(Ops<O> ops, O o);
    
//    default T deserialize(ByteBuf buf) {
//        Tag.deserialize(buf);
//    }
}
