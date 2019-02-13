package common.collectors;

import common.Tuple;

public interface OutCollector<Key,Value> {
    String fileName = null;

    void collect(Tuple<Key, Value> t);

    int count();

    void reset();

    String getFileName();

    void setFileName(String fileName);
}
