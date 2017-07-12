// import java.lang.Math.*;
import java.util.Random;

/**
 * Generate a random instance of an Object
 */
public class ParamBounds {

  // Max is exclusive.
  public static <S extends Number, T> T generate(S min, S max, Class<T> c) throws IllegalArgumentException {
    double range = max.doubleValue()-min.doubleValue();
    if (c.equals(String.class)) {
      return (T)(Object)randomStringOfLength(min.intValue(), max.intValue());
    } else if (c.equals(char.class)) {
      return (T)(Object)(char)(int) (Math.random()*127);
    } else if (c.equals(int.class)) {
      return (T)(Object)(int)((Math.random() * range) + min.doubleValue());
    } else if (c.equals(double.class)) {
      return (T)(Object)(double)((Math.random() * range) + min.doubleValue());
    } else if (c.equals(float.class)) {
      return (T)(Object)(float)((Math.random() * range) + min.doubleValue());
    } else if (c.equals(short.class)) {
      return (T)(Object)(short)((Math.random() * range) + min.doubleValue());
    } else if (c.equals(long.class)) {
      return (T)(Object)(long)((Math.random() * range) + min.doubleValue());
    } else if (c.equals(byte.class)) {
      // create byte array
      byte[] nbyte = new byte[1];
      // put the next byte in the array
      new Random().nextBytes(nbyte);
      // check the value of array
      return (T)(Object)nbyte[0];
    } else if (c.equals(boolean.class)) {
      return (T)(Object)new Random().nextBoolean();
    }
    throw new IllegalArgumentException();
  }

  private static String randomStringOfLength(int min, int max) {
    // A random length in the range from min to max
    int randomLength = (int) Math.random() * max + min;
    // The String to be returned
    String result = "";
    for (int i = 0; i < randomLength; i++) {
      // Generate a random ascii character by casting an int
      // in the ascii range 26 - 33.
      result += (char)(int) ((Math.random() * 126) + 33);
    }
    return result;
  }

  public static void main(String[] args) {
    System.out.println(ParamBounds.generate(2, 10, int.class));
    System.out.println(ParamBounds.generate(1.2, 3.2, double.class));
    System.out.println(ParamBounds.generate(8, 15, String.class));
    System.out.println(ParamBounds.generate(0, 0, byte.class));
  }

}
