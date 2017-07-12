import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wardbradt on 7/9/17.
 */
public class HashLinkedListTest {
    @Test
    public void testAdd() {
        HashLinkedList<Integer> addTester = new HashLinkedList<Integer>(5);
        assertTrue(addTester.add(5));
        assertTrue(addTester.add(5));
        try {
            addTester.add(4);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            addTester.add(17);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // expected
        }

        addTester = new HashLinkedList<Integer>();
        assertTrue(addTester.add(3));
        try {
            addTester.add(14);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            addTester.add(5);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue(addTester.add(3));
        assertEquals(2, addTester.size());

        // test adding lists
        HashLinkedList<Integer> threeList = new HashLinkedList<>(3);
        threeList.add(3);
        threeList.add(3);
        HashLinkedList<Integer> fourList = new HashLinkedList<>(4);
        fourList.add(4);

        assertTrue(addTester.add(threeList));
        try {
            addTester.add(fourList);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(5, addTester.size());

    }

    @Test
    public void testToString() {
        HashLinkedList<Integer> toStringTester = new HashLinkedList<>();
        for (int i = 0; i < 10; i++) {
            toStringTester.add(5);
        }
        toStringTester.add(4);
        // really not much of a test; I know.
        System.out.println(toStringTester);
    }
}