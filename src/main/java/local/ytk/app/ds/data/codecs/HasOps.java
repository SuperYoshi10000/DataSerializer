package local.ytk.app.ds.data.codecs;

public interface HasOps<S extends HasOps<S, O>, O extends Ops<S>> {
    O getOps();
}
