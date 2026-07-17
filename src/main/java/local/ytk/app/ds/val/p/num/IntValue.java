package local.ytk.app.ds.val.p.num;

public interface IntValue extends NumericValue<Integer> {
    int getInt();
    
    @Override
    default Integer get() {
        return getInt();
    }
}
