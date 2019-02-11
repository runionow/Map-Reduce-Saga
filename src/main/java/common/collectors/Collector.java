package common.collectors;

import common.Tuple;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Collector<Key, Value> implements InCollector<Key, Value>, OutCollector<Key, Value>, Serializable {

    private List<Tuple<Key,Value>> inter_tuples = new CopyOnWriteArrayList<>();
    private Iterator<Tuple<Key,Value>> tuple_iter = null;
    private Map<Key, Value> final_sol = new TreeMap<>();

    public List<Tuple<Key,Value>> toList()
    {
        return inter_tuples;
    }

    public synchronized void add(Collection<Tuple<Key,Value>> list) {
        inter_tuples.addAll(list);
    }

    public Collector<Key,Value> intermediateCollector(Key key)
    {
        Collector<Key,Value> c = new Collector<Key,Value>();
        synchronized (this) {
            for (Tuple<Key,Value> t : inter_tuples)
            {
                if (t.getKey().equals(key))
                    c.inter_tuples.add(t);
            }
        }
        return c;
    }

    public Map<Key,Collector<Key,Value>> intermediateCollectors() {
        Map<Key, Collector<Key, Value>> out = new TreeMap<>();
        synchronized (this) {
            for ( Tuple<Key,Value> t : inter_tuples)
            {
                Key key = t.getKey();
                Collector<Key,Value> c = out.get(key);

                if (c == null)
                    c = new Collector<Key,Value>();

                c.collect(t);
                out.put(key, c);
            }
        }
        return out;
    }


    @Override
    public void collect(Tuple<Key, Value> t) {
        synchronized (this){
            inter_tuples.add(t);
        }
    }

    @Override
    public synchronized int count() {
        return inter_tuples.size();
    }

    @Override
    public void reset() {
        tuple_iter = null;
    }

    @Override
    public boolean hasNext() {
        if (inter_tuples == null)
            tuple_iter = inter_tuples.iterator();
        return tuple_iter.hasNext();
    }

    @Override
    public Object next() {
        return tuple_iter.next();
    }
}
