package local.ytk.app.ds.data.codecs;

import local.ytk.util.Result;
import org.apache.commons.lang3.tuple.Pair;

public record BothCodec<A, B>(Codec<A> aCodec, Codec<B> bCodec) implements Codec<Pair<A, B>> {
    @Override
    public <O> Pair<A, B> decode(Ops<O> ops, O o) {
        return null;
    }
    
    @Override
    public <O> Result<O> encode(Ops<O> ops, Pair<A, B> abPair, O n) {
        return bCodec.encode(ops, abPair.getRight(), n).flatMap(x -> aCodec.encode(ops, abPair.getLeft(), x));
    }
}
