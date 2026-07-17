package local.ytk.app.ds.transform;

import local.ytk.app.ds.res.Result;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.*;

public class JavaObjectConverter implements DataConverter<Object> {
    @Override
    public Object createBoolean(boolean input) {
        return input;
    }
    
    @Override
    public Object createByte(byte input) {
        return input;
    }
    
    @Override
    public Object createShort(short input) {
        return input;
    }
    
    @Override
    public Object createInt(int input) {
        return input;
    }
    
    @Override
    public Object createLong(long input) {
        return input;
    }
    
    @Override
    public Object createFloat(float input) {
        return input;
    }
    
    @Override
    public Object createDouble(double input) {
        return input;
    }
    
    @Override
    public Object createChar(char input) {
        return input;
    }
    
    @Override
    public Object createNumber(Number input) {
        return input;
    }
    
    @Override
    public Object createString(String input) {
        return input;
    }
    
    @Override
    public Object createList(List<?> input) {
        return input;
    }
    
    @Override
    public Object createMap(Map<String, ?> input) {
        return input;
    }
    
    @Override
    public Object createNull() {
        return null;
    }
    
    @Override
    public Object createEnd() {
        return null;
    }
    
    @Override
    public Object createEmpty() {
        return null;
    }
    
    @Override
    public Object createEmptyList() {
        return new ArrayList<>();
    }
    
    @Override
    public Object createEmptyMap() {
        return new LinkedHashMap<>();
    }
    
    @Override
    public Class<Object> getCreatorClass() {
        return Object.class;
    }
    
