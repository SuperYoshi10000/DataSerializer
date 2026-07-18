package local.ytk.app.ds;

import tools.jackson.databind.ser.Serializers;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static final String FILE_EXTENSION = ".";
    
    public static void main(String... args) {
        if (args.length < 1) return;
        switch (args[0]) {
            case "help" -> help();
            case "serialize" -> serialize(args);
            case "deserialize" -> deserialize(args);
        }
    }
    
    public static void serialize(String... args) {
        String a = String.join(" ", args);
        Pattern p = Pattern.compile("serialize (.+) (.+)? (-f .+)?");
        Matcher m = p.matcher(a);
        
        String from = m.group(1);
        String to = Objects.requireNonNullElse(m.group(2), from.replaceFirst("\\.\\S+$", FILE_EXTENSION));
        String format = m.group(3);
        
        
    }
    public static void deserialize(String... args) {
        Pattern p = Pattern.compile("deserialize (.+) (.+)? (-f .+)?");
        
    }
    
    public static void help() {
        System.out.println("""
                Data Serialization
                help - Show this help message
                serialize <from> [<to>] [-f <format>] - Serialize a file
                deserialize <from> [<to>] [-f <format>] - Deserialize a file
                """);
    }
    public static void unknown(String msg) {
        System.out.printf("Unknown command: %s\nUse 'help' for help.\n", msg);
    }
}