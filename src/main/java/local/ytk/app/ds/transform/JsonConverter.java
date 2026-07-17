package local.ytk.app.ds.transform;

import local.ytk.app.ds.res.Result;
import tools.jackson.core.JsonParser;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonConverter implements DataConverter<JsonNode> {
    public static final JsonConverter INSTANCE = new JsonConverter();
    
    private JsonConverter() {}
    
    @Override
    public JsonNode create(Object input) {
        return DataConverter.super.create(input);
    }
    
    @Override
    public BooleanNode createBoolean(boolean input) {
        return JsonNodeFactory.instance.booleanNode(input);
    }
    
    @Override
    public NumericNode createByte(byte input) {
        return JsonNodeFactory.instance.numberNode(input);
    }
    
    @Override
    public NumericNode createShort(short input) {
        return JsonNodeFactory.instance.numberNode(input);
    }
    
    @Override
    public NumericNode createInt(int input) {
        return JsonNodeFactory.instance.numberNode(input);
    }
    
    @Override
    public NumericNode createLong(long input) {
        return JsonNodeFactory.instance.numberNode(input);
    }
    
    @Override
    public NumericNode createFloat(float input) {
        return JsonNodeFactory.instance.numberNode(input);
    }
    
    @Override
    public NumericNode createDouble(double input) {
        return JsonNodeFactory.instance.numberNode(input);
    }
    
    @Override
    public JsonNode createChar(char input) {
        return JsonNodeFactory.instance.stringNode(String.valueOf(input));
    }
    
    @Override
    public NumericNode createNumber(Number input) {
        return JsonNodeFactory.instance.numberNode(input.doubleValue());
    }
    
    @Override
    public StringNode createString(String input) {
        return JsonNodeFactory.instance.stringNode(input);
    }
    
    @Override
    public ArrayNode createList(List<? extends JsonNode> input) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(input.size());
        node.addAll(input);
        return node;
    }
    
    @Override
    public ObjectNode createMap(Map<String, ? extends JsonNode> input) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.setAll(input);
        return node;
    }
    
    @Override
    public NullNode createNull() {
        return JsonNodeFactory.instance.nullNode();
    }
    
    @Override
    public NullNode createEnd() {
        return JsonNodeFactory.instance.nullNode();
    }
    
    @Override
    public NullNode createEmpty() {
        return JsonNodeFactory.instance.nullNode();
    }
    
    @Override
    public ArrayNode createEmptyList() {
        return JsonNodeFactory.instance.arrayNode();
    }
    
    @Override
    public ObjectNode createEmptyMap() {
        return JsonNodeFactory.instance.objectNode();
    }
    
    @Override
    public JsonNode get(JsonNode input, String key) {
        return input.get(key);
    }
    
    @Override
    public JsonNode getOrDefault(JsonNode input, String key, JsonNode defaultValue) {
        return input.has(key) ? input.get(key) : defaultValue;
    }
    
    @Override
    public JsonNode get(JsonNode input, int index) {
        return input.get(index);
    }
    
    @Override
    public JsonNode getOrDefault(JsonNode input, int index, JsonNode defaultValue) {
        return input.has(index) ? input.get(index) : defaultValue;
    }
    
    @Override
    public ObjectNode set(JsonNode input, String key, JsonNode value) {
        return input.asObject().set(key, value);
    }
    
    @Override
    public ArrayNode set(JsonNode input, int index, JsonNode value) {
        return input.asArray().set(index, value);
    }
    
    @Override
    public ArrayNode insert(JsonNode input, int index, JsonNode value) {
        return input.asArray().insert(index, value);
    }
    
    @Override
    public ArrayNode add(JsonNode input, JsonNode value) {
        return input.asArray().add(value);
    }
    
    @Override
    public ObjectNode with(JsonNode input, String key, JsonNode value) {
        return JsonNodeFactory.instance.objectNode().setAll(input.asObject()).set(key, value);
    }
    
    @Override
    public JsonNode merge(JsonNode first, JsonNode second) {
        return switch (first) {
            case ObjectNode n -> n.deepCopy().setAll(second.asObject());
            case ArrayNode n -> n.deepCopy().addAll(second.asArray());
            case NumericNode n -> JsonNodeFactory.instance.numberNode(n.numberValue().doubleValue() + second.asDouble());
            case StringNode n -> JsonNodeFactory.instance.stringNode(n.stringValue() + second.stringValue());
            case null, default -> first;
        };
    }
    
    @Override
    public JsonNode mergeMap(JsonNode first, JsonNode second) {
        return first.asObject().deepCopy().setAll(second.asObject());
    }
    
    @Override
    public JsonNode mergeList(JsonNode first, JsonNode second) {
        return first.asArray().deepCopy().addAll(second.asArray());
    }
    
    @Override
    public JsonNode addAll(JsonNode first, JsonNode second) {
        return switch (first) {
            case ObjectNode n -> n.setAll(second.asObject());
            case ArrayNode n -> n.addAll(second.asArray());
            default -> first;
        };
    }
    
    @Override
    public JsonNode addAllMap(JsonNode first, JsonNode second) {
        return first.asObject().setAll(second.asObject());
    }
    
    @Override
    public JsonNode addAllList(JsonNode first, JsonNode second) {
        return first.asArray().addAll(second.asArray());
    }
    
    @Override
    public JsonNode copy(JsonNode input) {
        return input.deepCopy();
    }
    
    @Override
    public Result<Object> read(JsonNode input) {
        return switch (input) {
            case BooleanNode n -> Result.success(n.booleanValue());
            case NumericNode n -> Result.success(n.numberValue());
            case StringNode n -> Result.success(n.stringValue());
            case ArrayNode n -> Result.success(n.asArray()
                    .values()
                    .stream()
                    .map(this::read)
                    .map(Result::get)
                    .collect(Collectors.toCollection(ArrayList::new))
            );
            case ObjectNode n -> Result.success(n.asObject()
                    .properties()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> read(e.getValue()).get(),
                            (a, b) -> b,
                            LinkedHashMap::new
                    ))
            );
            case NullNode _ -> Result.success(null);
            default -> Result.failure();
        };
    }
    
    @Override
    public Result<Boolean> readBoolean(JsonNode input) {
        return input instanceof BooleanNode n ? Result.success(n.booleanValue()) : Result.failure();
    }
    
    @Override
    public Result<Byte> readByte(JsonNode input) {
        return input instanceof NumericNode n ? Result.success(n.numberValue().byteValue()) : Result.failure();
    }
    
    @Override
    public Result<Short> readShort(JsonNode input) {
        return input instanceof NumericNode n ? Result.success(n.shortValue()) : Result.failure();
    }
    
    @Override
    public Result<Integer> readInt(JsonNode input) {
        return input instanceof NumericNode n ? Result.success(n.intValue()) : Result.failure();
    }
    
    @Override
    public Result<Long> readLong(JsonNode input) {
        return input instanceof NumericNode n ? Result.success(n.longValue()) : Result.failure();
    }
    
    @Override
    public Result<Float> readFloat(JsonNode input) {
        return input instanceof NumericNode n ? Result.success(n.floatValue()) : Result.failure();
    }
    
    @Override
    public Result<Double> readDouble(JsonNode input) {
        return input instanceof NumericNode n ? Result.success(n.doubleValue()) : Result.failure();
    }
    
    @Override
    public Result<Character> readChar(JsonNode input) {
        return input instanceof StringNode n ? Result.success(n.stringValue().charAt(0)) : Result.failure();
    }
    
    @Override
    public Result<Number> readNumber(JsonNode input) {
        return input instanceof NumericNode n ? Result.success(n.numberValue()) : Result.failure();
    }
    
    @Override
    public Result<String> readString(JsonNode input) {
        return input instanceof StringNode n ? Result.success(n.stringValue()) : Result.failure();
    }
    
    @Override
    public Result<List<JsonNode>> readList(JsonNode input) {
        return input instanceof ArrayNode n ? Result.success(new ArrayList<>(n.values())) : Result.failure();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Result<Map<String, JsonNode>> readMap(JsonNode input) {
        return input instanceof ObjectNode n ? Result.success(new LinkedHashMap<>(Map.ofEntries(n.properties().toArray(Map.Entry[]::new)))) : Result.failure();
    }
    
    @Override
    public Class<JsonNode> getCreatorClass() {
        return JsonNode.class;
    }
    
    @Override
    public Class<JsonNode> getOperatorClass() {
        return JsonNode.class;
    }
    
    @Override
    public Class<JsonNode> getReaderClass() {
        return JsonNode.class;
    }
}
