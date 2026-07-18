package local.ytk.app.ds.data.save;

public interface IOSerializer<T> extends Serializer<T>, Deserializer<T> {
    interface Named<T> extends Serializer.Named<T>, Deserializer<T> {}
}
