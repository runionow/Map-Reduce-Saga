package common;

import java.io.Serializable;
import java.util.Objects;

public class Tuple<K, V> implements Serializable {

    private K map_key;
    private V map_value;

    public Tuple(K key, V value){
        setKey(key);
        setValue(value);
    }


    public K getKey() {
        return map_key;
    }

    public void setKey(K map_key) {
        this.map_key = map_key;
    }

    public V getValue() {
        return map_value;
    }

    public void setValue(V map_value) {
        this.map_value = map_value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(map_key, tuple.map_key) &&
                Objects.equals(map_value, tuple.map_value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map_key, map_value);
    }

    @Override
    public String toString() {
        return getKey() + ":" + getValue();
    }
}
