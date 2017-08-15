/**
 * Created by Ward Bradt on 7/9/17.
 */
import java.util.*;

/**
 * A wrapper class of sorts for a traditional <code>LinkedList</code> that is typically to be used for data analysis
 * of hash functions. Every <code>Object</code> in a <code>HashLinkedList</code> must have the same output for its
 * <code>hashcode()</code> function
 *
 * @param <T> any <code>Object</code> that can be hashed using a <code>hashcode()</code> function
 */
public class HashLinkedList<T> implements Comparable<HashLinkedList<T>>, Iterable<T> {
    private T contents;
    private HashLinkedList<T> next;

    public HashLinkedList(T object) {
        contents = object;
        next = null;
    }

    public HashLinkedList() {
        contents = null;
        next = null;
    }

    /**
     * Add the given object to the end of the <code>HashLinkedList</code>.
     * <i>If the <code>HashLinkedList</code> is of size '1' and its only
     * node has no data, the given object will be inserted
     * at index 0, and the <code>HashLinkedList</code>'s size will remain
     * the same.</i>
     *
     * If o does not have the same hash code as the other objects in the
     * <code>HashLinkedList</code>, it will not be added.
     *
     * Bits of the add method are inspired/ taken from a group project I
     * worked on in Spring of 2017 for a school project that created a
     * LinkedList class.
     *
     * @param o the object to add
     * @return whether the object was added successfully
     */
    public boolean add(T o) {
        return add(new HashLinkedList<>(o));
    }

    public boolean add(HashLinkedList<T> list) {
        if (contents == null && next == null) {
            contents = list.contents;
            next = list.next;
            return true;
        }

        if (contents.hashCode() != list.contents.hashCode())
            throw new IllegalArgumentException("All elements in both lists must have the same hash codes.");

        HashLinkedList<T> nextNode = this;
        /*
         * We start at 'next' because there is no scenario in which
         * the first element is empty (it is either 'null' or it
         * has data — that is, if you don't count 'null' as data!)
         */
        while (nextNode.next != null) {
            nextNode = nextNode.next;
        }
        nextNode.setNext(list);
        return true;
    }

    public String toString() {
        String str = "[";
        HashLinkedList<T> currentNode = this;
        while (currentNode.next != null) {
            str += currentNode.contents.toString();
            str += ", ";
            currentNode = currentNode.next;
        }
        str += currentNode.contents;
        str += "]";
        return str;
    }

    /**
     * Get the number of nodes in the HashLinkedList.
     *
     * @return the number of nodes in the HashLinkedList
     */
    public int size() {
        int nodes = 1; // start at 1, because the host object will always be a node
        HashLinkedList<T> currentNode = this;
        while (currentNode.getNext() != null) { // when there is a next node...
            nodes++; // increment the nodes...
            currentNode = currentNode.getNext(); // move to the next node — NOTE: this will never get 'null' because the 'while' loop does a check
        }
        return nodes;
    }

    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return contents != null;
            }

            @Override
            public T next() {
                if (contents == null) throw new NoSuchElementException("No more elements!");
                T popped = contents;
                if (next == null) {
                    contents = null;
                } else {
                    contents = next.contents;
                    next = next.next;
                }
                return popped;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
    /**
     * Compares which <code>HashLinkedList</code> has more elements
     * @param other another <code>HashLinkedList</code> to be compared to <code>this</code>
     * @return
     */
    public int compareTo(HashLinkedList<T> other) {
        return new Integer(contents.hashCode()).compareTo(other.contents.hashCode());
    }

    /**
     * Get the contents of the current node.
     *
     * @return the contents of the current node
     */
    public T getContents() {
        return contents;
    }

    /**
     * Set the contents of the current node.
     * This does not affect any nodes later in the <code>HashLinkedList</code>.
     *
     * @param contents the new contents of the current node
     */
    private void setContents(T contents) {
        this.contents = contents;
    }

    /**
     * Get the next node in the <code>HashLinkedList</code>.
     *
     * @return the next node in the <code>HashLinkedList</code>
     */
    public HashLinkedList<T> getNext() {
        return next;
    }

    /**
     * Set the next node in the <code>HashLinkedList</code>.
     * <i>This will, if used improperly, remove all future elements!</i>
     *
     * @param next the node to set as the next node in the <code>HashLinkedList</code>
     */
    private void setNext(HashLinkedList<T> next) {
        this.next = next;
    }
}
