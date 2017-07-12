import java.lang.reflect.*;
import java.util.*;

/**
 * Created by Ward Bradt on 05/03/2017.
 * First revision: 06/09/2017
 * Takes a <code>class</code> and provides various analyses of its <code>hashCode()</code> method by generating
 * instances using the first available constructor using Java's reflection and random parameter generation.
 *
 * Citations:
 * http://www.journaldev.com/1789/java-reflection-example-tutorial#public-constructor
 */
public class HashAnalyzerCopy {
    private PriorityQueue<HashLinkedList<Object>> hashTable;
    private Class analyzedClass;
    private final int INSTANCE_AMOUNT = 1024;
    private Constructor<?>[] constructors;
    private int[] parameterRanges;

    /**
     * @param c the <code>Class</code> that this <code>HashAnalyzer</code> object analyzes.
     */
    public HashAnalyzerCopy(Class c, int[] r) {
        hashTable = new PriorityQueue<>();
        analyzedClass = c;
        constructors = analyzedClass.getConstructors();
        parameterRanges = r;
    }

    public HashAnalyzerCopy(Class c, Constructor<?> constructor, int[] r) {
        hashTable = new PriorityQueue<>();
        analyzedClass = c;
        constructors = new Constructor<?>[]{constructor};
        parameterRanges = r;
    }

