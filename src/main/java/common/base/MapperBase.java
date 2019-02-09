package common.base;

import common.Tuple;
import common.collectors.OutCollector;

import java.io.Serializable;

public class MapperBase<Key, Value> implements Serializable, Runner {
    public void map(Tuple<Key,Value> t, OutCollector<Key,Value> out){

    }
}
