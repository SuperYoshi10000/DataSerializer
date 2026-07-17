package local.ytk.app.ds.data.tag;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.val.p.CharacterValue;
import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

public class CharTag implements TypedTag<Character, CharTag>, CharacterValue {
    public static final byte TYPE = 8;
    public static final CharTag NULL = of('\0');
    public static final CharTag SPACE = of(' ');
    
    private final char value;
    CharTag(char value) {
        this.value = value;
    }
    public char getValue() {
        return value;
    }

    @SuppressWarnings("all")
    public static CharTag of(char value) {
        if (cache.containsKey(value)) return cache.get(value);
        if (cache.size() > 4096) {
            if (value > 0) cache.remove(cache.lastKey());
            else cache.remove(cache.firstKey());
        }
        CharTag result = new CharTag(value);
        cache.put(value, result);
        return result;
    }
    public static CharTag of(short c) {
        return of((char) c);
    }
    public static CharTag of(double c) {
        return of((char) c);
    }
    public static CharTag of(Number c) {
        return of((char) c.shortValue());
    }
    public static CharTag of(@NotNull String s) {
        return of(Character.codePointAt(s, 0));
    }
    public static CharTag of(@NotNull Object o) {
        return o instanceof Character c ? of(c) : o instanceof Number n ? of(n) :
                o instanceof CharSequence s ? of(s.toString()) : of(o.getClass().getSimpleName().charAt(0));
    }
    public static CharTag deserialize(@NotNull ByteBuf buffer) {
        return of(buffer.readChar());
    }

    private static final TreeMap<Character, CharTag> cache = new TreeMap<>();

    public byte getId() {
        return TYPE;
    }
    @Override
    public Character objectValue() {
        return value;
    }
    @Override
    public boolean serialize(ByteBuf buffer) {
        buffer.writeChar(value);
        return true;
    }
    
    @Override
    public char getChar() {
        return value;
    }
    
    public String toTagString() {
        return "'" + StringTag.encodeEscapes(String.valueOf(value)) + "'";
    }
    
    public StringTag toStringTag() {
        return StringTag.of(value);
    }
    public ShortTag toShortTag() {
        return ShortTag.of(value);
    }
}
