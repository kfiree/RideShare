package utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Hashmap with a priority queue to Map's keySet.
 *
 * @param <K> key type.
 * @param <V> value type.
 */
public class HashPriorityQueue<K, V> extends HashMap<K, V> implements Iterable<AbstractMap.SimpleEntry<K, V>>{
    private final PriorityQueue<K> queue;
    private boolean changed;
    /* CONSTRUCTORS */

    public HashPriorityQueue(Comparator<K> comparator) {
        queue = new PriorityQueue(comparator);
    }

    public HashPriorityQueue() {
        queue = new PriorityQueue();
    }


    /* QUEUE METHODS */

    public AbstractMap.SimpleEntry<K, V> poll() {
        K key = queue.poll();
        V val = remove(key);
        return new AbstractMap.SimpleEntry<K, V>(key, val);
    }

    public AbstractMap.SimpleEntry<K, V> peek() {
        K key = queue.peek();
        V val = get(key);
        return new AbstractMap.SimpleEntry<K, V>(key, val);
    }



    /* MAP METHODS */

    @Override
    public V remove(Object key) {
        queue.remove(key);
        return super.remove(key);
    }

    public V remove(AbstractMap.SimpleEntry<V, K> entry) {
        return remove(entry.getKey());
    }

    @Override
    public V put(K key, V value) {
        queue.add(key);
        return super.put(key, value);
    }

    @Override
    public Iterator<AbstractMap.SimpleEntry<K, V>> iterator() {
        return new PriorityIterator();
    }


    private class PriorityIterator implements Iterator<AbstractMap.SimpleEntry<K, V>>{
        PriorityQueue<K> keys;
        K cursor;

        public PriorityIterator() {
            keys = new PriorityQueue<>(HashPriorityQueue.this.queue);
        }

        @Override
        public boolean hasNext() {
            return !keys.isEmpty();
        }

        @Override
        public AbstractMap.SimpleEntry<K, V> next() {
            cursor = keys.poll();
            V v = HashPriorityQueue.this.get(cursor);
            return new AbstractMap.SimpleEntry<>(cursor,v);
        }

        @Override
        public void remove() {
            HashPriorityQueue.this.remove(cursor);
        }
    }


    public static void main(String[] args) {
        double[] keys = {30.1, 1.2, 5.01, 3.3, 2.4, -1.0};
        String[] values = {"E", "A", "D", "C", "B", "D"};
        Comparator<Double> objectComparator = Comparator.comparingDouble(e -> e - e.intValue());
        HashPriorityQueue<Double, String> pm = new HashPriorityQueue<>(Comparator.comparingDouble(e -> e - e.intValue()));
        for (int i = 0; i < keys.length; i++) {
            pm.put(keys[i], values[i]);

        }
        Iterator<SimpleEntry<Double, String>> iterator = pm.iterator();
        while(iterator.hasNext()){
            SimpleEntry<Double, String> poll = pm.peek();
            SimpleEntry<Double, String> next = iterator.next();
            System.out.println(poll);
            System.out.println(next);
        }
        iterator = pm.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());

        }
    }


}