    /**
     * Generates 1024 instances of analyzedClass objectss using a constructor specified by the constructor
     * parameter then returns a <code>LinkedList</code> of those <code>INSTANCE_AMOUNT</code> analyzedClass
     * <code>Object</code>s, typically to be used by hashReport. I think using two different methods might be slightly
     * inefficient because we must traverse over 1024 not once, but twice.
     *
     * @param constructor the specific constructor for analyzedClass <code>Object</code>s that <code>Object</code>s will be
     *                    instantiated with.
     * @param ranges the ranges of each parameter for each constructor. Examples of how to use this can
     *               be found in <code>HashAnalyzerTest.java</code>.
     * @return a <code>LinkedLis</code>t of 1024 random objects of class analyzedClass.
     */
    public LinkedList<Object> generateRandomTable(Constructor<?> constructor, int[] ranges) {
        // The parameter types for constructor.
        Class<?>[] paramTypes = constructor.getParameterTypes();

        // The length of result will be 1024 and will hold all random instances.
        LinkedList<Object> result = new LinkedList<>();

        for (int i = 0; i < INSTANCE_AMOUNT; i++) {
            // This is an array of the random instantations of each parameter.
            Object[] randomParams = new Object[paramTypes.length];

           /*
            * b is the index of the Object in randomParams we are creating, and d is the column of the
            * maximum and minimum.
            */
            for (int b = 0, d = 0; b < randomParams.length; b++, d+=2) {
                // Create the random parameter Object.
                randomParams[b] = (new ParamBounds()).generate(ranges[d], ranges[d+1], paramTypes[b]);
            }
            try {
                Object newObj = constructor.newInstance(randomParams);
                // add it at 0 to save runtime.
                result.add(0, newObj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Generates 1024 <code>analyzedClass</code> instances using all of analyzedClass's constructors, then returns
     * an array of those 1024 analyzedClass objects, typically to be used by hashReport. I think using
     * two different methods might be slightly inefficient because we must traverse over 1024 not once,
     * but twice.
     *
     * @param ranges the ranges of each parameter for each constructor. Examples of how to use this can
     *               be found in HashAnalyzerTest.java.
     * @return a LinkedList of 1024 random objects of class analyzedClass.
     */
   public LinkedList<Object> generateRandomTable(int[][] ranges) {
       // The public constructors in Class c
       Constructor<?>[] publicConstructors = analyzedClass.getConstructors();
       // How many Objects we will instantiate using each constructor - Might
       // instantiate slightly more Objects using the last constructor because of int division
       int changeNum = INSTANCE_AMOUNT / publicConstructors.length;
       // The index of which constructor we are using in publicConstructors.
       int conIndex = 0;
       // The parameter types for the first constructor.
       Class<?>[] paramTypes = publicConstructors[conIndex].getParameterTypes();

       // The length of result will be 1024 and will hold all random instances.
       LinkedList<Object> result = new LinkedList<>();

       for (int i = 0; i < INSTANCE_AMOUNT; i++) {
           // Note: I think we are saving runTime by only updating paramTypes every time
           // the constructor changes; I might be wrong, code still works anyway. - Ward
           if (i % changeNum == 0 && i != 0) {
               conIndex++;
               paramTypes = publicConstructors[conIndex].getParameterTypes();
           }

           // This is an array of the random instantations of each parameter.
           Object[] randomParams = new Object[paramTypes.length];
           /*
            * b is the index of the Object in randomParams we are creating, and d is the column of the
            * maximum and minimum.
            */
           for (int b = 0, d = 0; b < randomParams.length; b++, d+=2) {
               // Create the random parameter Object.
               randomParams[b] = (new ParamBounds()).generate(ranges[conIndex][d], ranges[conIndex][d+1], paramTypes[b]);
           }
           try {
               Object newObj = publicConstructors[conIndex].newInstance(randomParams);
               // add it at 0 to save runtime.
               result.add(0, newObj);
           } catch (InstantiationException e) {
               e.printStackTrace();
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (InvocationTargetException e) {
               e.printStackTrace();
           }
       }
       return result;
   }

   public void hashReport() {

   }

    /**
     * Generates a hash report for the objects in randoms.
     * Beware: Will clear all of hashTable's current elements.
     *
     * @param randoms a LinkedList of Objects that we will use to create the HashReport.
     */
    public void hashReport(LinkedList<Object> randoms){
        hashTable = new PriorityQueue<>();
        long averageHash = 0;
        for (int i = 0; i < randoms.size(); i++) {
             // add randoms.get(i) to the hashTable and add its hashCode() runtime to averageHash.
             if (randoms.get(i) != null) averageHash += hashTableAdd((randoms.get(i)));
        }
        averageHash /= (long)INSTANCE_AMOUNT;

        // Print out the report.
        System.out.println("The average hashCode() runtime of an arbitrary " +
                randoms.get(0).getClass().getSimpleName()  + " object is " + averageHash + " nanoseconds.\n");
        String heatMap = heatMap();
        int rows = 1;
        String tempHeat = heatMap;
        while (tempHeat.indexOf("\n") > -1) {
            rows++;
            tempHeat = tempHeat.substring(tempHeat.indexOf("\n") + 1);
        }
        System.out.println(heatAnalysis(heatMap, rows));
        System.out.println("\nHeat Map: ");
        // System.out.println("Note: This heat map is altered for aesthetics so that it has no more than")
        System.out.println(heatMap + "\n");
        System.out.println("--- Avalanche Effect Analysis ---");
        System.out.println(paramChange());
        System.out.println("\n--- Continuity Effect Analysis ---");
        System.out.println(continuityAnalysis(heatMapToArr(heatMap, rows)));
    }

    /**
     * Generates an analysis of the avalanche effect of <code>analyzedClass</code>
     * @return
     */
    public String paramChange() {
        boolean isContinuous = true;
        String result = "";

        // A lot of this code is copied from the previous generateRandomTable method.
        Class<?>[] paramTypes = constructors[0].getParameterTypes();
        Object[] randomParams = new Object[paramTypes.length];
        int[] finalSums = new int[randomParams.length];
        double diff = 0;

        // Loop, generating INSTANCE_AMOUNT instances of analyzedClass
        for (int f = 0; f < INSTANCE_AMOUNT; f++) {
            for (int i = 0; i < randomParams.length; i++) {
                // Create the random parameter Object.
                randomParams[i] = (new ParamBounds()).generate(parameterRanges[i*2], parameterRanges[i*2+1], paramTypes[i]);
            }

            Object newObj = new Object();
            // I don't know why I have to do these try/ catch statements, but intelliJ says I have to.
            try {
                newObj = constructors[0].newInstance(randomParams);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            int originalHash = newObj.hashCode();
            Object[] paramsCopy = randomParams;
            Object objCopy = new Object();
            int prev = originalHash;

            // Iterate over the parameters that were used to construct newObj
            for (int i = 0; i < randomParams.length; i++) {
                // the variations of the element in randomParams we are testing
                Object[] alteredParams = differentiateParameter(randomParams[i]);

                int[] sums = new int[randomParams.length];
                // differences[i] = new int[alteredParams.length];

                // iterate over the variations
                for (int a = 0; a < alteredParams.length; a++) {
                    // Instantiate a new object (objCopy) using the altered parameter at alteredParams[a]
                    paramsCopy[i] = alteredParams[a];
                    try {
                        objCopy = constructors[0].newInstance(paramsCopy);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    if (a == 0) {
                        diff = prev + (Math.abs(objCopy.hashCode() - originalHash) * 1.1);
                    }
                    else {
                        if (objCopy.hashCode() > diff) isContinuous = false;
                        else {
                            prev = objCopy.hashCode();
                            diff = prev + diff;
                        }
                    }

                    // we are summing the hashes for the variations of objCopy
                    sums[i] += Math.abs(objCopy.hashCode() - originalHash);
                    // differences[i][a] = Math.abs(objCopy.hashCode() - originalHash);
                }

                sums[i] /= alteredParams.length;

                // add the average of the hashes for this group of variations to finalSums
                finalSums[i] += sums[i];

                paramsCopy = randomParams;
            }
        }

        for (int a = 0; a < finalSums.length; a++) {
            // randomParams.length == finalSums.length
            finalSums[a] /= INSTANCE_AMOUNT;
            result += "Minorly changing the " + randomParams[a].getClass().getSimpleName() + " parameter of an " +
                    "arbitary " + analyzedClass.getSimpleName() +" object changes the hash code output by an average of " +
                    finalSums[a] + ".";
            if (a != finalSums.length - 1) result += "\n";
        }
        result += "\nThis method's continuity is " + isContinuous + ".";

        return result;
    }

    /**
     * Returns an array of objects that are slightly differentiated/ altered from obj.
     *
     * @param obj the object you would like to be changed
     * @param <T>
     * @return an array of objects/ primitives slightly altered from obj.
     */
    public static <T> T[] differentiateParameter(T obj) throws IllegalArgumentException {
        Class<?> cls = obj.getClass();

        if (cls.equals(String.class)) {
            String stringObject = (String)obj;

            String[] result = new String[stringObject.length()];
            for (int i = 0; i < result.length; i++) {
                result[i] = stringObject.substring(0, i) + (char)((int) (stringObject.charAt(i) + 1)) +
                        stringObject.substring(i + 1);
            }
            return (T[])result;
        }
        if (cls.equals(Character.class)) {
            char charObj = (char)(Object)obj;
            Character[] result = new Character[2];
            result[0] = (char)((int)charObj + 1);
            result[1] = (char)((int)charObj - 1);
            return (T[])result;
        }
        if (cls.equals(Integer.class)) {
            int intObj = (int)(Object)obj;

            Integer[] result = new Integer[2];
            result[0] = intObj + 1;
            result[1] = intObj - 1;
            return (T[])result;
        }
        // Citation: For this double conditional, a lot of the code is inspired by:
        // http://stackoverflow.com/questions/3658174/how-to-alter-a-float-by-its-smallest-increment-in-java
        if (cls.equals(Double.class)) {
            Double[] result = new Double[2];
            double d = (double)(Object)obj;
            long bits = Double.doubleToLongBits(d);
            result[0] = Double.longBitsToDouble(bits + 1);
            result[1] = Double.longBitsToDouble(bits - 1);
            return (T[])result;
        }
        // Use a similar conversion method as double's.
        if (cls.equals(Float.class)) {
            Float[] result = new Float[2];
            float f = (float)(Object)obj;
            int intBits = Float.floatToIntBits(f);
            result[0] = Float.intBitsToFloat(intBits + 1);
            result[1] = Float.intBitsToFloat(intBits - 1);
            return (T[])result;
        }
        if (cls.equals(Short.class)) {
            short shortObj = (short)(Object)obj;

            Short[] result = new Short[2];
            // We need to have two casts because java will implicitly convert short to int:
            // http://stackoverflow.com/questions/477750/primitive-type-short-casting-in-java
            result[0] = (short)(shortObj + 1);
            result[1] = (short)(shortObj - 1);
            return (T[])result;
        }
        if (cls.equals(Long.class)) {
            long longObj = (long)(Object)obj;

            Long[] result = new Long[2];
            result[0] = longObj + 1;
            result[1] = longObj - 1;
            return (T[])result;
        }
        if (cls.equals(Byte.class)) {
            byte byteObj = (byte)(Object)obj;

            if (byteObj == -128) return (T[])(new Byte[]{(byte)(byteObj + 1)});
            if (byteObj == 127) return (T[])(new Byte[]{(byte)(byteObj - 1)});

            Byte[] result = new Byte[2];
            result[0] = (byte)(byteObj + 1);
            result[1] = (byte)(byteObj - 1);
            return (T[])result;
        }
        if (cls.equals(Boolean.class)) {
            return (T[])(new Boolean[]{!(boolean)(Object)obj});
        }

        throw new IllegalArgumentException("Parameter obj must be either a primitive or a String!");
    }

    /**
     * A method very similar to the code in generateRandomTable, however this will be used to generate singular
     * objects.
     * @param constructor the constructor being used to generate random instances
     * @param ranges the ranges of the parameters in constructor. see HashAnalyzerTest.java for details
     * @return a random instance using constructor
     */
    public Object generateRandomInstance(Constructor<?> constructor, int[] ranges) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] randomParams = new Object[paramTypes.length];
        for (int b = 0, d = 0; b < randomParams.length; b++, d+=2) {
            // Create the random parameter Object.
            randomParams[b] = (new ParamBounds()).generate(ranges[d], ranges[d+1], paramTypes[b]);
        }
        Object newObj = new Object();
        try {
            newObj = constructor.newInstance(randomParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return newObj;
    }

    /**
     * Returns a heat map of 50 lines or less displaying the frequencies of ranges of possible hash codes.
     *
     * @return a heat map of the frequencies of the randomized objects in hashTable.
     */
    public String heatMap() {
        String result = "";
        int rows = 50;
        // The code in this method may be slightly inefficient due to 2 traversals, but it is written to value the
        // aesthetics of the printout over the runtime.

        // the smallest hash code in hashTable
        int smallestHash = (int)hashTable.peek().getContents();
        // find the largest hashcode
        PriorityQueue<HashLinkedList<Object>> copy = new PriorityQueue<>();
        while (hashTable.size() > 1) {
            copy.add(hashTable.poll());
        }

        int largestHash = (int) hashTable.peek().getContents();
        hashTable.addAll(copy);

        if (copy.size() + 1 <= rows) {
            while (hashTable.size() > 0) {
                result += hashTable.peek().getContents() + " ";
                result += printX(hashTable.poll().size() - 1);
                if (hashTable.size() != 0) result += "\n";
                // else, while loop is done.
            }
        }
        else {
            // The average difference of hash codes that will be grouped together in each section of the heatmap
            int inc = (largestHash - smallestHash) / rows;

            // start at smallestHash then += inc until at largestHash.
            result += smallestHash + " ";

            int barrier = smallestHash + inc;
            while (hashTable.size() > 0) {
                if ((int) hashTable.peek().getContents() > barrier) {
                    result += "\n" + barrier + " ";
                    barrier += inc;
                }
                result += printX(hashTable.poll().size() - 1);
            }
        }
        return result;
    }

    /**
     * Turns a heatMap String into an int[][] representing a heat map.
     *
     * @param heatMap the String that follows heatMap's convention.
     * @param rows the amount of rows in heatMap
     * @return an int[][] representing a heat map.
     */
    public int[][] heatMapToArr(String heatMap, int rows) {
        int[][] heat = new int[rows][2];
        for (int i = 0; i < rows; i++) {
            if (heatMap.indexOf("\n") < 0) {
                heat[i][0] = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
                heat[i][1] = heatMap.substring(heatMap.indexOf(" ")).length() - 1;
                return heat;
            }
            else {
                heat[i][0] = Integer.parseInt(heatMap.substring(0, heatMap.indexOf(" ")));
                //heat[i][1] =
                if (!heatMap.substring(0, heatMap.indexOf("\n")).contains("x")) {
                    heat[i][1] = 0;
                }
                else heat[i][1] = heatMap.substring(heatMap.indexOf("x"), heatMap.indexOf("\n")).length();
            }

            heatMap = heatMap.substring(heatMap.indexOf("\n") + 1);
        }
        return heat;
    }

    /**
     * Analyzes an int[][] that represents a heat map for continuity.
     *
     * @param arr an int[][] representing a heat map
     * @return an analysis of arr's continuity.
     */
    public String continuityAnalysis(int[][] arr) {
        String result = "";

        double jumpAverage = 0;
        // size() will be == arr.length - 1
        ArrayList<Integer> jumps = new ArrayList<>();

        for (int r = 0; r < arr.length-1; r++) {
            jumps.add(new Integer(Math.abs(arr[r+1][1] - arr[r][1])));
            jumpAverage += jumps.get(r);
        }
        jumpAverage /= jumps.size();

        PriorityQueue<Integer> orderedJumps = new PriorityQueue<>();
        orderedJumps.addAll(jumps);

//        // find the largest and smallest jumps
//        int smallestJump = orderedJumps.peek();
//        PriorityQueue<Integer> copy = new PriorityQueue<>();
//        while (orderedJumps.size() > 1) {
//            copy.add(orderedJumps.poll());
//        }
//        int largestJump = orderedJumps.peek();
//        orderedJumps.addAll(copy);

        // arr.length - 1 == jumps's original size().
        for (int i = 0; i < arr.length - 1; i++) {
            int removedIndex = jumps.indexOf(orderedJumps.peek());
            result = "The difference between " + arr[removedIndex][0] + " and " + arr[removedIndex + 1][0] +
                    " is " + orderedJumps.peek() + "\n" + result;

            // set it to poll() + 1 because we cant remove it and it cant access it again cuz its a PQ.
            jumps.set(removedIndex, orderedJumps.poll() - 1);
        }

        result = "The average differentation between frequency of hashcodes on a perfectly continuous graph" +
                " would be 0. \nThe average differentiation for a random " + analyzedClass.getSimpleName() + " object is " +
                jumpAverage + ".\n\nHere is the raw data of the differentiations, largest first.\n" + result;

        return result;
    }

    /**
     * Constructs and returns a 2d-array. The first row is a
     * @param randoms
     * @return
     */
    public Object[][] heatMap(LinkedList<Object> randoms) {
        Object[][] result = new Object[2][randoms.size()];

        for (int i = 0; i < result[0].length; i ++) {
            result[0][i] = randoms.getContents();
            result[1][i] = randoms.getContents().hashCode();
            randoms = randoms.getNext();
        }
        return result;
    }

    /**
     * Examines a heat map for hot and cold spots and returns a String detailing the 3 three least common
     * hash codes and the 3 most common hash codes.
     *
     * @Precondition:
     * The Heat Map must follow the convention of the heatMap() method's output.
     *
     * @param heatmap the String to be examined, typically what is output by heatMap.
     * @param rows the amount of rows in the heatMap. 52 if hashTable has >= 50 entries.
     * @return a String describing the hot and cold spots in the heatMap.
     */
    public String heatAnalysis(String heatmap, int rows) {
        String result = "The least common hash codes are in the areas of: ";

        ArrayList<Integer> frequencies = new ArrayList<>();
        ArrayList<String> areas = new ArrayList<>();
        // iterate over the rows of heatmap
        for (int i = 0; i < rows; i++) {
            // if heatmap is on its last line. i == rows -1
            if (heatmap.indexOf("\n") < 0) {
                areas.add(heatmap.substring(0, heatmap.indexOf(" ")));
                frequencies.add(heatmap.substring(heatmap.indexOf(" ")).length() - 1);
                break;
            }
            else {
                areas.add(heatmap.substring(0, heatmap.indexOf(" ")));
                if (!heatmap.substring(0, heatmap.indexOf("\n")).contains("x")) {
                    frequencies.add(0);
                }
                else frequencies.add(heatmap.substring(heatmap.indexOf("x"), heatmap.indexOf("\n")).length());
            }

            heatmap = heatmap.substring(heatmap.indexOf("\n") + 1);
        }

        // PriorityQueue with the least frequencies first, thus is "colds".
        PriorityQueue<Integer> colds = new PriorityQueue<>();
        colds.addAll(frequencies);
        PriorityQueue<Integer> coldsCopy = new PriorityQueue<>();

        if (areas.size() < 6) {
            result = "This Object's hashCode() has far too few possible outputs. Only " + frequencies.size() + "!";
            result += "\nHere are the raw frequencies of the hashcodes for " + INSTANCE_AMOUNT + " random objects.\n";

            for (int i = 0; i < areas.size(); i++) {
                result += areas.get(frequencies.indexOf(colds.peek())) + ": " + colds.poll();
            }

        }
        else {
            // first 3 elements of "spots" are cold spots, second 3 are hot spots. same for "names".
            int[] spots = new int[6];
            String[] names = new String[6];
            String popularityList = "Raw frequencies, from least to greatest: \n";

            // Find the cold spots.
            for (int i = 0; i < spots.length / 2; i++) {
                // the index in areas of the front of colds.
                int removedIndex = frequencies.indexOf(colds.peek());
                spots[i] = colds.poll();

                // add the region of the frequency we just added to spots.
                names[i] = areas.get(removedIndex);
                areas.remove(removedIndex);
                frequencies.remove(removedIndex);

                popularityList += names[i] + ": " + spots[i] + "\n";
                result += "\n" + (i + 1) + ". " + names[i] + " occurs " + spots[i] + " times.";
            }

            while (colds.size() > 3) {
                int removedIndex = frequencies.indexOf(colds.peek());
                popularityList += areas.get(removedIndex) + ": " + colds.poll() + "\n";

                areas.remove(removedIndex);
                frequencies.remove(removedIndex);
            }

            result += "\nAnd the most common hash codes are in the areas of: ";
            String mostCommon = "";
            for (int i = spots.length / 2; i < spots.length; i++) {
                int removedIndex = frequencies.indexOf(colds.peek());
                spots[i] = colds.poll();

                names[i] = areas.get(removedIndex);
                areas.remove(removedIndex);
                frequencies.remove(removedIndex);

                popularityList += names[i] + ": " + spots[i];
                if (i != spots.length - 1) popularityList += "\n";
                mostCommon = "\n" + (spots.length - i) + ". " + names[i] + " occurs " + spots[i] +
                        " times." + mostCommon;
            }

            result += mostCommon;
            result += "\n\n" + popularityList;
        }

        return result;
    }

    /**
     * Adds obj to the hashTable (according to the format commented above hashTable's instantiation).
     * Sort of a helper method for hashReport().
     *
     * @param obj the Object to be added to hashTable
     * @return The time (in nanoseconds) it took to hash obj
     */
    public long hashTableAdd(Object obj) {
        long startHash = System.nanoTime();
        // obj's hash code
        int hashed = obj.hashCode();
        long hashTime = System.nanoTime() - startHash;

        // if this is the first HLL being added to hashTable
        if (hashTable.size() == 0) {
            HashLinkedList<Object> adding = new HashLinkedList<>(hashed);
            adding.add(obj);
            hashTable.add(adding);
        }

        else {
            PriorityQueue<HashLinkedList<Object>> copy = new PriorityQueue<>();
            while(hashTable.size() > 0) {
                if ((int)hashTable.peek().getContents() != hashed) {
                    copy.add(hashTable.poll());
                }
                else {
                    hashTable.peek().add(obj);
                    break;
                }
            }

            // if hashed is not yet in hashTable.
            if (hashTable.size() == 0) {
                HashLinkedList<Object> adding = new HashLinkedList<>(hashed);
                adding.add(obj);
                hashTable.add(adding);
            }

            hashTable.addAll(copy);
        }

        return hashTime;
    }

    /**
     * Helper method for heatMap(). Returns n 'x's in one String.
     * I don't feel it is necessary to code this using TDD.
     *
     * @param n amount of 'x's to be printed.
     */
    public static String printX(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += "x";
        }
        return result;
    }

    /**
     * A quazi-main method. Call this on your HashAnalyzer object to instantiate INSTANCE_AMOUNT
     * randomized objects and see the hash report!
     */
    public void analyze() {
        LinkedList<Object> randoms = generateRandomTable(constructors[0], parameterRanges);
        hashReport(randoms);
    }

    // To be used for tests.
    public void setHashTable(PriorityQueue<HashLinkedList<Object>> x) {
        hashTable = x;
    }

    public Constructor<?>[] getConstructors() {
        return constructors;
    }

    public int[] getParameterRanges() {
        return parameterRanges;
    }
}
