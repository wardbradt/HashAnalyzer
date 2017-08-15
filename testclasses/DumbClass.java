package testclasses;

public class DumbClass {
    private int x;
    private int y;
    private int z;
    public DumbClass(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int hashCode() {
//        return (31 + x * 5 + (int)(y * 0.43) + z) % 60;
        return 12 + 3 * x + y + z % 20;
    }
}
