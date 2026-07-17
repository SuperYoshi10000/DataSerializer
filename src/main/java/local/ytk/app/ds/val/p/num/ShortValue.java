package local.ytk.app.ds.val.p.num;

public interface ShortValue extends NumericValue<Short> {
    short getShort();
    
    @Override
    default Short get() {
        return getShort();
    }
}
