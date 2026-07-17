package local.ytk.app.ds.val.p;

public interface CharacterValue extends PrimitiveValue<Character> {
    char getChar();
    
    @Override
    default Character get() {
        return getChar();
    }
}
