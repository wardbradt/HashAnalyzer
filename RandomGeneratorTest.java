import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class RandomGeneratorTest {
    @Test
    public void testRandomBase() {
        for (RecursiveRandomGenerator.BASE_TYPE b : RecursiveRandomGenerator.BASE_TYPE.values()) {
            assertEquals(b.getWrapperClass(), RecursiveRandomGenerator.randomBase(b).getClass());
        }
    }

    @Test
    public void testNextRandom() throws IllegalAccessException, InstantiationException, InvocationTargetException {
//        String str = (String)RecursiveRandomGenerator.nextRandom(String.class);
//        DumbClass dc = new DumbClass(0, 2.0, new int[]{1,2,3});
//        DumbClass foo = (DumbClass)RecursiveRandomGenerator.nextRandom(DumbClass.class);
        String str = (String) RecursiveRandomGenerator.nextRandom(String.class);
    }
}