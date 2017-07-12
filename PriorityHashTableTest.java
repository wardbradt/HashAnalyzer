import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Created by wardbradt on 7/9/17.
 */
public class PriorityHashTableTest {
    @Test
    public void testAdd() {
        PriorityHashTable<Integer> addTester = new PriorityHashTable<>();
        addTester.add(5);
        addTester.add(4);
        addTester.add(5);
        addTester.add(4);
        addTester.add(5);
        addTester.add(6);
        assertEquals(6, addTester.getLargestHash());
        assertNotEquals(5, addTester.getLargestHash());
        assertNotEquals(2, addTester.getLargestHash());
        // Test that the iteration doesn't mess up the PQ
        assertEquals(6, addTester.getLargestHash());
        assertEquals(4, addTester.getSmallestHash());
        assertNotEquals(6, addTester.getSmallestHash());

        assertEquals(4, (int)addTester.getQueue().peek().getContents());
        assertEquals(2, addTester.getQueue().poll().size());
        assertEquals(5, (int)addTester.getQueue().peek().getContents());
        assertEquals(3, addTester.getQueue().poll().size());
        assertEquals(6, (int)addTester.getQueue().peek().getContents());
        assertEquals(1, addTester.getQueue().poll().size());
    }

    @Test
    public void testGetHashes() {
        PriorityHashTable<Integer> addTester = new PriorityHashTable<>();
        addTester.add(5);
        addTester.add(4);
        addTester.add(5);
        addTester.add(4);
        addTester.add(5);
        addTester.add(6);

        assertEquals(4, addTester.getSmallestHash());
        assertNotEquals(3, addTester.getSmallestHash());
        assertEquals(6, addTester.getLargestHash());
    }

    @Test
    public void testIterator() {

    }
}