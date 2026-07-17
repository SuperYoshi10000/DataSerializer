package local.ytk.app.ds.val;

import local.ytk.app.ds.val.p.PrimitiveValue;

public interface EmptyValue extends PrimitiveValue<Object> {
    @Override
    default Object get() {
        return null;
    }
}
