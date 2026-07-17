package local.ytk.app.ds.transform;

import local.ytk.app.ds.res.Result;

import java.util.List;
import java.util.Map;

public interface DataStructure<T> extends DataConverter<T> {
    T value();
    
    record Impl<T, C extends DataConverter<T>>(T value, C converter) implements DataStructure<T> {
        @Override
        public T createBoolean(boolean input) {
            return converter.createBoolean(input);
        }
        
        @Override
        public T createByte(byte input) {
            return converter.createByte(input);
        }
        
        @Override
        public T createShort(short input) {
            return converter.createShort(input);
        }
        
        @Override
        public T createInt(int input) {
            return converter.createInt(input);
        }
        
        @Override
        public T createLong(long input) {
            return converter.createLong(input);
        }
        
        @Override
        public T createFloat(float input) {
            return converter.createFloat(input);
        }
        
        @Override
        public T createDouble(double input) {
            return converter.createDouble(input);
        }
        
        @Override
        public T createChar(char input) {
            return converter.createChar(input);
        }
        
        @Override
        public T createNumber(Number input) {
            return converter.createNumber(input);
        }
        
        @Override
        public T createString(String input) {
            return converter.createString(input);
        }
        
        @Override
        public T createList(List<? extends T> input) {
            return converter.createList(input);
        }
        
        @Override
        public T createMap(Map<String, ? extends T> input) {
            return converter.createMap(input);
        }
        
        @Override
        public T createNull() {
            return converter.createNull();
        }
        
        @Override
        public T createEnd() {
            return converter.createEnd();
        }
        
        @Override
        public T createEmpty() {
            return converter.createEmpty();
        }
        
        @Override
        public T createEmptyList() {
            return converter.createEmptyList();
        }
        
        @Override
        public T createEmptyMap() {
            return converter.createEmptyMap();
        }
        
        @Override
        public Class<T> getCreatorClass() {
            return converter.getCreatorClass();
        }
        
        @Override
        public T get(T input, String key) {
            return converter.get(input, key);
        }
        
        @Override
        public T get(T input, int index) {
            return converter.get(input, index);
        }
        
        @Override
        public T set(T input, String key, T value) {
            return converter.set(input, key, value);
        }
        
        @Override
        public T set(T input, int index, T value) {
            return converter.set(input, index, value);
        }
        
        @Override
        public T insert(T input, int index, T value) {
            return converter.insert(input, index, value);
        }
        
        @Override
        public T add(T input, T value) {
            return converter.add(input, value);
        }
        
        @Override
        public T with(T input, String key, T value) {
            return converter.with(input, key, value);
        }
        
        @Override
        public T merge(T first, T second) {
            return converter.merge(first, second);
        }
        
        @Override
        public T addAll(T first, T second) {
            return converter.addAll(first, second);
        }
        
        @Override
        public T copy(T input) {
            return converter.copy(input);
        }
        
        @Override
        public Class<T> getOperatorClass() {
            return converter.getOperatorClass();
        }
        
        @Override
        public Result<Object> read(T input) {
            return converter.read(input);
        }
        
        @Override
        public Result<Boolean> readBoolean(T input) {
            return converter.readBoolean(input);
        }
        
        @Override
        public Result<Byte> readByte(T input) {
            return converter.readByte(input);
        }
        
        @Override
        public Result<Short> readShort(T input) {
            return converter.readShort(input);
        }
        
        @Override
        public Result<Integer> readInt(T input) {
            return converter.readInt(input);
        }
        
        @Override
        public Result<Long> readLong(T input) {
            return converter.readLong(input);
        }
        
        @Override
        public Result<Float> readFloat(T input) {
            return converter.readFloat(input);
        }
        
        @Override
        public Result<Double> readDouble(T input) {
            return converter.readDouble(input);
        }
        
        @Override
        public Result<Character> readChar(T input) {
            return converter.readChar(input);
        }
        
        @Override
        public Result<Number> readNumber(T input) {
            return converter.readNumber(input);
        }
        
        @Override
        public Result<String> readString(T input) {
            return converter.readString(input);
        }
        
        @Override
        public Result<List<T>> readList(T input) {
            return converter.readList(input);
        }
        
        @Override
        public Result<Map<String, T>> readMap(T input) {
            return converter.readMap(input);
        }
        
        @Override
        public Class<T> getReaderClass() {
            return converter.getReaderClass();
        }
    }
}
