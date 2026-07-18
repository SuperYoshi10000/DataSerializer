package local.ytk.app.ds.data.tag;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.val.p.num.BigIntValue;
import local.ytk.app.ds.val.p.num.DoubleValue;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.TreeMap;

public class BigIntTag extends NumericTag<BigInteger, BigIntTag> implements ObjectTag<BigInteger, BigIntTag>, BigIntValue {
    public static final byte TYPE = 6;
    public static final BigIntTag ZERO = of(0);
    public static final BigIntTag ONE = of(1);
    public static final BigIntTag NEGATIVE_ONE = of(-1);
    
    private final BigInteger value;
    BigIntTag(BigInteger value) {
        this.value = value;
    }
    public BigInteger getValue() {
        return value;
    }
    
    @Override
    public double doubleValue() {
        return value.doubleValue();
    }
    
    @Override
    public long longValue() {
        return value.longValue();
    }
    
    @SuppressWarnings("all")
    public static BigIntTag of(BigInteger value) {
        return new BigIntTag(value);
    }
    public static BigIntTag of(@NotNull String value) {
        return of(Double.parseDouble(value));
    }
    public static BigIntTag of(@NotNull Number n) {
        return of(BigInteger.valueOf(n.longValue()));
    }
    public static BigIntTag of(@NotNull Object o) {
        return of(Double.parseDouble(o.toString()));
    }
    public static BigIntTag deserialize(@NotNull ByteBuf buffer) {
        return of(buffer.readDouble());
    }
    
    @Override
    public BigInteger objectValue() {
        return value;
    }
    @Override
    public boolean serialize(ByteBuf buffer) {
        byte[] bytes = value.toByteArray();
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        return true;
    }
    
    @Override
    public BigInteger get() {
        return value;
    }
}
