package local.ytk.app.ds.data.codecs;

import com.mojang.datafixers.util.Either;
import local.ytk.util.Result;

public record EitherCodec<A, B>(Codec<A> aCodec, Codec<B> bCodec) implements Codec<Either<A, B>> {
    @Override
    public <O> Result<Either<A, B>> decode(Ops<O> ops, O o) {
        return null;
    }
    
    @Override
    public <O> Result<O> encode(Ops<O> ops, Either<A, B> abEither, O n) {
        return null;
    }
}
