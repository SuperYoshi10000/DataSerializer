package local.ytk.app.ds;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.save.FileIO;
import local.ytk.app.ds.data.tag.Tag;
import local.ytk.app.ds.transform.DataTransformer;
import org.apache.commons.lang3.ArrayUtils;
import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.databind.JsonNode;

import java.io.File;
import java.util.Objects;

import static org.apache.commons.lang3.ArrayUtils.get;

public class Main {
    public static final String FILE_EXTENSION = ".";
    
    public static final int UNKNOWN_COMMAND = 1;
    public static final int MISSING_ARGUMENT = 2;
    
    public static void main(String... args) {
        if (args.length < 1) {
            help();
            return;
        }
        switch (args[0]) {
            case "help" -> help();
            case "serialize" -> serialize(args);
            case "deserialize" -> deserialize(args);
            case "convert" -> deserialize(args);
            default -> unknown(args[0]);
        }
    }
    
    public static void serialize(String... args) {
        String format = Objects.equals(get(args, 1), "-f") ? get(args, 2) : null;
        String from = format == null ? get(args, 1) : get(args, 3);
        String to = format == null ? get(args, 2) : get(args, 4);
        
        if (from == null) {
            System.err.println("Missing input filename");
            System.exit(MISSING_ARGUMENT);
        }
        if (format == null) format = from.replaceFirst("^.*\\.", ""); // File extension of input file
        if (to == null) to = from.replaceFirst("\\.\\S+$", FILE_EXTENSION);
        
        DataFormat<?, ?, ?> dataFormat = DataFormat.get(format);
        JsonNode json = dataFormat.mapper().readTree(new File(from));
        Tag tag = DataTransformer.JSON_TO_TAG.transform(json);
        FileIO.DEFAULT.serialize(to, tag);
    }
    public static void deserialize(String... args) {
        String format = Objects.equals(get(args, 1), "-f") ? get(args, 2) : null;
        String from = format == null ? get(args, 1) : get(args, 3);
        String to = format == null ? get(args, 2) : get(args, 4);
        
        if (from == null) {
            System.err.println("Missing input filename");
            System.exit(MISSING_ARGUMENT);
        }
        if (format == null) {
            if (to == null) {
                System.err.println("Missing format or output filename");
                System.exit(MISSING_ARGUMENT);
            }
            format = to.replaceFirst("^.*\\.", ""); // File extension of output file
        } else if (to == null) to = from.replaceFirst("\\.\\S+$", FILE_EXTENSION);
        
        DataFormat<?, ?, ?> dataFormat = DataFormat.get(format);
        ByteBuf buf = FileIO.DEFAULT.deserialize(new File(from));
        Tag tag = Tag.deserialize(buf);
        JsonNode json = DataTransformer.TAG_TO_JSON.transform(tag);
        JsonGenerator generator = dataFormat.factory().createGenerator(ObjectWriteContext.empty(), new File(to), JsonEncoding.UTF8);
        dataFormat.mapper().writeTree(generator, json);
    }
    
    public static void convert(String... args) {
        String format1 = get(args, 1);
        String format2 = get(args, 2);
        String from = get(args, 3);
        String to = get(args, 4);
        
        if (from == null) {
            System.err.println("Missing formats or input filename");
            System.exit(MISSING_ARGUMENT);
        }
        
        DataFormat<?, ?, ?> dataFormat1 = DataFormat.get(format1);
        DataFormat<?, ?, ?> dataFormat2 = DataFormat.get(format2);
        JsonNode json = dataFormat1.mapper().readTree(new File(from));
        JsonGenerator generator = dataFormat2.factory().createGenerator(ObjectWriteContext.empty(), new File(to), JsonEncoding.UTF8);
        dataFormat2.mapper().writeTree(generator, json);
    }
    
    public static void help() {
        System.out.println("""
                \u001b[1mData Serialization\u001b[0m
                help
                    Show this help message
                serialize [-f <format>] <from> [<to>]
                    Serialize a file
                deserialize [-f <format>] <from> [<to>]
                    Deserialize a file
                convert <format1> <format2> <from> [<to>]
                    Convert a file from <format1> to <format2>
                """);
    }
    public static void unknown(String msg) {
        System.out.printf("Unknown command: %s\nUse 'help' for help.\n", msg);
        System.exit(UNKNOWN_COMMAND);
    }
}