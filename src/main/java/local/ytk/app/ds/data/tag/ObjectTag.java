package local.ytk.app.ds.data.tag;

import java.io.Serializable;

public interface ObjectTag<V, T extends ObjectTag<V, T>> extends Cloneable, Serializable, TypedTag<V, T> {
    @SuppressWarnings("unchecked")
    default T copy() {
        try {
            return (T) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
