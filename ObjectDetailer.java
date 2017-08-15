import java.lang.reflect.*;

public class ObjectDetailer {
    private Class<?> cls;
    // the values of the parameters used to construct the object
    private Object[] parameterValues;
    private int hashCode;

    public ObjectDetailer(Class<?> cls, Object[] parameterValues, int hashCode) {
        this.parameterValues = parameterValues;
        this.hashCode = hashCode;
        this.cls = cls;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public int getHashCode() {
        return hashCode;
    }

    public Class<?> getCls() {
        return cls;
    }

    public Constructor<?> getConstructor() throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class<?>[parameterValues.length];
        // parameterTypes.length == parameterValues.length
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = parameterValues[i].getClass();
        }
        return cls.getConstructor(parameterTypes);
    }
}
