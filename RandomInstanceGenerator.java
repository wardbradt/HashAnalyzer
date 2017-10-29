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

}
