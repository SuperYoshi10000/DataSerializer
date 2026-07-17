package local.ytk.app.ds.data.codecs;

import local.ytk.util.Result;
import local.ytk.util.tuple.Either;

public record XorCodec<A, B>(Codec<A> aCodec, Codec<B> bCodec) implements Codec<Either<A, B>> {
    @Override
    public <O> Either<A, B> decode(Ops<O> ops, O o) {
        return null;
    }
    
    @Override
    public <O> Result<O> encode(Ops<O> ops, Either<A, B> abEither, O n) {
        return null;
    }
}
