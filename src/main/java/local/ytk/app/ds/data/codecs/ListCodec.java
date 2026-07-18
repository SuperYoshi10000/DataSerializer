package local.ytk.app.ds.data.codecs;

import java.util.List;

public class ListCodec<T> extends AbstractCodec<List<T>> {
    public ListCodec() {
        super(Encoder.of(Ops::ofList), Ops::getList);
    }
}
