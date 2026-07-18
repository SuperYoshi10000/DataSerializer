package local.ytk.app.ds.data.codecs;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.tag.Tag;
import local.ytk.util.Result;

public interface Decoder<T> {
    default <O extends HasOps<O, ? extends Ops<O>>> Result<T> decode(O o) {
        return decode(o.getOps(), o);
    }
    <O> Result<T> decode(Ops<O> ops, O o);
    
    default Result<T> deserialize(ByteBuf buf) {
        return this.decode(Tag.deserialize(buf));
    }
}
