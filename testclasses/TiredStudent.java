package testclasses;

/**
 * Created by wardbradt on 5/8/17.
 */
public class TiredStudent {
    String name;
    int tiredness;

    public TiredStudent(String n, int t) {
        name = n;
        tiredness = t;
    }

    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < name.length(); i++) {
            sum += (int)name.charAt(i);
        }
        return sum % 13 + tiredness;
    }
}
