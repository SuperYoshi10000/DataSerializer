package local.ytk.app.ds.val.p.num;

public interface DoubleValue extends NumericValue<Double> {
    double getDouble();
    
    @Override
    default Double get() {
        return getDouble();
    }
}
