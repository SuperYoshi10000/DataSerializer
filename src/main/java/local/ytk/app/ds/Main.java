package local.ytk.app.ds;

import io.netty.buffer.ByteBuf;
import local.ytk.app.ds.data.save.FileIO;
import local.ytk.app.ds.data.tag.Tag;
import local.ytk.app.ds.transform.AbstractDataTransformer;
import org.apache.commons.cli.*;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.commons.lang3.ArrayUtils;
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

import static org.apache.commons.lang3.ArrayUtils.get;

public class Main {
    public static final String FILE_EXTENSION = ".";
    
    public static final int UNKNOWN_COMMAND = 1;
    public static final int MISSING_ARGUMENT = 2;
    
    public static final Options CLI_OPTIONS = new Options()
            .addOption("c", "compress", false, "Use compression when serializing")
            .addOption("d", "debug", false, "Log additional debug information")
            .addOption("f", "format", true, "Specify the input/output format")
            .addOption("h", "help", false, "Show this help message");
    
    public static void main(String... args) {
        if (args.length < 1) {
            basicHelp();
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
        switch (cmd.getArgList().getFirst()) {
            case "help" -> help(cmd);
            case "serialize" -> serialize(cmd);
            case "deserialize" -> deserialize(cmd);
            case "convert" -> convert(cmd);
            default -> unknown(cmd.getArgList().getFirst());
        }
    }
    
    public static void serialize(CommandLine cmd) {
        boolean debug = cmd.hasOption("d");
        String format = cmd.getOptionValue("f");
        List<String> args = cmd.getArgList();
        String from = args.get(1);
        String to = args.get(2);
        
        if (from == null) {
            System.err.println("Missing input filename");
            System.exit(MISSING_ARGUMENT);
        }
        if (format == null) format = from.replaceFirst("^.*\\.", ""); // File extension of input file
        if (to == null) to = from.replaceFirst("\\.\\S+$", FILE_EXTENSION);
        
        if (debug) System.out.println("From: " + from + ", To: " + to + ", Format: " + format);
        
        DataFormat<?, ?, ?> dataFormat = DataFormat.get(format);
        JsonNode json = dataFormat.mapper().readTree(new File(from));
        Tag tag = AbstractDataTransformer.JSON_TO_TAG.transform(json);
        if (debug) System.out.println("Read input file");
        FileIO.DEFAULT.<Tag>onInit((b, t) -> {
            b.writeByte(t.getId());
            t.serialize(b);
        }).serialize(to, tag);
        if (debug) System.out.println("Wrote output file");
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
        } else if (to == null) to = from.replaceFirst("\\.\\S+$", FILE_EXTENSION);
        
        if (debug) System.out.println("From: " + from + ", To: " + to + ", Format: " + format);
        
        DataFormat<?, ?, ?> dataFormat = DataFormat.get(format);
        ByteBuf buf = FileIO.DEFAULT.deserialize(new File(from));
        Tag tag = Tag.deserialize(buf);
        if (debug) System.out.println("Read input file (" + buf.writerIndex() + " bytes)");
        JsonNode json = AbstractDataTransformer.TAG_TO_JSON.transform(tag);
        JsonGenerator generator = dataFormat.factory().createGenerator(ObjectWriteContext.empty(), new File(to), JsonEncoding.UTF8);
        dataFormat.mapper().writeTree(generator, json);
        if (debug) System.out.println("Wrote output file");
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
    
    public static void basicHelp() {
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
    public static void help(CommandLine cmd) {
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