import java.lang.reflect.*;
import java.util.Random;

/**
 * Sources used:
 * https://stackoverflow.com/questions/709961/determining-if-an-object-is-of-primitive-type
 * https://stackoverflow.com/questions/4936819/java-check-if-enum-contains-a-given-string
 * https://stackoverflow.com/questions/10189016/generate-short-random-number-in-java
 */
public class RandomGenerator {

    public enum BASE_TYPE {
        INTEGER (int.class, Integer.class),
        DOUBLE (double.class, Double.class),
        FLOAT (float.class, Float.class),
        LONG (long.class, Long.class),
        SHORT (short.class, Short.class),
        BYTE (byte.class, Byte.class),
        BOOLEAN (boolean.class, Boolean.class),
        CHAR (char.class, Character.class);

        private final Class<?> primitiveClass;
        private final Class<?> wrapperClass;

        BASE_TYPE(Class<?> primitive, Class<?> wrapper) {
            this.primitiveClass = primitive;
            this.wrapperClass = wrapper;
        }

        public Class<?> getPrimitiveClass() {
            return primitiveClass;
        }

        public Class<?> getWrapperClass() {
            return wrapperClass;
        }
    }

    public enum BASE_TYPE_ARRAY {
        INTEGER (int[].class, Integer[].class, BASE_TYPE.INTEGER),
        DOUBLE (double[].class, Double[].class, BASE_TYPE.DOUBLE),
        FLOAT (float[].class, Float[].class, BASE_TYPE.FLOAT),
        LONG (long[].class, Long[].class, BASE_TYPE.LONG),
        SHORT (short[].class, Short[].class, BASE_TYPE.SHORT),
        BYTE (byte[].class, Byte[].class, BASE_TYPE.BYTE),
        BOOLEAN (boolean[].class, Boolean[].class, BASE_TYPE.BOOLEAN),
        CHAR (char[].class, Character[].class, BASE_TYPE.CHAR);

        private final Class<?> primitiveArrayClass;
        private final Class<?> wrapperArrayClass;
        private final BASE_TYPE baseType;

        BASE_TYPE_ARRAY(Class<?> primitiveArray, Class<?> wrapperArray, BASE_TYPE b) {
            this.primitiveArrayClass = primitiveArray;
            this.wrapperArrayClass = wrapperArray;
            this.baseType = b;
        }

        public Class<?> getPrimitiveClass() {
            return baseType.getPrimitiveClass();
        }

        public Class<?> getPrimitiveArrayClass() {
            return primitiveArrayClass;
        }

        public Class<?> getWrapperClass() {
            return baseType.getWrapperClass();
        }

        public Class<?> getWrapperArrayClass() {
            return wrapperArrayClass;
        }

        public BASE_TYPE getBaseType() {
            return baseType;
        }
    }

//    public static final List<Class<?>> WRAPPER_TYPES = new ArrayList<Class<?>>() {{
//        for (BASE_TYPE b : BASE_TYPE.values()) {
//            add(b.getWrapperClass());
//        }
//    }};
//
//    public static final List<Class<?>> WRAPPER_ARRAY_TYPES = new ArrayList<Class<?>>() {{
//        for (BASE_TYPE_ARRAY b : BASE_TYPE_ARRAY.values()) {
//            add(b.getWrapperArrayClass());
//        }
//    }};
//
//    public static final List<Class<?>> PRIMITIVE_TYPES = new ArrayList<Class<?>>() {{
//        for (BASE_TYPE b : BASE_TYPE.values()) {
//            add(b.getPrimitiveClass());
//        }
//    }};
//
//    public static final List<Class<?>> PRIMITIVE_ARRAY_TYPES = new ArrayList<Class<?>>() {{
//        for (BASE_TYPE_ARRAY b : BASE_TYPE_ARRAY.values()) {
//            add(b.getPrimitiveArrayClass());
//        }
//    }};

