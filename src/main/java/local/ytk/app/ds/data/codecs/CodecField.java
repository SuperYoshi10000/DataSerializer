package local.ytk.app.ds.data.codecs;

public interface CodecField<T> {
    Codec<T> codec();
    String name();
}
