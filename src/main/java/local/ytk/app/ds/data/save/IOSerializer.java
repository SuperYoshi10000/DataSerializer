package local.ytk.app.ds.data.save;

public interface IOSerializer<T, S extends Serializable> extends Serializer<T, S>, Deserializer<T> {
    interface Named<T, S extends Serializable> extends Serializer.Named<T, S>, Deserializer<T> {}
}
