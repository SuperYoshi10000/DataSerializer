package local.ytk.app.ds.transform;

import local.ytk.app.ds.res.Result;
import local.ytk.app.ds.val.DataValue;

import java.util.List;
import java.util.Map;

public interface DataReader<T> {
    Result<Object> read(T input);
    Result<Boolean> readBoolean(T input);
    Result<Byte> readByte(T input);
    Result<Short> readShort(T input);
    Result<Integer> readInt(T input);
    Result<Long> readLong(T input);
    Result<Float> readFloat(T input);
    Result<Double> readDouble(T input);
    Result<Character> readChar(T input);
    Result<Number> readNumber(T input);
    Result<String> readString(T input);
    Result<List<T>> readList(T input);
    Result<Map<String, T>> readMap(T input);
    
    Class<T> getReaderClass();
}
