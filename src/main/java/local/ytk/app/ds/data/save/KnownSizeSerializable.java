package local.ytk.app.ds.data.save;

public interface KnownSizeSerializable extends Serializable {
    default int size() {
        return asByteBuf().writerIndex();
    }
}
