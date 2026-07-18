package local.ytk.app.ds.transform;

import local.ytk.app.ds.data.tag.Tag;
import tools.jackson.databind.JsonNode;

public abstract class AbstractDataTransformer<T, R> implements DataTransformer<T, R> {
    public static final DataTransformer<JsonNode, Tag> JSON_TO_TAG = new AbstractDataTransformer<>(JsonConverter.INSTANCE, TagConverter.INSTANCE) {};
    public static final DataTransformer<Tag, JsonNode> TAG_TO_JSON = new AbstractDataTransformer<>(TagConverter.INSTANCE, JsonConverter.INSTANCE) {};
    
    private final DataReader<T> reader;
    private final DataCreator<R> creator;
    
    public AbstractDataTransformer(DataReader<T> reader, DataCreator<R> creator) {
        this.reader = reader;
        this.creator = creator;
    }
    
    @Override
    public DataReader<T> reader() {
        return reader;
    }
    
    @Override
    public DataCreator<R> creator() {
        return creator;
    }
}
