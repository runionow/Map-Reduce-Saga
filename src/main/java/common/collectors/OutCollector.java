package common.collectors;

import common.Tuple;

public interface OutCollector<Key,Value> {
    public void collect(Tuple<Key,Value> t);
    public int count();
    public void reset();
}