    @Override
    public Object get(Object input, String key) {
        try {
            return Object.class.getDeclaredField(key).get(input);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
    
    @Override
    public Object get(Object input, int index) {
        if (input.getClass().isArray()) {
            return Array.get(input, index);
        }
        return null;
    }
    
    @Override
    public Object set(Object input, String key, Object value) {
        try {
            Object.class.getDeclaredField(key).set(key, value);
            return input;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    @Override
    public Object set(Object input, int index, Object value) {
        if (input instanceof List<?> list) {
            ((List<Object>) list).set(index, value);
        }
        Array.set(input, index, value);
        return input;
    }
    
    @Override
    public Object insert(Object input, int index, Object value) {
        if (!input.getClass().isArray()) throw new IllegalArgumentException("Input must be array");
        return switch (input) {
            case byte[] a -> ArrayUtils.insert(index, a, (byte) value);
            case short[] a -> ArrayUtils.insert(index, a, (short) value);
            case int[] a -> ArrayUtils.insert(index, a, (int) value);
            case long[] a -> ArrayUtils.insert(index, a, (long) value);
            case float[] a -> ArrayUtils.insert(index, a, (float) value);
            case double[] a -> ArrayUtils.insert(index, a, (double) value);
            case char[] a -> ArrayUtils.insert(index, a, (char) value);
            case String[] a -> ArrayUtils.insert(index, a, value == null ? null : value.toString());
            case Object[] a -> ArrayUtils.insert(index, a, value);
            default -> insertObject(input, index, value);
        };
    }
    
    private @NotNull Object insertObject(Object input, int index, Object value) {
        int length = Array.getLength(input);
        if (index >= 0 && index <= length) {
            Class<?> type = input.getClass().getComponentType();
            Object result = Array.newInstance(type, length + 1);
            Array.set(result, index, value);
            if (index > 0) {
                System.arraycopy(input, 0, result, 0, index);
            }
            if (index < length) {
                System.arraycopy(input, index, result, index + 1, length - index);
            }
            return result;
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
    }
    
    @Override
    public Object add(Object input, Object value) {
        return insert(input, Array.getLength(input), value);
    }
    
    @Override
    public Object with(Object input, String key, Object value) {
        return set(ObjectUtils.clone(input), key, value);
    }
    
    @Override
    public Object merge(Object first, Object second) {
        if (first.getClass().isArray()) return mergeArray(first, second);
        return switch (first) {
            case String n -> n + second.toString();
            case Collection<?> _ -> mergeList(first, second);
            case Map<?, ?> _ -> mergeMap(first, second);
            case Number n when second instanceof Number m -> n.doubleValue() + m.doubleValue();
            default -> first;
        };
    }
    
    
    @Override
    public Object addAll(Object first, Object second) {
        if (first.getClass().isArray()) return mergeArray(first, second);
        return switch (first) {
            case Collection<?> _ -> addAllList(first, second);
            case Map<?, ?> _ -> addAllMap(first, second);
            default -> first;
        };
    }
    
    @Override
    public Object copy(Object input) {
        return ObjectUtils.clone(input);
    }
    
    @Override
    public Class<Object> getOperatorClass() {
        return Object.class;
    }
    
    @Override
    public Result<Object> read(Object input) {
        return Result.success(input);
    }
    
    @Override
    public Result<Boolean> readBoolean(Object input) {
        return input instanceof Boolean b ? Result.success(b) : Result.failure();
    }
    
    @Override
    public Result<Byte> readByte(Object input) {
        return input instanceof Number n ? Result.success(n.byteValue()) : Result.failure();
    }
    
    @Override
    public Result<Short> readShort(Object input) {
        return input instanceof Number n ? Result.success(n.shortValue()) : Result.failure();
    }
    
    @Override
    public Result<Integer> readInt(Object input) {
        return input instanceof Number n ? Result.success(n.intValue()) : Result.failure();
    }
    
    @Override
    public Result<Long> readLong(Object input) {
        return input instanceof Number n ? Result.success(n.longValue()) : Result.failure();
    }
    
    @Override
    public Result<Float> readFloat(Object input) {
        return input instanceof Number n ? Result.success(n.floatValue()) : Result.failure();
    }
    
    @Override
    public Result<Double> readDouble(Object input) {
        return input instanceof Number n ? Result.success(n.doubleValue()) : Result.failure();
    }
    
    @Override
    public Result<Character> readChar(Object input) {
        return input instanceof Character c ? Result.success(c) :
                input instanceof CharSequence s && s.length() == 1 ? Result.success(s.charAt(0)) : Result.failure();
    }
    
    @Override
    public Result<Number> readNumber(Object input) {
        return input instanceof Number n ? Result.success(n) : Result.failure();
    }
    
    @Override
    public Result<String> readString(Object input) {
        return input instanceof CharSequence s ? Result.success(s.toString()) : Result.failure();
    }
    
    @Override
    public Result<List<Object>> readList(Object input) {
        return input instanceof List<?> list ? Result.success((List<Object>) list) :
                input instanceof Collection<?> collection ? Result.success(new ArrayList<>(collection))
                : input.getClass().isArray() ? Result.success(Arrays.asList(
                        input.getClass().isPrimitive() ? getAsObjectArray(input) : (Object[]) input
                )) : Result.failure();
    }
    
    private Object[] getAsObjectArray(Object input) {
        return switch (input) {
            case byte[] a -> ArrayUtils.toObject(a);
            case short[] a -> ArrayUtils.toObject(a);
            case int[] a -> ArrayUtils.toObject(a);
            case long[] a -> ArrayUtils.toObject(a);
            case float[] a -> ArrayUtils.toObject(a);
            case double[] a -> ArrayUtils.toObject(a);
            case char[] a -> ArrayUtils.toObject(a);
            case String[] a -> a;
            case Object[] a -> a;
            default -> throw new IllegalArgumentException("Input must be array");
        };
    }
    
    @Override
    public Result<Map<String, Object>> readMap(Object input) {
        return input instanceof Map<?, ?> map ? Result.success((Map<String, Object>) map) : Result.failure();
    }
    
    @Override
    public Class<Object> getReaderClass() {
        return Object.class;
    }
    
    @Override
    public Object addAllList(Object first, Object second) {
        if (first.getClass().isArray()) return mergeArray(first, second);
        if (first instanceof Collection<?> c1 && second instanceof Collection<?> c2) {
            ((Collection<Object>) c1).addAll(c2);
            return c1;
        }
        return null;
    }
    
    @Override
    public Object addAllMap(Object first, Object second) {
        if (first instanceof Map<?, ?> m1 && second instanceof Map<?, ?> m2) {
            ((Map<Object, Object>) m1).putAll(m2);
            return m1;
        }
        return null;
    }
    
    @Override
    public Object create(Object input) {
        return input;
    }
    
    @Override
    public Object mergeList(Object first, Object second) {
        if (first.getClass().isArray()) return mergeArray(first, second);
        if (first instanceof Collection<?> c1 && second instanceof Collection<?> c2) {
            List<Object> list = new ArrayList<>(c1);
            list.addAll(c2);
            return list;
        }
        return null;
    }
    
    @Nullable
    public Object mergeArray(Object first, Object second) {
        if (!first.getClass().isArray()) return null;
        int firstLength = Array.getLength(first);
        int secondLength = Array.getLength(second);
        Class<?> firstType = first.getClass().getComponentType();
        Class<?> secondType = second.getClass().getComponentType();
        Class<?> newType;
        if (firstType.isAssignableFrom(secondType)) newType = firstType;
        else if (secondType.isAssignableFrom(firstType)) newType = secondType;
        else newType = Object.class;
        Object newArray = Array.newInstance(newType, firstLength + secondLength);
        System.arraycopy(first, 0, newArray, 0, firstLength);
        System.arraycopy(second, 0, newArray, firstLength, secondLength);
        return newArray;
    }
    
    @Override
    public Object mergeMap(Object first, Object second) {
        if (first instanceof Map<?, ?> m1 && second instanceof Map<?, ?> m2) {
            Map<Object, Object> map = new LinkedHashMap<>(m1);
            map.putAll(m2);
            return map;
        }
        return null;
    }
}
