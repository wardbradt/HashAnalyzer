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
//    private Class<?> analyzedClass;

    public HashAnalyzerPro(Class c, int[] r) {
        table = new PriorityHashTable<>();
        constructor = c.getConstructors()[0];
        parameterRanges = r;
    }

    public void hashReport() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("The average hashCode() runtime of an arbitrary " +
                constructor.getDeclaringClass().getSimpleName()  + " " +
                "object is " + populateTable() + " nanoseconds.\n");
        System.out.println(heatMap());
//        int rows = 1;
//        String tempHeat = heatMap;
//        while (tempHeat.indexOf("\n") > -1) {
//            rows++;
//            tempHeat = tempHeat.substring(tempHeat.indexOf("\n") + 1);
//        }
    }

    public String heatMap(int rows) {
        String result = "";
        HeatMap<T> heatMap = new HeatMap<>(table);
        HashMap<Integer, Integer> rawHeatMap = heatMap.getHeatMap();

        result += "Raw frequencies of hash codes: \n";
        Iterator<Map.Entry<Integer, Integer>> it = rawHeatMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = it.next();
            result += pair.getKey() + ": " + pair.getValue() + "\n";
        }

        result += "Heat Map fit to " + rows + " rows: \n";
        HashMap<Integer, Integer> averageHeatMap = HeatMap.averageHeatMap(heatMap, rows);
        it = averageHeatMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = it.next();
            result += pair.getKey() + ": " + printX(pair.getValue()) + "\n";
        }

        return result;
    }

    public String heatMap() {
        return heatMap(50);
    }

    /**
     * Populates <code>PriorityHashTable<T> table</code> with random instantiations using the
     * <code>generateRandomInstance(Class<?>[] paramTypes)</code> helper method.
     * @return the average time it takes to hash a random instantiation of the given <code>class</code>
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
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
        // Todo: How to see if a variable is an int/double/boolean/ etc.
        // Todo: make recursive
        return generateRandomInstance(fillParameterArray(paramTypes));
    }

    public T generateRandomInstance(Object[] randomParams) throws InstantiationException,
            IllegalAccessException, InvocationTargetException{
        return (T)constructor.newInstance(randomParams);
    }

    /**
     * Creates an <code>Object[]</code> of randomly instantiated <code>Object</code>s given a <code>Class<?>[]</code>.
     *
     * @param paramTypes
     * @return
     */
    private Object[] fillParameterArray(Class<?>[] paramTypes) {
        Object[] randomParams = new Object[paramTypes.length];
        for (int b = 0; b < randomParams.length; b++) {
            // Create the random parameter Object.
            randomParams[b] = (new ParamBounds()).generate(parameterRanges[b*2], parameterRanges[b*2+1], paramTypes[b]);
        }
        return randomParams;
    }

    /**
     * Analyzes the avalanche effect of the given <code>class</code> by generating random instances then
     * slightly "altering" (in a way that makes sense, typically by minutely changing the bits) each parameter
     * Todo: make sure bits are changed only +1 and -1
     *
     * @return a String describing an an analysis of the given class's avalanche effect or lack thereof
     */
    public String avalancheEffectAnalysis() throws IllegalAccessException, InvocationTargetException,
            InstantiationException {
        String result = "";

        for (int a = 0; a < INSTANCE_AMOUNT; a++) {
            // create the random parameters we will use to instantiate on this iteration
            Object[] randomParams = fillParameterArray(constructor.getParameterTypes());
            // instantiate a T Object using randomParams as the parameters for the constructor
            T randomInstance = generateRandomInstance(randomParams);

            int originalHash = randomInstance.hashCode();
            Object[] paramsCopy = randomParams;
            Object objCopy = new Object();
            int prev = originalHash;

            // Iterate over the parameters that were used to construct/ initialize randomInstance
            for (int b = 0; b < randomParams.length; b++) {
                // an array of Objects that are slightly different from randomParams[b] (or each random parameter)
                Object[] alteredParameters = differentiateParameter(randomParams[b]);
                int[] sums = new int[randomParams.length];

                // Iterate over the differentiated parameters
                for (int c = 0; c < alteredParameters.length; c++) {
                    paramsCopy[b] = alteredParameters[c];
                }
            }
        }
        return result;
    }

    /**
     * Returns an array of objects that are slightly differentiated/ altered from obj.
     * Todo: Make recursive to instantiate any object until you get to its primitive constructor(s)
     * Todo: Revise this method if you don't end up making it recursive
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

    public String continuityAnalysis() {
        String result = "";
        return result;
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
