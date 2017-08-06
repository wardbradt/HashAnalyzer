import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RandomInstanceGenerator<T> {
    Class<?> cls;
    private Constructor constructor;
    int[] parameterRanges;

    public RandomInstanceGenerator(Class<?> cls, int[] ranges) {
        this.cls = cls;
        constructor = cls.getConstructors()[0];
        this.parameterRanges = ranges;
    }

    public RandomInstance<T> nextRandom() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return new RandomInstance<T>(cls, parameterRanges);
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public static class RandomInstance<T> {
        private T contents;
        private Object[] parameterValues;
        private Constructor constructor;
        private int[] parameterRanges;

        public RandomInstance(Class<?> cls, int[] ranges) throws IllegalAccessException, InstantiationException, InvocationTargetException {
            constructor = cls.getConstructors()[0];
            this.parameterRanges = ranges;
            contents = init();
        }

        private T init() throws IllegalAccessException, InvocationTargetException, InstantiationException {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            parameterValues = fillParameterArray(parameterTypes);

            return (T)constructor.newInstance(parameterValues);
        }

        private Object[] fillParameterArray(Class<?>[] paramTypes) {
            Object[] randomParams = new Object[paramTypes.length];

            for (int b = 0; b < randomParams.length; b++) {
                // Create the random parameter Object.
                randomParams[b] = (new ParamBounds()).generate(parameterRanges[b*2], parameterRanges[b*2+1], paramTypes[b]);
            }
            return randomParams;
        }

        public Object[] getParameterValues() {
            return parameterValues;
        }

        public void setParameterValues(Object[] parameterValues) {
            this.parameterValues = parameterValues;
        }

        public Constructor getConstructor() {
            return constructor;
        }

        public int[] getParameterRanges() {
            return parameterRanges;
        }

        public T getContents() {
            return contents;
        }
    }
}
