package local.ytk.app.ds.data.codecs;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.tag.TagOps;
import local.ytk.util.Result;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;

@FunctionalInterface
public interface Encoder<T> {
    default <O> Result<O> encode(Ops<O> ops, T t) {
        return encode(ops, t, ops.ofEmpty());
    }
    default <S extends HasOps<S, Ops<S>>> Result<S> encode(S t, T n) {
        return encode(t.getOps(), n, t);
    }
    <O> Result<O> encode(Ops<O> ops, T t, O n);
    
    default boolean serialize(ByteBuf buf, T t) {
        try {
            return encode(TagOps.INSTANCE, t).getOrThrow().serialize(buf);
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    static <O1, T> Encoder<T> of(BiFunction<Ops<? super O1>, T, ? super O1> function) {
        return new Encoder<>() {
            @Override
            public <O2> Result<O2> encode(Ops<O2> ops, T t, O2 n) {
                return (Result<O2>) Result.of(function.apply((Ops<O1>) ops, t));
            }
        };
    }
}
