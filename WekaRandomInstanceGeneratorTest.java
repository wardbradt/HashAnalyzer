import org.junit.Test;
import testclasses.TiredStudent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class WekaRandomInstanceGeneratorTest {
    @Test
    public void testNextRandom() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        WekaRandomInstanceGenerator<Integer> test = new WekaRandomInstanceGenerator<>(TiredStudent.class, new int[]{5,12,10,50});
        for (int i = 0; i < 1024; i++) {
            test.nextRandom();
        }
        test.instancesToArff();
    }

}