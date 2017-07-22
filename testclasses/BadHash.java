package testclasses;

/**
 * Created by Ward Bradt on 5/8/17.
 * An example class that demonstrates a bad hashCode() function
 */
public class BadHash {
    private int num;
    private String str;

    public BadHash(int n, String s) {
        num = n;
        str = s;
    }

    public int hashCode() {
        int sum = 0;
        sum += str.length() / str.length();
        sum += num;
        // returns num + 1 or num if str.length() == 0
        return sum;
    }
}
