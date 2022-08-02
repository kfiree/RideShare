package utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Hashmap with a priority queue to Map's keySet.
 *
 */
public class HashPriorityQueue<T> extends HashSet<T> implements Iterable<T>{
    private final PriorityQueue<T> queue;
    private boolean changed;
    /* CONSTRUCTORS */

    public HashPriorityQueue(Comparator<T> comparator) {
        queue = new PriorityQueue(comparator);
    }

    public HashPriorityQueue() {
        queue = new PriorityQueue();
    }


    /* QUEUE METHODS */

    public T poll() {
        T key = queue.poll();
        remove(key);

        return key;
    }

    public T peek() {
        return queue.peek();
    }



    /* HASHSET METHODS */

    @Override
    public boolean remove(Object key) {
        queue.remove(key);
        return super.remove(key);
    }

    @Override
    public boolean add(T val) {
        return super.add(val) && queue.add(val);
    }

    @Override
    public Iterator<T> iterator() {
        return new PriorityIterator();
    }


    private class PriorityIterator implements Iterator<T>{
        PriorityQueue<T> keys;
        T cursor;

        public PriorityIterator() {
            keys = new PriorityQueue<>(HashPriorityQueue.this.queue);
        }

        @Override
        public boolean hasNext() {
            return !keys.isEmpty();
        }

        @Override
        public T next() {
            cursor = keys.poll();
            return cursor;
        }

        @Override
        public void remove() {
            HashPriorityQueue.this.remove(cursor);
        }
    }


    public static void main(String[] args) {
//        double[] keys = {30.1, 1.2, 5.01, 3.3, 2.4, -1.0};
//
//        HashPriorityQueue<Double> q = new HashPriorityQueue<>(Comparator.comparingDouble(e -> e - e.intValue()));
//
//        q.add(30.1);
//        q.add(1.2);
//        q.add(5.01);
//        q.add(3.3);
//        q.add(2.4);
//
//
//
//        q.forEach(System.out::println);
//        Iterator<Double> iterator = q.iterator();
//
//        q.poll();
//
//        System.out.println(iterator.next());


        HashPriorityQueue<Double> pq = new HashPriorityQueue<>(Comparator.comparingDouble(e -> e - e.intValue()));
        pq.add(30.1);
        pq.add(1.2);
        pq.add(5.01);
        pq.add(3.3);
        pq.add(2.4);


        Iterator<Double> itr = pq.iterator();

        while(itr.hasNext()){
            System.out.println(itr.next());
        }
//
//        for (int i = 0; i < keys.length; i++) {
//            pm.add(keys[i]);
//
//        }
//        Iterator<Double> iterator = pm.iterator();
//        while(iterator.hasNext()){
//            Double poll = pm.poll();
//            Double next = iterator.next();
//            System.out.println(poll);
//            System.out.println(next);
//        }
//        iterator = pm.iterator();
//        while(iterator.hasNext()) {
//            System.out.println(iterator.next());
//
//        }
    }


}