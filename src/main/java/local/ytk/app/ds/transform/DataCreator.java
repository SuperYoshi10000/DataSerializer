package local.ytk.app.ds.transform;

import tools.jackson.databind.node.*;

import java.util.*;
import java.util.stream.Collectors;

public interface DataCreator<T> extends DataOperator<T> {
    default T create(Object input) {
        return switch (input) {
            case Boolean b -> createBoolean(b);
            case Byte b -> createByte(b);
            case Short s -> createShort(s);
            case Integer i -> createInt(i);
            case Long l -> createLong(l);
            case Float f -> createFloat(f);
            case Double d -> createDouble(d);
            case Character c -> createChar(c);
            case Number n -> createNumber(n);
            case String s -> createString(s);
            case Collection<?> c -> createList((ArrayList<T>) c.stream()
                    .map(this::create)
                    .collect(Collectors.toCollection(ArrayList::new))
            );
            case Map<?, ?> m -> createMap((LinkedHashMap<String, T>) m
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> create(e.getValue()),
                            (a, b) -> merge(a, b),
                            LinkedHashMap::new
                    ))
            );
            case null -> createNull();
            default -> null;
        };
    }
    T createBoolean(boolean input);
    T createByte(byte input);
    T createShort(short input);
    T createInt(int input);
    T createLong(long input);
    T createFloat(float input);
    T createDouble(double input);
    T createChar(char input);
    T createNumber(Number input);
    T createString(String input);
    T createList(List<? extends T> input);
    T createMap(Map<String, ? extends T> input);
    T createNull();
    T createEnd();
    T createEmpty();
    
    T createEmptyList();
    T createEmptyMap();
    
    Class<T> getCreatorClass();
}
