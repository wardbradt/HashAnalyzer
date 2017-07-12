/**
 * Created by Ward Bradt on 5/3/17.
 * Helper class for <code>HashAnalyzer.java</code>
 * A data structures that stores a <code>LinkedList</code> of <code>Object</code>s of the same class whose
 * <code>hashCode()</code> method returns the same value. The head node of the HashedLinkedList is the hash code
 * that all other <code>Object</code>s in the <code>HashedLinkedList</code> share.
 */
public class HashedLinkedList<T> extends LinkedList implements Comparable<HashedLinkedList<T>> {

    public HashedLinkedList(T object) {
        super(object);
    }

    public int compareTo(HashedLinkedList<T> other) throws IllegalArgumentException{
        // If either the first element of this or other is not an Integer
        if (!(Integer.class.isInstance(this.getContents()) && Integer.class.isInstance(other.getContents())))
            throw new IllegalArgumentException("The first element of both HashedLinkedLists must be an Integer.");

        // Compare the first element of the two HashedLinkedLists, which is a hash code.
        return new Integer((int)this.getContents()).compareTo((int)other.getContents());
    }
}
