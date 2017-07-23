import org.junit.Test;
import testclasses.DumbClass;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class RandomGeneratorTest {
    @Test
    public void testRandomBase() {
        for (RandomGenerator.BASE_TYPE b : RandomGenerator.BASE_TYPE.values()) {
            assertEquals(b.getWrapperClass(), RandomGenerator.randomBase(b).getClass());
        }
    }

    @Test
    public void testNextRandom() throws IllegalAccessException, InstantiationException, InvocationTargetException {
//        String str = (String)RandomGenerator.nextRandom(String.class);
//        DumbClass dc = new DumbClass(0, 2.0, new int[]{1,2,3});
//        DumbClass foo = (DumbClass)RandomGenerator.nextRandom(DumbClass.class);
        String str = (String)RandomGenerator.nextRandom(String.class);
    }
}