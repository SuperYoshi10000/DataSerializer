package local.ytk.app.ds.data.tag;

import local.ytk.app.ds.val.DataValue;
import local.ytk.util.annotation.AutoGeneric;

public interface TypedTag<V, T extends TypedTag<V, T>> extends Tag, DataValue<V> {
    
    @Override
    default Tag toTag() {
        return this;
    }
    
    @SuppressWarnings("unchecked")
    default <@AutoGeneric O, S extends SequenceTag<V, T, O, S>> TagType<V, T, S> getType() {
        return TagType.of(this);
    }
    @Override
    default String getTypeName() {
        return getType().name();
    }
    @Override
    default String getTypeCode() {
        return getType().code();
    }
    
    @Override
    default byte getId() {
        return TagType.getId(this);
    }
    
    @Override
    default V objectValue() {
        return get();
    }
}
