package local.ytk.app.ds.transform;

import local.ytk.app.ds.data.tag.Tag;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface DataTransformer<T, R> {
    
    DataReader<T> reader();
    DataCreator<R> creator();
    
    default R transform(T input) {
        return switch (reader().read(input).get()) {
            case Boolean _ -> transformString(input);
            case Number _ -> transformNumber(input);
            case Character _ -> transformChar(input);
            case String _ -> transformString(input);
            case null -> creator().createNull();
            default -> creator().create(reader().read(input).get());
        };
    }
    
    
    default R transformBoolean(T input) {
        return creator().createBoolean(reader().readBoolean(input).get());
    }
    default R transformByte(T input) {
        return creator().createByte(reader().readByte(input).get());
    }
    default R transformShort(T input) {
        return creator().createShort(reader().readShort(input).get());
    }
    default R transformInt(T input) {
        return creator().createInt(reader().readInt(input).get());
    }
    default R transformLong(T input) {
        return creator().createLong(reader().readLong(input).get());
    }
    default R transformFloat(T input) {
        return creator().createFloat(reader().readFloat(input).get());
    }
    default R transformDouble(T input) {
        return creator().createDouble(reader().readDouble(input).get());
    }
    default R transformChar(T input) {
        return creator().createChar(reader().readChar(input).get());
    }
    default R transformNumber(T input) {
        return creator().createNumber(reader().readNumber(input).get());
    }
    default R transformString(T input) {
        return creator().createString(reader().readString(input).get());
    }
    default R transformList(T input) {
        return creator().createList((ArrayList<R>) reader().readList(input).get().stream().map(this::transform).collect(Collectors.toCollection(ArrayList::new)));
    }
    default R transformMap(T input) {
        return creator().createMap((Map<String, R>) reader().readMap(input).get().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> transform(e.getValue()),
                (a, b) -> b,
                LinkedHashMap::new
        )));
    }
    
    default <U> DataTransformer<T, U> map(DataTransformer<R, U> next) {
        return new DataTransformer.Impl<>(this.reader(), next.creator());
    }
    
    static <T, R> Impl<T, R> create(DataReader<T> reader, DataCreator<R> creator) {
        return new Impl<>(reader, creator);
    }
    record Impl<T, R>(DataReader<T> reader, DataCreator<R> creator) implements DataTransformer<T, R> {}
}
