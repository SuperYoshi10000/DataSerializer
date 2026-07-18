package local.ytk.app.ds.data.tag;

import java.io.Serializable;
import java.util.SequencedMap;

public interface DictionaryTag<K, T extends Tag, M extends DictionaryTag<K, T, M>> extends SequencedMap<K, T>, SelfObjectTag<M>, DictionaryTag<String, Tag, DictionaryTag<String, Tag, ?>> {
    @Override
    int size();
}
