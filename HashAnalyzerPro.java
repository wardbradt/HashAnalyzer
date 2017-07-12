import java.lang.reflect.*;
import java.util.*;

/**
 * Created by wardbradt on 7/9/17.
 */
public class HashAnalyzerPro<T> {
    private PriorityHashTable<T> table;
    private final int INSTANCE_AMOUNT = 1024;
    private Constructor constructor;
    private int[] parameterRanges;

    public HashAnalyzerPro(Class c, int[] r) {
        table = new PriorityHashTable<>();
        constructor = c.getConstructors()[0];
        parameterRanges = r;
    }

    public void hashReport() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("The average hashCode() runtime of an arbitrary " +
                table.getQueue().peek().getContents().getClass().getSimpleName()  + " " +
                "object is " + populateTable() + " nanoseconds.\n");
        String heatMap = heatMap();
        int rows = 1;
        String tempHeat = heatMap;
        while (tempHeat.indexOf("\n") > -1) {
            rows++;
            tempHeat = tempHeat.substring(tempHeat.indexOf("\n") + 1);
        }
    }

    /**
     * Returns a heat map of 50 lines or less displaying the frequencies of ranges of possible hash codes.
     *
     * @return a heat map of the frequencies of the randomized objects in hashTable.
     */
    public String heatMap() {
        String result = "";
        int rows = 50;

        int smallestHash = table.getSmallestHash();
        int largestHash = table.getLargestHash();
        int tableSize = table.getQueue().size();

        if (tableSize <= rows) {
            while (!table.getQueue().isEmpty()) {
                result += table.getQueue().peek().getContents().hashCode() + " ";
                result += printX(table.getQueue().poll().size());
                if (table.getQueue().size() != 0) result += "\n";
                // else, while loop is done.
            }
        }
        else {
            // The average difference of hash codes that will be grouped together in each section of the heatmap
            int inc = (largestHash - smallestHash) / rows;

            // start at smallestHash then += inc until at largestHash.
            result += smallestHash + " ";
            int barrier = smallestHash + inc;
            while (table.size() > 0) {
                if (table.getQueue().peek().getContents().hashCode() > barrier) {
                    result += "\n" + barrier + " ";
                    barrier += inc;
                }
                result += printX(table.getQueue().poll().size() - 1);
            }
        }
        return result;
    }

    public long populateTable() throws InstantiationException, IllegalAccessException, InvocationTargetException{
        // The parameter types for constructor.
        Class<?>[] paramTypes = constructor.getParameterTypes();

        long averageHashTime = 0;
        for (int i = 0; i < INSTANCE_AMOUNT; i++) {
            averageHashTime += table.add(generateRandomInstance(paramTypes));
        }
        return averageHashTime / (long)INSTANCE_AMOUNT;
    }

    public T generateRandomInstance(Class<?>[] paramTypes) throws InstantiationException,
            IllegalAccessException, InvocationTargetException {
        Object[] randomParams = new Object[paramTypes.length];
        /*
         * b is the index of the Object in randomParams we are creating, and d is the index of the
         * maximum and minimum for that specific parameter.
         */
        for (int b = 0, d = 0; b < randomParams.length; b++, d+=2) {
            // Create the random parameter Object.
            randomParams[b] = (new ParamBounds()).generate(parameterRanges[d], parameterRanges[d+1], paramTypes[b]);
        }
        T newObject = (T)constructor.newInstance(randomParams);
        return newObject;
    }

    /**
     * Helper method for heatMap(). Returns n 'x's in one String.
     * I don't feel it is necessary to code this using TDD.
     *
     * @param n amount of 'x's to be printed.
     */
    private static String printX(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += "x";
        }
        return result;
    }

    // To be used for tests.
    public void setHashTable(PriorityHashTable<T> x) {
        table = x;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public int[] getParameterRanges() {
        return parameterRanges;
    }
}
