import org.junit.Test;
import org.junit.Before;

import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.*;

public class StandardHasherTest {
  public HandsomeJimmy jimmy;
  
  @Before 
  public void initialize() {
	  jimmy = new HandsomeJimmy();
   }
  
  @Test
  public void testHashCode() {
	  assertEquals(jimmy.getHandsomeness(), 100);
	  assertEquals(jimmy.hashCode(), 131);
  }

    @Test
    public void testLL() {
        LinkedList<Integer> test = new LinkedList<>(4);
        System.out.println(test instanceof ParameterizedType);
    }
}
