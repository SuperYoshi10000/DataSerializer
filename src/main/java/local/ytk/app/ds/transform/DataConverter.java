package local.ytk.app.ds.transform;

import local.ytk.app.ds.val.DataValue;

public interface DataConverter<T> extends DataCreator<T>, DataReader<T>, DataOperator<T> {
}
