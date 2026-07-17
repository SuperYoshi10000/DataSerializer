package local.ytk.app.ds.data.tag;

public interface SelfObjectTag<T extends SelfObjectTag<T>> extends ObjectTag<T, T> {
    @Override
    default T get() {
        return (T) this;
    }
    
    @Override
    default T objectValue() {
        return (T) this;
    }
}
