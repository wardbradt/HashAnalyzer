import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Created by wardbradt on 5/2/17.
 */
public class HashAnalyzerTest {

    @Test
    public void testGenerateRandomTable() throws NoSuchMethodException {
        HashAnalyzer test = new HashAnalyzer(Integer.class, Integer.class.getConstructor(int.class), new int[]{0,20});
        // implicitly checking that it throws no errors -- im not versed with reflection
        test.generateRandomTable(test.getConstructors()[0], test.getParameterRanges());
    }

    @Test
    public void testHeatMap() throws NoSuchMethodException {
        HashAnalyzer test = new HashAnalyzer(Integer.class,Integer.class.getConstructor(int.class), new int[]{0,20});
        PriorityQueue<HashedLinkedList<Object>> hashTableTest = new PriorityQueue<>();

        HashedLinkedList<Object> foo = new HashedLinkedList<>(8);
        foo.add("hey");
        hashTableTest.add(foo);
        HashedLinkedList<Object> foo2 = new HashedLinkedList<>(4);
        foo2.add("hey");
        foo2.add("hey world");
        foo2.add("hey there partner");
        foo2.add("PACTF YAAAY");
        hashTableTest.add(foo2);
        HashedLinkedList<Object> foo3 = new HashedLinkedList<>(12);
        foo3.add("hello");
        foo3.add("good night dr.zzzzzzzz");
        hashTableTest.add(foo3);
        test.setHashTable(hashTableTest);
        String heatMap = test.heatMap();

        int prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
        heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
        while (prev != 4) {
            assertTrue(prev > Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" "))));
            prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
            heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
        }
        // --------------------------------------------- New Test ---------------------------------------------------

        // reset the HashTable.
        hashTableTest = new PriorityQueue<>();
        ArrayList<Integer> hashes = new ArrayList<>();
        // I manually input the Integers because in the code for creating hashTable, there will never be two CLLs
        // that have duplicate hashes.
        hashes.add(46);
        hashes.add(15);
        hashes.add(42);
        hashes.add(60);
        hashes.add(78);
        hashes.add(48);
        hashes.add(52);
        hashes.add(19);
        hashes.add(29);
        hashes.add(27);
        hashes.add(24);
        hashes.add(32);
        hashes.add(38);
        hashes.add(36);

        for (int i = 0; i < hashes.size(); i++) {
            // Each LinkedList will have 3-15 objects (that share the same hash code && will normally be random).
            // The 1st element of every LinkedList will be between 2 and 31.

            HashedLinkedList<Object> added = new HashedLinkedList<>(hashes.get(i));
            for (int a = 0; a < 2 + (int)(Math.random() * 10); a++) {
                // Normally these would be randomized Objects, but as the properties of the Objects after the 1st
                // element - the hash - don't matter to heatMap(), they don't matter for this test.
                added.add(2);
            }
            hashTableTest.add(added);
        }
        test.setHashTable(hashTableTest);

        heatMap = test.heatMap();
        prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
        heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
        while (prev != 15) {
            assertTrue(prev > Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" "))));
            prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
            heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
        }
        // --------------------------------------------- New Test ---------------------------------------------------
        // This is testing how heatMap will actually be used.

        hashes = new ArrayList<>();
        for (int i = 1; i <= 1024; i++) {
            hashes.add(i);
        }
        // shuffle/ randomize hashes.
        Collections.shuffle(hashes);

        for (int i = 0; i < hashes.size(); i++) {
            HashedLinkedList<Object> added = new HashedLinkedList<>(hashes.get(i));
            for (int a = 0; a < 2 + (int)(Math.random() * 10); a++) {
                // Normally these would be randomized Objects, but as the properties of the Objects after the 1st
                // element - the hash - don't matter to heatMap(), they don't matter for this test.
                added.add(1);
            }
            hashTableTest.add(added);
        }

        test.setHashTable(hashTableTest);

        heatMap = test.heatMap();
        prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
        heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
        while (heatMap.indexOf("\n") > 0) {
            int currentHash = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
            assertTrue(prev < Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" "))));
            assertTrue(currentHash - prev >= 20 && currentHash - prev <=21);
            prev = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
            heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
        }
    }

    @Test
    public void testHeatAnalysis() throws NoSuchMethodException {
        HashAnalyzer test = new HashAnalyzer(TiredStudent.class,
                TiredStudent.class.getConstructor(String.class, int.class), new int[]{4,8,2,7});
        // This is where NoSuchMethodException would throw, if this constructor doesn't exist. IntelliJ made me do it.
        Constructor constructor = test.getConstructors()[0];
        int[] ranges = test.getParameterRanges();

        LinkedList<Object> randomObjects = test.generateRandomTable(constructor, ranges);
        test.hashReport(randomObjects);
        String testMap = "2 x\n4 xxxxxxxxxx\n6 xxxxxxxxxxxxxxxx\n7 xxx\n8 xxxxxxxxxxxxxxxxxx\n10 xxx\n12 xxxxx\n14 xxxx";
        String hashAnalysisTest = test.heatAnalysis(testMap, 8);
        assertEquals("1. 2 occurs 1 ",hashAnalysisTest.substring(hashAnalysisTest.indexOf("1."),
                hashAnalysisTest.indexOf("times")));

        while (hashAnalysisTest.length() > 0) {
            if (hashAnalysisTest.charAt(0) == 'T' || hashAnalysisTest.charAt(0) == 'A') {
                hashAnalysisTest = hashAnalysisTest.substring(hashAnalysisTest.indexOf("\n") + 1);
            }
            int hash = Integer.parseInt(hashAnalysisTest.substring(hashAnalysisTest.indexOf(" ") + 1,
                    hashAnalysisTest.indexOf("o") - 1));
            int frequency = Integer.parseInt(hashAnalysisTest.substring(hashAnalysisTest.indexOf("s") + 2,
                    hashAnalysisTest.indexOf("t") - 1));

            int index = testMap.indexOf(hash + " ");
            String hashAndX;
            if (testMap.substring(index).indexOf("\n") > -1)
                hashAndX = testMap.substring(index, testMap.substring(index).indexOf("\n") + index);
            // only if hash == 14
            else hashAndX = testMap.substring(index);

            // how many 'x's there are after a hash
            int xLength = hashAndX.substring(hashAndX.indexOf("x")).length();
            // check that the 'x's we declared earlier are correctly counted in heatAnalysis.
            assertEquals(frequency, xLength);

            hashAnalysisTest = hashAnalysisTest.substring(hashAnalysisTest.indexOf("\n") + 1);
            // if we have checked the most and least frequent already
            if (hashAnalysisTest.indexOf("\n") == 0) break;
        }

        // --------------------------------------------- New Test ---------------------------------------------------
        // Testing the edge case if a certain hash code never appears (i.e if the heat map has no 'x's for a certain
        // hash code.
        testMap = "2 x\n4 xxxxxxxxxx\n6 xxxxxxxxxxxxxxxx\n7 xxx\n8 xxxxxxxxxxxxxxxxxx\n9 \n12 xxxxx\n14 xxxx";
        hashAnalysisTest = test.heatAnalysis(testMap, 8);
        assertEquals("1. 9 occurs 0 ", hashAnalysisTest.substring(hashAnalysisTest.indexOf("\n") + 1,
                hashAnalysisTest.indexOf("times")));

        // --------------------------------------------- New Test ---------------------------------------------------
//        HashAnalyzer badTest = new HashAnalyzer(BadHash.class, BadHash.class.getConstructor(int.class, String.class),
//                new int[]{3,10,4,12});
//        badTest.analyze();
    }

    @Test
    public void testBadHash() throws NoSuchMethodException {
        HashAnalyzer badTest = new HashAnalyzer(BadHash.class, BadHash.class.getConstructor(int.class, String.class),
                new int[]{3,10,4,12});
        badTest.analyze();
    }

    @Test
    public void testContinuityAnalysis() throws NoSuchMethodException {
        int[][] contTest = new int[10][2];
        for (int i = 0; i < contTest.length; i++) {
            contTest[i][0] = i;
            contTest[i][1] = (int)(Math.random() * 20);
        }

        contTest[0][1] = 7;
        contTest[1][1] = 1;
        contTest[2][1] = 4;
        contTest[3][1] = 9;
        contTest[4][1] = 2;

        HashAnalyzer test = new HashAnalyzer(Integer.class, Integer.class.getConstructor(int.class), new int[]{0,20});
        String contStr = test.continuityAnalysis(contTest);
        contStr = contStr.substring(contStr.indexOf("difference"));
        System.out.println(test.continuityAnalysis(contTest));

        for (int i = 0; i < contTest.length - 1; i++) {
            int firstIndex = Integer.parseInt(contStr.substring(contStr.indexOf("between") + 8, contStr.indexOf("and")-1));
            assertEquals(Math.abs(contTest[firstIndex][1] - contTest[firstIndex+1][1]),
                    Integer.parseInt(contStr.substring(contStr.indexOf("is") + 3, contStr.indexOf("\n"))));
            if (contStr.indexOf("\n") > -1) contStr = contStr.substring(contStr.indexOf("\n") + 1);
        }
    }

    @Test
    public void testAll() throws NoSuchMethodException {
        HashAnalyzer englishTest = new HashAnalyzer(EnglishWord.class,
                EnglishWord.class.getConstructor(String.class), new int[]{2,12});
        englishTest.analyze();

        System.out.println("String");
        englishTest = new HashAnalyzer(String.class,
                String.class.getConstructor(String.class), new int[]{1,12});
        englishTest.analyze();

        System.out.println("Integer");
        englishTest = new HashAnalyzer(Integer.class, Integer.class.getConstructor(int.class), new int[]{0,20});
        englishTest.analyze();

    }

    @Test
    public void testDifferentiatePrimitive() {
        // Test String
        String[] arr = HashAnalyzer.differentiateParameter("abcd");
        assertEquals("bbcd", arr[0]);
        assertEquals("accd", arr[1]);
        assertEquals("abdd", arr[2]);
        assertEquals("abce", arr[3]);

        // test char
        char c = 'c';
        Character[] charArr = HashAnalyzer.differentiateParameter(c);
        assertArrayEquals(new Character[]{'d', 'b'}, charArr);

        // edge case when (int)char == 255 or 127 (how many ascii characters there are)/ is over the limt
        charArr = HashAnalyzer.differentiateParameter((char)127);
        charArr = HashAnalyzer.differentiateParameter((char)255);
        charArr = HashAnalyzer.differentiateParameter((char)563);
        // i am implicitly (not anymore) asserting that none of these calls to differentiateParameter throw exceptions.

        // test int
        Integer[] intArr = HashAnalyzer.differentiateParameter(500);
        assertArrayEquals(new Integer[]{501,499}, intArr);

        // test double
        Double[] doubleArr = HashAnalyzer.differentiateParameter(3.0);
        assertTrue(doubleArr[0] > 3.0);
        assertTrue(doubleArr[1] < 3.0);

        try {
            Object[] badHashArr = HashAnalyzer.differentiateParameter(new BadHash(3, "throw an exception!"));
            assertTrue(false);
        } catch (IllegalArgumentException expected) {
            // good to go
        }

        try {
            Object[] englishWordArr = HashAnalyzer.differentiateParameter(new EnglishWord("exception"));
            assertTrue(false);
        } catch (IllegalArgumentException expected) {
            // good to go
        }
    }
}
