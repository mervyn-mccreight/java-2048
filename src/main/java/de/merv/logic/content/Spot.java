package de.merv.logic.content;

import java.util.Objects;

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

    public boolean isEmpty() {
        return this.equals(Spot.EMPTY);
    }

    public Spot improve() {
        return new Spot(this.value * 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spot spot = (Spot) o;
        return value == spot.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Spot{" +
                "value=" + "[" + print() + "]" +
                '}';
    }

    public int value() {
        return value;
    }
}
