package local.ytk.app.ds.val.p.num;

public interface LongValue extends NumericValue<Long> {
    long getLong();
    
    @Override
    default Long get() {
        return getLong();
    }
}
