import weka.core.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WekaRandomInstance<T> {
    private T contents;
    private Object[] parameterValues;
    private Constructor constructor;
    private int[] parameterRanges;
    private Instance instance;

    public WekaRandomInstance(Class<?> cls, int[] ranges, Instances dataSet) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        constructor = cls.getConstructors()[0];
        this.parameterRanges = ranges;
        instance = new DenseInstance(constructor.getParameterCount() + 1);
        instance.setDataset(dataSet);
        init();
    }

    private void init() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        parameterValues = fillParameterArray(parameterTypes);

        for (int b = 0; b < parameterValues.length; b++) {
            // Create the random parameter Object.
            parameterValues[b] = (new ParamBounds()).generate(parameterRanges[b*2], parameterRanges[b*2+1],
                    parameterTypes[b]);

            Class<?> paramClass = parameterValues[b].getClass();
            if (paramClass.equals(boolean.class)) {
                if ((boolean)parameterValues[b]) {
                    instance.setValue(b, 0);
                } else {
                    instance.setValue(b, 1);
                }
            } else if (paramClass.equals(String.class) || paramClass.equals(char.class)) {
                instance.setValue(b, (String)parameterValues[b]);
            } else {
                instance.setValue(b, (double)(int)parameterValues[b]);
            }
        }

        contents = (T)constructor.newInstance(parameterValues);
        instance.setValue(instance.numAttributes()-1, contents.hashCode());
    }

    /**
     * Randomly generates an <code>Object[]</code> given the <code>Class<?>[]</code> that typically represents the
     * parameter types for a certain constructor.
     *
     * @param paramTypes
     * @return
     */
    private Object[] fillParameterArray(Class<?>[] paramTypes) {
        Object[] randomParams = new Object[paramTypes.length];

        for (int b = 0; b < randomParams.length; b++) {
            // Create the random parameter Object.
            randomParams[b] = (new ParamBounds()).generate(parameterRanges[b*2], parameterRanges[b*2+1],
                    paramTypes[b]);
        }

        return randomParams;
    }

    public Instance getInstance() {
        return instance;
    }

    public T getContents() {
        return contents;
    }
}
