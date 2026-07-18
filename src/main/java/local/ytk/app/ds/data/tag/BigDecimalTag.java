package local.ytk.app.ds.data.tag;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.val.p.num.BigDecimalValue;
import local.ytk.app.ds.val.p.num.BigIntValue;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.TreeMap;

public class BigDecimalTag extends NumericTag<BigDecimal, BigDecimalTag> implements ObjectTag<BigDecimal, BigDecimalTag>, BigDecimalValue {
    public static final byte TYPE = 6;
    public static final BigDecimalTag ZERO = of(0);
    public static final BigDecimalTag ONE = of(1);
    public static final BigDecimalTag NEGATIVE_ONE = of(-1);
    
    private final BigDecimal value;
    BigDecimalTag(BigDecimal value) {
        this.value = value;
    }
    public BigDecimal getValue() {
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
    public static BigDecimalTag of(BigDecimal value) {
        return new BigDecimalTag(value);
    }
    public static BigDecimalTag of(@NotNull String value) {
        return of(Double.parseDouble(value));
    }
    public static BigDecimalTag of(@NotNull Number n) {
        return of(BigDecimal.valueOf(n.longValue()));
    }
    public static BigDecimalTag of(@NotNull Object o) {
        return of(Double.parseDouble(o.toString()));
    }
    public static BigDecimalTag deserialize(@NotNull ByteBuf buffer) {
        return of(buffer.readDouble());
    }
    
    @Override
    public BigDecimal objectValue() {
        return value;
    }
    @Override
    public boolean serialize(ByteBuf buffer) {
        byte[] bytes = value.unscaledValue().toByteArray();
        int scale = value.scale();
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        buffer.writeInt(scale);
        return true;
    }
    
    @Override
    public BigDecimal get() {
        return value;
    }
}