    public static Object nextRandom(Class<?> cls) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        // base case: primitive array or wrapper array
        if (cls.isArray()) {
            for (BASE_TYPE_ARRAY i : BASE_TYPE_ARRAY.values()) {
                if (cls.equals(i.getPrimitiveArrayClass()) || cls.equals(i.getWrapperArrayClass())) {
                    // later: random wrapper of cls if primitive
                    // later: is slightly inefficient because we iterate over BTA.values() than iterate in
                    // randomBase using a switch statement.
                    return randomBaseArray(i);
                }
            }
        }
        // base case: if primitive or wrapper
        else {
            for (BASE_TYPE i : BASE_TYPE.values()) {
                if (cls.equals(i.getPrimitiveClass()) || cls.equals(i.getWrapperClass())) {
                    // later: random wrapper array of cls
                    return randomBase(i);
                }
            }
        }

        Constructor<?> constructor = cls.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameterValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterValues.length; i++) {
            // Recursively creates objects/ parameters for constructor
            parameterValues[i] = nextRandom(parameterTypes[i]);
            System.out.println(parameterValues[i].getClass());
        }

        return constructor.newInstance(parameterValues);
    }

    public static Object randomBase(BASE_TYPE baseType) {
        Random rand = new Random();
        switch (baseType) {
            case BYTE:
                return randomByte();
            case CHAR:
                return randomChar();
            case LONG:
                return rand.nextLong();
            case FLOAT:
                return rand.nextFloat();
            case DOUBLE:
                return rand.nextDouble();
            case SHORT:
                return randomShort();
            case BOOLEAN:
                return rand.nextBoolean();
            case INTEGER:
                return rand.nextInt();
        }
        throw new IllegalArgumentException();
    }

    public static Object randomBaseArray(BASE_TYPE_ARRAY baseTypeArray) {
        short srt = (short) (1 + new Random().nextInt(Short.MAX_VALUE));
        Object arr = Array.newInstance(baseTypeArray.getPrimitiveClass(), srt);

        for (int i = 0; i < Array.getLength(arr); i++) {
            Array.set(arr, i, randomBase(baseTypeArray.getBaseType()));
        }
        return arr;
    }

    public static Byte randomByte() {
        byte[] b = new byte[1];
        new Random().nextBytes(b);
        return b[0];
    }

    public static Character randomChar() {
        return (char)(new Random().nextInt(85) + 32);
    }

    public static Short randomShort() {
        return (short) (Short.MIN_VALUE + new Random().nextInt(Short.MAX_VALUE * 2 + 2));
    }

//    public static boolean isWrapperArray(Class<?> cls) {
//        // later: does this next line save time or should i delete it?
//        if (!cls.isArray()) return false;
//        return WRAPPER_ARRAY_TYPES.contains(cls);
//    }
//
//    public static boolean isWrapperArray(Object[] arr) {
//        return isWrapperArray(arr.getClass());
//    }
//
//    public static boolean isPrimitiveArray(Class<?> cls) {
//        if (!cls.isArray()) return false;
//        return PRIMITIVE_ARRAY_TYPES.contains(cls);
//    }
//
//    /**
//     * @return if a <code>Class<?></code> is a BASE_TYPE
//     */
//    public static boolean isBasic(Class<?> cls) {
//        return isWrapper(cls) || isPrimitive(cls) || isWrapperArray(cls) || isPrimitiveArray(cls);
//    }
//
//    public static boolean isWrapper(Object o) {
//        return isWrapper(o.getClass());
//    }
//
//    public static boolean isWrapper(Class<?> cls) {
//        return WRAPPER_TYPES.contains(cls);
//    }
//
//    public static boolean isPrimitive(Class<?> cls) {
//        return PRIMITIVE_TYPES.contains(cls);
//    }
    
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        // I don't fully know how to use @SuppressWarnings yet, so most of my methods throw these exceptions^
        String str = (String)nextRandom(String.class);
    }
}
