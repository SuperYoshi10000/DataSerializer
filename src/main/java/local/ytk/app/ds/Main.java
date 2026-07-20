package local.ytk.app.ds;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import local.ytk.app.ds.data.save.FileIO;
import local.ytk.app.ds.data.save.Serializer;
import local.ytk.app.ds.data.tag.Tag;
import local.ytk.app.ds.transform.AbstractDataTransformer;
import org.apache.commons.cli.*;
import org.apache.commons.cli.help.HelpFormatter;
import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.databind.JsonNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Main {
    public static final String FILE_EXTENSION_TEXT = ".ysfs";
    public static final String FILE_EXTENSION_BINARY = ".ysfb";
    public static final String FILE_EXTENSION_COMPRESSED = ".ysfc";
    
    public static final int UNKNOWN_COMMAND = 1;
    public static final int MISSING_ARGUMENT = 2;
    
    public static final Options CLI_OPTIONS = new Options()
            .addOption("c", "compress", false, "Use compression when serializing")
            .addOption("d", "debug", false, "Log additional debug information")
            .addOption("f", "format", true, "Specify the input/output format")
            .addOption("h", "help", false, "Show this help message");
    
    public static final int MAGIC_NUMBER = 0x8059544B;
    public static final int VERSION = 1;
    
    public static void main(String... args) {
        if (args.length < 1) {
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
            return;
        }
        
        String[] runArgs;
        if (Objects.equals(args[0], "--input-args")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.print("ARGS>>> ");
                runArgs = reader.readLine().split(" ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else runArgs = args;
        
        
        CommandLineParser parser = new DefaultParser(false);
        try {
            CommandLine cmd = parser.parse(CLI_OPTIONS, runArgs);
            run(cmd);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public static void run(CommandLine cmd) {
        if (cmd.hasOption("h")) help();
        else switch (cmd.getArgList().getFirst()) {
            case "help" -> help();
            case "serialize" -> serialize(cmd);
            case "deserialize" -> deserialize(cmd);
            case "convert" -> convert(cmd);
            default -> unknown(cmd.getArgList().getFirst());
        }
    }
    
    public static void serialize(CommandLine cmd) {
        boolean debug = cmd.hasOption("d");
        boolean compress = cmd.hasOption("c");
        List<String> args = cmd.getArgList();
        String from = args.get(1);
        if (from == null) {
            System.err.println("Missing input filename");
            System.exit(MISSING_ARGUMENT);
        }
        String to = Objects.requireNonNullElse(args.get(2), from.replaceFirst("\\.\\S+$", compress ? FILE_EXTENSION_COMPRESSED : FILE_EXTENSION_BINARY));
        String format = Objects.requireNonNullElse(cmd.getOptionValue("f"), from.replaceFirst("^.*\\.", ""));
        
        if (debug) System.out.println("From: " + from + ", To: " + to + ", Format: " + format);
        
        DataFormat<?, ?, ?> dataFormat = DataFormat.get(format);
        JsonNode json = dataFormat.mapper().readTree(new File(from));
        Tag tag = AbstractDataTransformer.JSON_TO_TAG.transform(json);
        if (debug) System.out.println("Read input file");
        
        
        Serializer<File, Tag> serializer = FileIO.DEFAULT.<Tag>before((b, t) -> {
            b.writeByte(t.getId());
            t.serialize(b);
        }).after((buf, t) -> {
            if (!compress) return buf;
            return compressSerialized(buf);
        });
        Serializer.serialize(serializer, to, tag);
        if (debug) System.out.println("Wrote output file");
    }
    public static ByteBuf compressSerialized(ByteBuf buf) {
        ByteBuf output = Unpooled.buffer();
        output.writeInt(MAGIC_NUMBER);
        output.writeInt(VERSION);
        try (Deflater deflater = new Deflater()) {
            deflater.setInput(buf.nioBuffer());
            deflater.finish();
            while (!deflater.finished()) {
                byte[] bytes = new byte[256];
                int written = deflater.deflate(bytes);
                output.writeBytes(bytes, 0, written);
            }
            return output;
        }
    }
    
    public static void deserialize(CommandLine cmd) {
        boolean debug = cmd.hasOption("d");
        String format = cmd.getOptionValue("f");
        List<String> args = cmd.getArgList();
        String from = args.get(1);
        String to = args.get(2);
        
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
        } else if (to == null) to = from.replaceFirst("\\.\\S+$", format);
        
        if (debug) System.out.println("From: " + from + ", To: " + to + ", Format: " + format);
        
        DataFormat<?, ?, ?> dataFormat = DataFormat.get(format);
        ByteBuf fileBuf = FileIO.DEFAULT.deserialize(new File(from));
        
        ByteBuf buf;
        
        if (fileBuf.getInt(0) == MAGIC_NUMBER) buf = decompressSerialized(fileBuf);
        else buf = fileBuf;
        
        Tag tag = Tag.deserialize(buf);
        if (debug) System.out.println("Read input file (" + buf.writerIndex() + " bytes)");
        JsonNode json = AbstractDataTransformer.TAG_TO_JSON.transform(tag);
        JsonGenerator generator = dataFormat.factory().createGenerator(ObjectWriteContext.empty(), new File(to), JsonEncoding.UTF8);
        dataFormat.mapper().writeTree(generator, json);
        if (debug) System.out.println("Wrote output file");
    }
    public static ByteBuf decompressSerialized(ByteBuf buf) {
        buf.readInt();
        if (buf.readInt() > VERSION) throw new IllegalStateException("Invalid version " + buf.getInt(4) + " (Max supported " + VERSION + ")");
        ByteBuf output = Unpooled.buffer();
        try (Inflater inflater = new Inflater()) {
            inflater.setInput(buf.nioBuffer());
            while (!inflater.finished()) {
                byte[] bytes = new byte[256];
                int written = inflater.inflate(bytes);
                output.writeBytes(bytes, 0, written);
            }
            return output;
        } catch (DataFormatException e) {
            throw new RuntimeException("Failed after " + output.writerIndex() + " bytes", e);
        }
    }
    
    public static void convert(CommandLine cmd) {
        boolean debug = cmd.hasOption("d");
        
        List<String> args = cmd.getArgList();
        String format1 = args.get(1);
        String format2 = args.get(2);
        String from = args.get(3);
        String to = args.get(4);
        
        if (from == null) {
            System.err.println("Missing formats or input filename");
            System.exit(MISSING_ARGUMENT);
        }
        if (debug) System.out.println("From: " + from + ", To: " + to + ", From Format: " + format1 + ", To Format: " + format2);
        
        DataFormat<?, ?, ?> dataFormat1 = DataFormat.get(format1);
        DataFormat<?, ?, ?> dataFormat2 = DataFormat.get(format2);
        JsonNode json = dataFormat1.mapper().readTree(new File(from));
        if (debug) System.out.println("Read input file");
        JsonGenerator generator = dataFormat2.factory().createGenerator(ObjectWriteContext.empty(), new File(to), JsonEncoding.UTF8);
        dataFormat2.mapper().writeTree(generator, json);
        if (debug) System.out.println("Wrote output file");
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
        
        HelpFormatter formatter = HelpFormatter.builder().get();
        try {
            formatter.printHelp(
                    "DataSerializer [options] <command> [<args>]",
                    "\u001b[1mData Serialization\u001b[0m",
                    CLI_OPTIONS,
                    null,
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void unknown(String msg) {
        System.out.printf("Unknown command: %s\nUse 'help' for help.\n", msg);
        System.exit(UNKNOWN_COMMAND);
    }
}