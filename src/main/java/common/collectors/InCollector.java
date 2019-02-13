package common.collectors;

import common.Tuple;

import java.util.Iterator;

public interface InCollector<Key,Value> extends Iterator {
    String fileName = null;

    void collect(Tuple<Key, Value> t);

    int count();

    void reset();

    String getFileName();

    void setFileName(String fileName);
}
