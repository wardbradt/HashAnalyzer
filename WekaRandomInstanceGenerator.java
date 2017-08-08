import weka.core.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Sources used:
 * Weka API
 * https://stackoverflow.com/questions/12151702/weka-core-unassigneddatasetexception-when-creating-an-unlabeled-instance
 * https://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
 * @param <T> any <code>class</code> with a <code>hashcode()</code> function that returns an <code>int</code>
 */
public class WekaRandomInstanceGenerator<T> {
    Class<?> cls;
    private Constructor constructor;
    private int[] parameterRanges;
    private Instances data;
    private ArrayList<Attribute> attributes;
    public final static ArrayList<String> BOOLEAN_NAMES = new ArrayList<>(Arrays.asList("true", "false"));

    public WekaRandomInstanceGenerator(Class<?> cls, int[] ranges) {
        this.cls = cls;
        constructor = cls.getConstructors()[0];
        this.parameterRanges = ranges;

        // initialize attributes and data
        attributes = createAttributes(constructor.getParameterTypes());
        data = new Instances("MyRelation", attributes, 0);
        // todo later: is this line necessary?
        data.setClassIndex(data.numAttributes() - 1);
    }

    /**
     * Creates and returns a new <code>WekaRandomInstance</code>, adds it to <code>data</code>, and returns it
     * @return a <code>WekaRandomInstance<T></code>
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public WekaRandomInstance<T> nextRandom() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        WekaRandomInstance<T> added = new WekaRandomInstance<T>(cls, parameterRanges, data);
        data.add(added.getInstance());
        return added;
    }

    /**
     * Generates an <code>ArrayList<Attribute></code> for the parameters of a constructor.
     * @param arr an <code>Class<?>[]</code> representing the classes of the Objects in a constructor's parameter list
     *
     * @return
     */
    public static ArrayList<Attribute> createAttributes(Class<?>[] arr) {
        ArrayList<Attribute> result = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            // boolean == only primitive nominal attribute
            if (arr[i].equals(boolean.class)) {
                result.add(new Attribute("att" + i, BOOLEAN_NAMES));
            }
            // char and string == string attribute
            else if (arr[i].equals(String.class) || arr[i].equals(char.class)) {
                result.add(new Attribute("att" + i, true));
            }
            // int, double, float, long, short, or byte == numeric attribute
            else {
                result.add(new Attribute("att" + i));
            }
        }
        result.add(new Attribute("hashcode"));

        return result;
    }

    public void instancesToArff() throws IOException {
        instancesToArff("test");
    }

    public void instancesToArff(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName +".arff"));
        writer.write(data.toString());
        writer.flush();
        writer.close();
    }

    public void addToData(Instance inst) {
        data.add(inst);
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public Instances getData() {
        return data;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void clearData() {
        data = new Instances("MyRelation", attributes, 0);
        data.setClassIndex(data.numAttributes() - 1);
    }
}
