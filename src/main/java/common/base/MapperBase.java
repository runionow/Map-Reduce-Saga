package common.base;

import common.Tuple;
import common.collectors.Collector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MapperBase<Key, Value> implements Serializable, Runner {
    public Collector<Key, Value> context = new Collector<>();

    private Map<Key, Value> intermediate = new HashMap<>();

    public void map(Tuple<Key, Value> t, Collector<Key, Value> context) {

    }

    public Collector<Key, Value> getOut() {
        return context;
    }

    public Map<Key, Value> getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(Map<Key, Value> intermediate) {
        this.intermediate = intermediate;
    }
}
