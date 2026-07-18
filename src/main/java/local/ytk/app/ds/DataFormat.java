package local.ytk.app.ds;


import tools.jackson.core.TokenStreamFactory;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.avro.AvroFactory;
import tools.jackson.dataformat.avro.AvroMapper;
import tools.jackson.dataformat.cbor.CBORFactory;
import tools.jackson.dataformat.cbor.CBORMapper;
import tools.jackson.dataformat.csv.CsvFactory;
import tools.jackson.dataformat.csv.CsvMapper;
import tools.jackson.dataformat.ion.IonFactory;
import tools.jackson.dataformat.ion.IonObjectMapper;
import tools.jackson.dataformat.javaprop.JavaPropsFactory;
import tools.jackson.dataformat.javaprop.JavaPropsMapper;
import tools.jackson.dataformat.protobuf.ProtobufFactory;
import tools.jackson.dataformat.protobuf.ProtobufMapper;
import tools.jackson.dataformat.smile.SmileFactory;
import tools.jackson.dataformat.smile.SmileMapper;
import tools.jackson.dataformat.toml.TomlFactory;
import tools.jackson.dataformat.toml.TomlMapper;
import tools.jackson.dataformat.xml.XmlFactory;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataFormat<M extends ObjectMapper, F extends TokenStreamFactory, B extends MapperBuilder<M, B>> {
//    public static Bson bson() {
//        return new XmlMapper();
//    }

//    public static MessagePackMapper msgpack() {
//        return new MessagePackMapper();
//    }
    
    private static final HashMap<String, DataFormat<?, ?, ?>> FORMAT_BY_NAME = new HashMap<>();
    
    public static final DataFormat<JsonMapper, JsonFactory, JsonMapper.Builder> JSON = new DataFormat<>("json", new JsonFactory(), JsonMapper::new, JsonMapper::new, JsonMapper::builder, JsonMapper::builder).register();
    public static final DataFormat<YAMLMapper, YAMLFactory, YAMLMapper.Builder> YAML = new DataFormat<>("yaml", new YAMLFactory(), YAMLMapper::new, YAMLMapper::new, YAMLMapper::builder, YAMLMapper::builder).register("yml");
    public static final DataFormat<XmlMapper, XmlFactory, XmlMapper.Builder> XML = new DataFormat<>("xml", new XmlFactory(), XmlMapper::new, XmlMapper::new, XmlMapper::builder, XmlMapper::builder).register();
    public static final DataFormat<CBORMapper, CBORFactory, CBORMapper.Builder> CBOR = new DataFormat<>("cbor", new CBORFactory(), CBORMapper::new, CBORMapper::new, CBORMapper::builder, CBORMapper::builder).register();
    public static final DataFormat<SmileMapper, SmileFactory, SmileMapper.Builder> SMILE = new DataFormat<>("smile", new SmileFactory(), SmileMapper::new, SmileMapper::new, SmileMapper::builder, SmileMapper::builder).register();
    public static final DataFormat<JavaPropsMapper, JavaPropsFactory, JavaPropsMapper.Builder> PROPERTIES = new DataFormat<>("properties", new JavaPropsFactory(), JavaPropsMapper::new, JavaPropsMapper::new, JavaPropsMapper::builder, JavaPropsMapper::builder).register("javaprops", "props", "javaproperties");
    public static final DataFormat<CsvMapper, CsvFactory, CsvMapper.Builder> CSV = new DataFormat<>("csv", new CsvFactory(), CsvMapper::new, CsvMapper::new, CsvMapper::builder, CsvMapper::builder).register();
    public static final DataFormat<TomlMapper, TomlFactory, TomlMapper.Builder> TOML = new DataFormat<>("toml", new TomlFactory(), TomlMapper::new, TomlMapper::new, TomlMapper::builder, TomlMapper::builder).register();
    public static final DataFormat<AvroMapper, AvroFactory, AvroMapper.Builder> AVRO = new DataFormat<>("avro", new AvroFactory(), AvroMapper::new, AvroMapper::new, AvroMapper::builder, AvroMapper::builder).register();
    public static final DataFormat<ProtobufMapper, ProtobufFactory, ProtobufMapper.Builder> PROTOBUF = new DataFormat<>("protobuf", new ProtobufFactory(), ProtobufMapper::new, ProtobufMapper::new, ProtobufMapper::builder, ProtobufMapper::builder).register();
    public static final DataFormat<IonObjectMapper, IonFactory, IonObjectMapper.Builder> ION = new DataFormat<>("ion", new IonFactory(), IonObjectMapper::new, IonObjectMapper::new, IonObjectMapper::builder, IonObjectMapper::builder).register();
    
    public static DataFormat<?, ?, ?> get(String name) {
        return FORMAT_BY_NAME.get(name.toLowerCase());
    }
    
    public DataFormat<M, F, B> register(String... otherNames) {
        FORMAT_BY_NAME.put(name.trim().toLowerCase(), this);
        for (String otherName : otherNames) FORMAT_BY_NAME.put(otherName.trim().toLowerCase(), this);
        return this;
    }
    
    private final String name;
    private final F factory;
    private final Supplier<M> mapperSupplier;
    private final Function<F, M> mapperFromFactory;
    private final Supplier<B> builderSupplier;
    private final Function<F, B> builderFromFactory;
    
    public DataFormat(
            String name,
            F factory,
            Supplier<M> mapperSupplier,
            Function<F, M> mapperFromFactory,
            Supplier<B> builderSupplier,
            Function<F, B> builderFromFactory
    ) {
        this.name = name;
        this.factory = factory;
        this.mapperSupplier = mapperSupplier;
        this.mapperFromFactory = mapperFromFactory;
        this.builderSupplier = builderSupplier;
        this.builderFromFactory = builderFromFactory;
    }
    
    public String name() {
        return name;
    }
    
    public F factory() {
        return factory;
    }
    
    public M mapper() {
        return mapperSupplier.get();
    }
    public M mapperFromFactory() {
        return mapperFromFactory.apply(factory);
    }
    public M mapperFromFactory(F factory) {
        return mapperFromFactory.apply(factory);
    }
    
    public B builder() {
        return builderSupplier.get();
    }
    public B builderFromFactory() {
        return builderFromFactory.apply(factory);
    }
    public B builderFromFactory(F factory) {
        return builderFromFactory.apply(factory);
    }
}
