package local.ytk.app.ds.data.codecs;

public interface Decoder<T> {
    <O> T decode(Ops<O> ops, O o);
}
