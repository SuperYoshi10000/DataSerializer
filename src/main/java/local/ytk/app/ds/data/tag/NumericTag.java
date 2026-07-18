package local.ytk.app.ds.data.tag;

import local.ytk.app.ds.val.p.num.NumericValue;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public abstract class NumericTag<N extends Number, T extends NumericTag<N, T>> extends Number implements TypedTag<N, T>, Comparable<NumericTag<?, ?>>, NumericValue<N> {
    @Override
    public int intValue() {
        return (int) doubleValue();
    }
    
    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }
    
    @Override
    public abstract N objectValue();
    
    @Override
    public int compareTo(@NotNull NumericTag<?, ?> tag) {
        double difference = compare(this, tag);
        if (difference > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (difference < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        if (difference > 0 && difference < 1) return 1;
        if (difference < 0 && difference > -1) return -1;
        return (int) difference;
    }
    
    public byte sign() {
        return (byte)(doubleValue() == 0 ? 0 : doubleValue() > 0 ? 1 : -1);
    }
    public boolean isPositive() {
        return doubleValue() > 0;
    }
    public boolean isNegative() {
        return doubleValue() < 0;
    }
    public boolean isZero() {
        return doubleValue() == 0;
    }
    public boolean isInteger() {
        return doubleValue() == longValue();
    }
    public boolean isDecimal() {
        return doubleValue() != longValue();
    }

    public static double compare(NumericTag<?, ?> a, NumericTag<?, ?> b) {
        return a.doubleValue() - b.doubleValue();
    }
    public static double compareInt(NumericTag<?, ?> a, NumericTag<?, ?> b) {
        return a.longValue() - b.longValue();
    }

    public boolean equals(Object value) {
        return value instanceof NumericTag<?, ?> tag && getClass() == tag.getClass() && doubleValue() == tag.doubleValue();
    }
    public String toString() {
        return isInteger() ? Long.toString(longValue()) : Double.toString(doubleValue());
    }
    public String toTagString() {
        return TAG_CODES[getId()];
    }
    
    @Override
    public int size() {
        return switch (objectValue()) {
            case Byte _ -> 1;
            case Short _ -> 2;
            case Integer _, Float _ -> 4;
            case Long _, Double _ -> 8;
            default -> TypedTag.super.size();
        };
    }
}
