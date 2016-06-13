package de.merv.logic.content;

import org.immutables.value.Value;

@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE)
@Value.Immutable(builder = false, copy = false)
public abstract class Spot {
    public static Spot EMPTY = Spot.of(1);

    @Value.Parameter
    public abstract int value();

    public static Spot of(int x) {
        return ImmutableSpot.of(x);
    }

    public String print() {
        if (EMPTY.equals(this)) {
            return " ";
        }
        return String.valueOf(value());
    }

    public boolean isEmpty() {
        return this.equals(Spot.EMPTY);
    }

    public Spot improve() {
        return Spot.of(value() * 2);
    }
}
