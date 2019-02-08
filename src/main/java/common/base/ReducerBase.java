package common.base;

import common.collectors.InCollector;
import common.collectors.OutCollector;

import java.io.Serializable;

public class ReducerBase<Key,Value> implements Serializable {
    public void reduce(InCollector<Key,Value> in, OutCollector<Key,Value> out){

    }

}
