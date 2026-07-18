package local.ytk.app.ds;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.save.FileIO;
import local.ytk.app.ds.data.tag.Tag;
import local.ytk.app.ds.transform.DataTransformer;
import local.ytk.app.ds.transform.JsonConverter;
import local.ytk.app.ds.transform.TagConverter;
import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.dataformat.cbor.CBORFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class Main {
    public static final String FILE_EXTENSION = ".";
    
    public static void main(String... args) {
        if (args.length < 1) return;
        switch (args[0]) {
            case "help" -> help();
            case "serialize" -> serialize(args);
            case "deserialize" -> deserialize(args);
            case "convert" -> deserialize(args);
        }
    }
    
    public static void serialize(String... args) {
        String format = Objects.equals(args[1], "-f") ? args[2] : null;
        String from = format == null ? args[1] : args[3];
        String to = format == null ? args[2] : args[4];
        
        if (from == null) {
            System.err.println("Missing input filename");
            System.exit(2);
        }
        if (format == null) format = from.replaceFirst("^.*\\.", ""); // File extension of input file
        if (to == null) to = from.replaceFirst("\\.\\S+$", FILE_EXTENSION);
        
        DataFormat<?, ?, ?> dataFormat = DataFormat.get(format);
        JsonNode json = dataFormat.mapper().readTree(new File(from));
        Tag tag = DataTransformer.JSON_TO_TAG.transform(json);
        FileIO.DEFAULT.serialize(to, tag);
    }
    public static void deserialize(String... args) {
        String format = Objects.equals(args[1], "-f") ? args[2] : null;
        String from = format == null ? args[1] : args[3];
        String to = format == null ? args[2] : args[4];
        
        if (from == null) {
            System.err.println("Missing input filename");
            System.exit(2);
        }
        if (format == null) {
            if (to == null) {
                System.err.println("Missing format or output filename");
                System.exit(3);
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
        String format1 = args[1];
        String format2 = args[2];
        String from = args[3];
        String to = args[4];
        
        DataFormat<?, ?, ?> dataFormat1 = DataFormat.get(format1);
        DataFormat<?, ?, ?> dataFormat2 = DataFormat.get(format2);
        JsonNode json = dataFormat1.mapper().readTree(new File(from));
        JsonGenerator generator = dataFormat2.factory().createGenerator(ObjectWriteContext.empty(), new File(to), JsonEncoding.UTF8);
        dataFormat2.mapper().writeTree(generator, json);
    }
    
    public static void help() {
        System.out.println("""
                \u001b[1mData Serialization\u001b[0m
                help - Show this help message
                serialize [-f <format>] <from> [<to>] - Serialize a file
                deserialize [-f <format>] <from> [<to>] - Deserialize a file
                deserialize <format1> <format2> <from> [<to>] - Deserialize a file
                """);
    }
    public static void unknown(String msg) {
        System.out.printf("Unknown command: %s\nUse 'help' for help.\n", msg);
        System.exit(1);
    }
}