package common.base;

import common.collectors.Collector;
import common.collectors.InCollector;
import common.collectors.OutCollector;

import java.io.Serializable;
import java.util.Map;

public class ReducerBase<Key, Value> implements Serializable, Runner {
    public Collector<Key, Value> out = new Collector<>();

    /**
     * The reduce recieves the shuffled data
     *
     * @param input
     * @param out
     */
    public void reduce(Map<Key, InCollector<Key, Value>> input, OutCollector<Key, Value> out) {

    }

    public Collector<Key, Value> getOut() {
        return out;
    }
}
