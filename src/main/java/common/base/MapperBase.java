package common.base;

import common.collectors.OutCollector;
import common.Tuple;

import java.io.Serializable;

public class MapperBase<Key,Value> implements Serializable {
    public void map(Tuple<Key,Value> t, OutCollector<Key,Value> out){

    }
}
