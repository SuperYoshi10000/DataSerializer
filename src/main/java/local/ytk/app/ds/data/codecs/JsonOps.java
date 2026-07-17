package local.ytk.app.ds.data.codecs;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.POJONode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonOps implements Ops<JsonNode> {
    public static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;
    @Override
    public Object getObject(JsonNode node) {
        return switch (node.getNodeType()) {
            case ARRAY -> node.values();
            case BINARY -> node.binaryValue();
            case BOOLEAN -> node.booleanValue();
            case NUMBER -> node.numberValue();
            case OBJECT -> node.asObject().properties().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            case POJO -> ((POJONode) node).getPojo();
            case STRING -> node.stringValue();
            default -> null;
        };
    }
    @Override
    public <U> U convert(JsonNode node, Ops<U> outOps) {
        return null;
    }
    @Override
    public JsonNode ofEmpty() {
        return FACTORY.nullNode();
    }
    @Override
    public JsonNode ofNumber(Number n) {
        return FACTORY.numberNode(n.doubleValue());
    }
    @Override
    public JsonNode ofString(String s) {
        return FACTORY.stringNode(s);
    }
    @Override
    public JsonNode ofChar(char c) {
        return FACTORY.stringNode(String.valueOf(c));
    }
    @Override
    public JsonNode ofBool(boolean b) {
        return FACTORY.booleanNode(b);
    }
    @Override
    public Number getNumber(JsonNode node) {
        return node.isNumber() ? node.numberValue() : 0d;
    }
    @Override
    public String getString(JsonNode node) {
        return node.isString() ? node.stringValue() : node.toString();
    }
    @Override
    public char getChar(JsonNode node) {
        return node.isString() ? node.stringValue().charAt(0) : 0;
    }
    @Override
    public boolean getBool(JsonNode node) {
        return switch (node.getNodeType()) {
            case ARRAY -> !node.isEmpty();
            case BINARY -> node.binaryValue().length > 0;
            case BOOLEAN -> node.booleanValue();
            case MISSING, NULL -> false;
            case NUMBER -> node.doubleValue() != 0d;
            case OBJECT -> true;
            case POJO -> ((POJONode) node).getPojo() != null;
            case STRING -> !node.stringValue().isEmpty();
        };
    }
    @Override
    public JsonNode ofStream() {
        return ofList();
    }
    @Override
    public <E extends JsonNode> JsonNode ofStream(E item) {
        return ofList(item);
    }
    @Override
    public <E extends JsonNode> JsonNode ofStream(Stream<E> s) {
        return ofList(s.toList());
    }
    @Override
    public <E extends JsonNode> JsonNode mergeStream(JsonNode stream, E item) {
        return null;
    }
    @Override
    public <E extends JsonNode> JsonNode mergeStreams(JsonNode stream, Stream<E> items) {
        return null;
    }
    @Override
    public JsonNode mergeStreams(JsonNode stream1, JsonNode stream2) {
        return null;
    }
    @Override
    public <E> Stream<E> getStream(JsonNode node) {
        return null;
    }
    @Override
    public JsonNode ofList() {
        return FACTORY.arrayNode();
    }
    @Override
    public <E extends JsonNode> JsonNode ofList(E item) {
        ArrayNode array = FACTORY.arrayNode();
        array.add(item);
        return array;
    }
    @Override
    public <E extends JsonNode> JsonNode ofList(Collection<E> l) {
        ArrayNode array = FACTORY.arrayNode();
        l.forEach(array::add);
        return array;
    }
    @Override
    public <E extends JsonNode> JsonNode mergeList(JsonNode list, E item) {
        return null;
    }
    @Override
    public <E extends JsonNode> JsonNode mergeLists(JsonNode list, List<E> items) {
        return null;
    }
    @Override
    public JsonNode mergeLists(JsonNode list1, JsonNode list2) {
        return null;
    }
    @Override
    public <E extends JsonNode> E add(JsonNode list) {
        return null;
    }
    @Override
    public <E extends JsonNode> E add(JsonNode list, int index) {
        return null;
    }
    @Override
    public <E extends JsonNode> E set(JsonNode list, int index, E value) {
        return null;
    }
    @Override
    public <E extends JsonNode> E get(JsonNode list, int index) {
        return null;
    }
    @Override
    public <E extends JsonNode> E remove(JsonNode list, int index) {
        return null;
    }
    @Override
    public <E> List<E> getList(JsonNode node) {
        return null;
    }
    @Override
    public JsonNode ofMap() {
        return null;
    }
    @Override
    public <K extends JsonNode, V extends JsonNode> JsonNode toMap(K k, V v) {
        return null;
    }
    @Override
    public <K extends JsonNode, V extends JsonNode> JsonNode ofMap(Map<K, V> m) {
        return null;
    }
    @Override
    public <K extends JsonNode, V extends JsonNode> JsonNode mergeMap(JsonNode map, K k, V v) {
        return null;
    }
    @Override
    public <K extends JsonNode, V extends JsonNode> JsonNode mergeMap(JsonNode map, Map<K, V> items) {
        return null;
    }
    @Override
    public JsonNode mergeMaps(JsonNode map1, JsonNode map2) {
        return null;
    }
    @Override
    public <K extends JsonNode, V extends JsonNode> V set(JsonNode map, K k, V v) {
        return null;
    }
    @Override
    public <K extends JsonNode, V extends JsonNode> V get(JsonNode map, K k) {
        return null;
    }
    @Override
    public <K extends JsonNode, V extends JsonNode> V remove(JsonNode map, K k) {
        return null;
    }
    @Override
    public <K, V> Map<K, V> getMap(JsonNode node) {
        return null;
    }
}
