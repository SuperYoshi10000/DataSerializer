package local.ytk.app.ds.data.tag;

import java.util.SequencedMap;

public interface DictionaryTag<K, T extends Tag, M extends DictionaryTag<K, T, M>> extends SequencedMap<K, T>, SelfObjectTag<M> {
    @Override
    int size();
}
