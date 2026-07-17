package local.ytk.app.ds.val.p.num;

public interface ByteValue extends NumericValue<Byte> {
    byte getByte();
    
    @Override
    default Byte get() {
        return getByte();
    }
}
