package local.ytk.app.ds.data.type;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.codecs.Codec;
import local.ytk.util.Result;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

import java.util.function.Function;

public record TypeKey<T, D>(String name, Class<T> type, Codec<T> codec, ToBooleanBiFunction<ByteBuf, D> serializer, Function<ByteBuf, Result<D>> deserializer) {
    public TypeKey(Class<T> type, Codec<T> codec, ToBooleanBiFunction<ByteBuf, D> serializer, Function<ByteBuf, Result<D>> deserializer) {
        this(type.getSimpleName(), type, codec, serializer, deserializer);
    }
//    public TypeKey(Class<T> type, Codec<T> codec) {
//        this(type.getSimpleName(), type, codec, serializer, deserializer);
//    }
}
