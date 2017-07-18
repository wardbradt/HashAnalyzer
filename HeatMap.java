/**
 * Created by Ward Bradt on 7/16/17.
 * Sources used:
 * https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
 */
import java.util.*;

public class HeatMap<T> {
    /**
     * HeatMap is an auxiliary class/ data structure for a PriorityHashTable.
     *
     */
    private PriorityHashTable<T> table;
    private HashMap<Integer, Integer> heatMap;

    public HeatMap() {
        table = null;
        heatMap = null;
    }

    public HeatMap(PriorityHashTable<T> table) {
        this.table = table;
        this.heatMap = getRawHeatMap(table);
    }

    /**
     * @param rows how many keys the HashMap of the returned HeatMap will contain.
     * @return a <code>HeatMap</code> with the given amount of rows
     */
    public static HashMap<Integer, Integer> averageHeatMap(HeatMap modHeatMap, int rows) {
        PriorityHashTable table = modHeatMap.table;
        double smallestHash = table.getSmallestHash();
        int difference = table.getLargestHash() - (int)smallestHash;
        // edge case: this.table.keySet().size() <= keys than rows
        if (difference < rows) {
            return modHeatMap.heatMap;
        }

        double increment = (double)difference / (double)(rows-1);

        Iterator it = modHeatMap.heatMap.entrySet().iterator();
        HashMap<Integer, Integer> resultHashMap = new HashMap<>();
        AbstractMap.SimpleEntry<Integer, Integer> iteratingPair = new AbstractMap.SimpleEntry<>((int)smallestHash, 0);
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry)it.next();
            // iterate to the correct entry for this pair, filling groups with zeroes until we get to the correct one
            // Todo: make it more space/time efficient by not having to store the K/Vs where V == 0?
            while (pair.getKey() >= smallestHash + increment) {
                resultHashMap.put(iteratingPair.getKey(), iteratingPair.getValue());
                smallestHash += increment;
                iteratingPair = new AbstractMap.SimpleEntry<>((int)smallestHash, 0);
            }
            iteratingPair.setValue(iteratingPair.getValue() + pair.getValue());
        }
        resultHashMap.put(iteratingPair.getKey(), iteratingPair.getValue());
        return resultHashMap;
    }

    /**
     * @param rows how many keys the HashMap of the returned HeatMap will contain.
     * @return a <code>HeatMap</code> with the given amount of rows
     */
    public static HashMap<Integer, Integer> averageHeatMap(PriorityHashTable table, int rows) {
        return averageHeatMap(new HeatMap(table), rows);
    }

    /**
     * Given a <code>PriorityHashTable</code>, <code>getRawHeatMap</code> returns a <code>HashMap</code>
     * of Integer: Integer. Each key is a hash code represented by one of the <code>HashLinkedList</code>s in the
     * <code>PriorityHashTable</code> hashTable. Each value is the size (<code>size()</code>) of that
     * <code>HashLinkedList</code>.
     * @param hashTable
     * @return a HashMap representation of <code>PriorityHashTable</code> hashTable
     */
    public static HashMap<Integer, Integer> getRawHeatMap(PriorityHashTable hashTable) {
        PriorityQueue<HashLinkedList> queue = hashTable.getQueue();
        HashMap<Integer, Integer> result = new HashMap<>();
        if (queue.isEmpty()) return result;

        PriorityQueue<HashLinkedList> queueCopy = new PriorityQueue<>();
        while (queue.size() > 0) {
            result.put(queue.peek().getContents().hashCode(), queue.peek().size());
            queueCopy.add(queue.poll());
        }

        queue.addAll(queueCopy);
        return result;
    }
}
