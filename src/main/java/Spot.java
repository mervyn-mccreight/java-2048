public class Spot {
    public static Spot EMPTY = new Spot(1);
    private final int value;

    public Spot(int value) {
        this.value = value;
    }

    public String print() {
        if (EMPTY.equals(this)) {
            return " ";
        }
        return String.valueOf(value);
    }

    public Spot improve() {
        return new Spot(this.value * 2);
    }
}
