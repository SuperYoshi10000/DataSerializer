package local.ytk.app.ds.data.tag;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static local.ytk.app.ds.data.save.Serializer.checkReadable;

/**
 * @deprecated use {@link ListTag} or nested list tags
 */
@Deprecated
public abstract class KVMapTag<K extends Tag, T extends Tag, M extends KVMapTag<K, T, M>> extends LinkedHashMap<K, T> implements DictionaryTag<K, T, M> {

}

