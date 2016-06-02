public class Spot {
    public static Spot EMPTY = new Spot(1);
    private final int value;

    public Spot(int value) {
        this.value = value;
    }
}
