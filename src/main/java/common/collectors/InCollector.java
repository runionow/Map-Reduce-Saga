package common.collectors;

import common.Tuple;

import java.util.Iterator;

public interface InCollector<Key,Value> extends Iterator {
    public void collect(Tuple<Key,Value> t);
    public int count();
    public void reset();
}
