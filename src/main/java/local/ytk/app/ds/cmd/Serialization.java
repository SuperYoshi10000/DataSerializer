package local.ytk.app.ds.cmd;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.cbor.CBORGenerator;
import tools.jackson.dataformat.cbor.CBORParser;

import java.io.File;

public class Serialization {
    public static void read(File f, ObjectMapper mapper) {
        mapper.readTree(f);
    }
}
