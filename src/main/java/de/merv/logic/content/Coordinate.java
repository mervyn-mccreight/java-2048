package de.merv.logic.content;

import org.immutables.value.Value;

@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE)
@Value.Immutable(builder = false, copy = false)
public abstract class Coordinate {

    @Value.Parameter
    public abstract int x();

    @Value.Parameter
    public abstract int y();

    public static Coordinate of(int x, int y) {
        return ImmutableCoordinate.of(x, y);
    }
}
