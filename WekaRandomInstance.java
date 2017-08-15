import weka.core.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// todo later make this a subclass of DenseInstance or AbstractInstance instead of having an Instance variable
public class WekaRandomInstance<T> {
    private T contents;
    private Object[] parameterValues;
    private Constructor<?> constructor;
    private Instance instance;

    public WekaRandomInstance(Class<?> cls, int[] ranges, Instances dataSet) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        this(cls.getConstructors()[0], fillParameterArray(cls.getConstructors()[0].getParameterTypes(), ranges), dataSet);
    }

    public WekaRandomInstance(Constructor<?> constructor, Object[] parameterValues, Instances dataSet) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.constructor = constructor;
        this.parameterValues = parameterValues;
        instance = new DenseInstance(constructor.getParameterCount() + 1);
        instance.setDataset(dataSet);
        init();
    }

    private void init() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (int b = 0; b < parameterValues.length; b++) {
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
                instance.setValue(b, ((Number)parameterValues[b]).doubleValue());
            }
        }

        contents = (T)constructor.newInstance(parameterValues);
        // the last value in the dataset is the hashcode for this instance
        instance.setValue(instance.numAttributes()-1, contents.hashCode());
    }

    /**
     * Randomly generates an <code>Object[]</code> given the <code>Class<?>[]</code> that typically represents the
     * parameter types for a certain constructor.
     *
     * @param paramTypes
     * @return
     */
    public static Object[] fillParameterArray(Class<?>[] paramTypes, int[] parameterRanges) {
        Object[] randomParams = new Object[paramTypes.length];

        for (int b = 0; b < randomParams.length; b++) {
            // Create the random parameter Object.
            randomParams[b] = (new ParamBounds()).generate(parameterRanges[b*2], parameterRanges[b*2+1],
                    paramTypes[b]);
        }

        return randomParams;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public Instance getInstance() {
        return instance;
    }

    public T getContents() {
        return contents;
    }
}
