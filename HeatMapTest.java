import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by wardbradt on 7/17/17.
 */
public class HeatMapTest {
    @Test
    public void testGetRawHeatMap() {
        PriorityHashTable<Integer> heatMapTester = new PriorityHashTable<>();
        heatMapTester.add(5);
        heatMapTester.add(4);
        heatMapTester.add(12);
        heatMapTester.add(14);
        heatMapTester.add(5);
        heatMapTester.add(5);
        heatMapTester.add(4);

        HashMap<Integer, Integer> mapTest = HeatMap.makeRawHeatMap(heatMapTester);

        assertEquals(new Integer(3), mapTest.get(5));
        assertEquals(new Integer(2), mapTest.get(4));
        assertEquals(new Integer(1), mapTest.get(12));
        assertEquals(new Integer(1), mapTest.get(14));

        HashMap<Integer, Integer> mapCopyTest = new HashMap<>();
        mapCopyTest.put(5, 3);
        mapCopyTest.put(4, 2);
        mapCopyTest.put(12, 1);
        mapCopyTest.put(14, 1);
        assertEquals(mapCopyTest, mapTest);
    }

    @Test
    public void testAverageHeatMap() {
        PriorityHashTable<Integer> heatMapTester = new PriorityHashTable<>();
        heatMapTester.add(5);
        heatMapTester.add(4);
        heatMapTester.add(8);
        heatMapTester.add(12);
        heatMapTester.add(14);
        heatMapTester.add(5);
        heatMapTester.add(5);
        heatMapTester.add(4);

        HeatMap<Integer> tester = new HeatMap<>(heatMapTester);

        HashMap<Integer, Integer> mapTest = HeatMap.averageHeatMap(tester, 5);

//        // from 4 to 5, there are 5 values: 4,4,5,5,5
        assertEquals(new Integer(5), mapTest.get(4));
        assertEquals(new Integer(1), mapTest.get(6));
        assertEquals(new Integer(0), mapTest.get(9));
        assertEquals(new Integer(1), mapTest.get(11));
        assertEquals(new Integer(1), mapTest.get(14));
    }
}