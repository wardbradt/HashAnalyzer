import java.util.*;
/**
 * Created by wardbradt on 7/9/17.
 * Wrapper class for PriorityQueue of HLLs basically
 */
public class PriorityHashTable<T> {
    private PriorityQueue<HashLinkedList<T>> queue;

    public PriorityHashTable() {
        queue = new PriorityQueue<>();
    }

    public long add(HashLinkedList<T> list) {
        // Need an object to hash
        T obj = list.getContents();
        if (queue.isEmpty()) {
            queue.add(list);
            long startTime = System.nanoTime();
            obj.hashCode();
            return System.nanoTime() - startTime;
        }
        // For iteration
        PriorityQueue<HashLinkedList<T>> copy = new PriorityQueue<>();

        long startTime = System.nanoTime();
        int hashed = obj.hashCode();
        startTime = System.nanoTime() - startTime;

        while (queue.size() > 0) {
            if (queue.peek().getContents().hashCode() == hashed) {
                // add this list of elements to the HLL of elements with the same hash code
                queue.peek().add(list);
                break;
            } else {
                copy.add(queue.poll());
            }
        }

        // if hashed is not yet in queue.
        if (queue.isEmpty()) {
            queue.add(list);
        }

        queue.addAll(copy);
        return startTime;
    }

    /**
     * Adds an Object to queue
     * @param obj
     * @return the time (in nanoseconds) it takes to hash T obj
     */
    public long add(T obj) {
        if (queue.isEmpty()) {
            queue.add(new HashLinkedList<T>(obj));
            long startTime = System.nanoTime();
            obj.hashCode();
            return System.nanoTime() - startTime;
        }
        // For iteration
        PriorityQueue<HashLinkedList<T>> copy = new PriorityQueue<>();

        long startTime = System.nanoTime();
        int hashed = obj.hashCode();
        startTime = System.nanoTime() - startTime;

        while (queue.size() > 0) {
            if (queue.peek().getContents().hashCode() != hashed) {
                copy.add(queue.poll());
            }
            // if obj belongs in the HLL we have iterated to
            else {
                queue.peek().add(obj);
                break;
            }
        }

        // if hashed is not yet in queue.
        if (queue.isEmpty()) {
            queue.add(new HashLinkedList<T>(obj));
        }

        queue.addAll(copy);
        return startTime;
    }

    public long add(T obj, boolean k) {
        return add(new HashLinkedList<>(obj));
    }

    public int getSmallestHash() {
        return queue.peek().getContents().hashCode();
    }

    public int size() {
        return queue.size();
    }

    // Think: computer property from Swift
    // Todo: better way to iterate?
    public int getLargestHash() {
        if (queue.isEmpty()) throw new IllegalStateException("queue has no elements!");
        PriorityQueue<HashLinkedList<T>> queueCopy = new PriorityQueue<>();
        while (queue.size() > 1) {
            queueCopy.add(queue.poll());
        }

        int largestHash = queue.peek().getContents().hashCode();
        queue.addAll(queueCopy);
        return largestHash;
    }

    public PriorityQueue<HashLinkedList<T>> getQueue() {
        return queue;
    }
}
