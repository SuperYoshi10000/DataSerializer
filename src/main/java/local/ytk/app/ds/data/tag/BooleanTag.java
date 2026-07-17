package local.ytk.app.ds.data.tag;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.val.p.BooleanValue;
import local.ytk.app.ds.val.p.num.ByteValue;
import local.ytk.util.math.ArrayRange;
import org.jetbrains.annotations.NotNull;

public class BooleanTag implements BooleanValue, TypedTag<Boolean, BooleanTag> {
    public static final byte TYPE = 1;
    
    private final boolean value;
    BooleanTag(boolean value) {
        this.value = value;
    }
    public boolean getValue() {
        return value;
    }

    public static BooleanTag of(boolean value) {
        return value ? TRUE : FALSE;
    }
    public static BooleanTag of(@NotNull String s) {
        return s.equals("true") ? TRUE : FALSE;
    }
    public static BooleanTag deserialize(@NotNull ByteBuf buffer) {
        return of(buffer.readByte() != 0);
    }
    
    @Override
    public String toTagString() {
        return "";
    }
    
    public static final BooleanTag FALSE = new BooleanTag(false);
    public static final BooleanTag TRUE  = new BooleanTag(true);

    public byte getId() {
        return TYPE;
    }
    
    @Override
    public Boolean objectValue() {
        return value;
    }
    @Override
    public boolean serialize(ByteBuf buffer) {
        buffer.writeByte(value ? 1 : 0);
        return true;
    }
    
    public byte getByte() {
        return (byte) (value ? 1 : 0);
    }
    @Override
    public boolean getBoolean() {
        return value;
    }
    
    public ByteTag toByteTag() {
        return value ? ByteTag.TRUE : ByteTag.FALSE;
    }
}
