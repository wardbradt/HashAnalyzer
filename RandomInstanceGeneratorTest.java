import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class RandomInstanceGeneratorTest {
    @Test
    public void testNextRandom() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        RandomInstanceGenerator<Integer> test = new RandomInstanceGenerator<>(Integer.class, new int[]{0,20});
        for (int i = 0; i < 10; i++) {
            assertTrue(test.nextRandom().getContents() < 20);
        }
    }
}