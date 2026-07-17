package local.ytk.app.ds.val.p.num;

public interface FloatValue extends NumericValue<Float> {
    float getFloat();
    
    @Override
    default Float get() {
        return getFloat();
    }
}
