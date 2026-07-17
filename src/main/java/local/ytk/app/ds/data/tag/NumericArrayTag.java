package local.ytk.app.ds.data.tag;

import java.lang.reflect.InvocationTargetException;

public abstract class NumericArrayTag<N extends Number, T extends NumericTag<N, T>, O, S extends NumericArrayTag<N, T, O, S>> extends AbstractArrayTag<N, T, O, S> {
    public abstract Object toPrimitiveArray();
    
    @Override
    public byte getId() {
        return (byte) (getItemId() + ARRAY_TYPE_OFFSET);
    }
}
