import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Created by wardbradt on 7/10/17.
 */
public class HashAnalyzerProTest {
//    @Test
//    public void testHeatMap() throws NoSuchMethodException {
//        HashAnalyzerPro test = new HashAnalyzerPro(Integer.class, new int[]{0,20});
//        PriorityHashTable<Integer> hashTableTester = new PriorityHashTable<>();
//
//        HashLinkedList<Integer> foo = new HashLinkedList<>(8);
//        foo.add(8);
//        foo.add(8);
//        hashTableTester.add(foo);
//        HashLinkedList<Integer> foo2 = new HashLinkedList<>(4);
//        foo2.add(4);
//        foo2.add(4);
//        foo2.add(4);
//        hashTableTester.add(foo2);
//        test.setHashTable(hashTableTester);
//        String heatMap = test.heatMap();
//        System.out.println(heatMap);
//
////        // --------------------------------------------- New Test ---------------------------------------------------
////
////        // reset the HashTable.
////        hashTableTester = new PriorityQueue<>();
////        ArrayList<Integer> hashes = new ArrayList<>();
////        // I manually input the Integers because in the code for creating hashTable, there will never be two CLLs
////        // that have duplicate hashes.
////        hashes.add(46);
////        hashes.add(15);
////        hashes.add(42);
////        hashes.add(60);
////        hashes.add(78);
////        hashes.add(48);
////        hashes.add(52);
////        hashes.add(19);
////        hashes.add(29);
////        hashes.add(27);
////        hashes.add(24);
////        hashes.add(32);
////        hashes.add(38);
////        hashes.add(36);
////
////        for (int i = 0; i < hashes.size(); i++) {
////            // Each LinkedList will have 3-15 objects (that share the same hash code && will normally be random).
////            // The 1st element of every LinkedList will be between 2 and 31.
////
////            HashLinkedList<Object> added = new HashLinkedList<>(hashes.get(i));
////            for (int a = 0; a < 2 + (int)(Math.random() * 10); a++) {
////                // Normally these would be randomized Objects, but as the properties of the Objects after the 1st
////                // element - the hash - don't matter to heatMap(), they don't matter for this test.
////                added.add(2);
////            }
////            hashTableTester.add(added);
////        }
////        test.setHashTable(hashTableTester);
////
////        heatMap = test.heatMap();
////        prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
////        heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
////        while (prev != 15) {
////            assertTrue(prev > Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" "))));
////            prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
////            heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
////        }
////        // --------------------------------------------- New Test ---------------------------------------------------
////        // This is testing how heatMap will actually be used.
////
////        hashes = new ArrayList<>();
////        for (int i = 1; i <= 1024; i++) {
////            hashes.add(i);
////        }
////        // shuffle/ randomize hashes.
////        Collections.shuffle(hashes);
////
////        for (int i = 0; i < hashes.size(); i++) {
////            HashLinkedList<Object> added = new HashLinkedList<>(hashes.get(i));
////            for (int a = 0; a < 2 + (int)(Math.random() * 10); a++) {
////                // Normally these would be randomized Objects, but as the properties of the Objects after the 1st
////                // element - the hash - don't matter to heatMap(), they don't matter for this test.
////                added.add(1);
////            }
////            hashTableTester.add(added);
////        }
////
////        test.setHashTable(hashTableTester);
////
////        heatMap = test.heatMap();
////        prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
////        heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
////        while (heatMap.indexOf("\n") > 0) {
////            int currentHash = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
////            assertTrue(prev < Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" "))));
////            assertTrue(currentHash - prev >= 20 && currentHash - prev <=21);
////            prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
////            heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
////        }
//    }

    @Test
    public void testViewHashReport() throws IllegalAccessException, InstantiationException, InvocationTargetException {
//        PriorityHashTable<Integer> heatMapTester = new PriorityHashTable<>();
//        heatMapTester.add(5);
//        heatMapTester.add(4);
//        heatMapTester.add(8);
//        heatMapTester.add(12);
//        heatMapTester.add(14);
//        heatMapTester.add(5);
//        heatMapTester.add(5);
//        heatMapTester.add(4);
//        heatMapTester.add(32);
//        heatMapTester.add(4);
//        heatMapTester.add(16);
//        heatMapTester.add(12);
//        heatMapTester.add(15);

        HashAnalyzerPro<Integer> tester = new HashAnalyzerPro<>(Integer.class, new int[]{10,30});

        tester.hashReport();
    }
}