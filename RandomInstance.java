import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RandomInstance<T> {
    private T contents;
    private Object[] parameterValues;
    private Constructor constructor;

    public RandomInstance(Class<?> cls, int[] ranges) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        this(cls.getConstructors()[0], fillParameterArray(cls.getConstructors()[0].getParameterTypes(), ranges));
    }

    public RandomInstance(Constructor<?> constructor, Object[] parameterValues) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.constructor = constructor;
        this.parameterValues = parameterValues;
        init();
    }

    private void init() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        contents = (T)constructor.newInstance(parameterValues);
    }

    private static Object[] fillParameterArray(Class<?>[] paramTypes, int[] parameterRanges) {
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

    public T getContents() {
        return contents;
    }
}
