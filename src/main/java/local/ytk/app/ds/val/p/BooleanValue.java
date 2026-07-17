package local.ytk.app.ds.val.p;

public interface BooleanValue extends PrimitiveValue<Boolean> {
    boolean getBoolean();
    
    @Override
    default Boolean get() {
        return getBoolean();
    }
}
