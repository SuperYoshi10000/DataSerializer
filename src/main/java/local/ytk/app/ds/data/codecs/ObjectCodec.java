package local.ytk.app.ds.data.codecs;

public class ObjectCodec<T> extends AbstractCodec<T> {
    public ObjectCodec(Encoder<T> encoder, Decoder<T> decoder) {
        super(encoder, decoder);
    }
}
