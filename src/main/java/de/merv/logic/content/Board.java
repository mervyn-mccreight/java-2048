package de.merv.logic.content;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.SortedMap;
import javaslang.collection.Stream;
import javaslang.collection.TreeMap;

import java.util.Comparator;

public class Board {
    private final Map<Coordinate, Spot> state;

    private Board(SortedMap<Coordinate, Spot> state) {
        this.state = state;
    }

    public static Board empty(int dimension) {
        Map<Coordinate, Spot> state = Stream.from(0).take(dimension).crossProduct(2).toMap(
                pair -> Tuple.of(new Coordinate(pair.get(0), pair.get(1)), Spot.EMPTY)
        );

        SortedMap<Coordinate, Spot> emptyState = TreeMap.empty(
                Comparator
                        .comparingInt(Coordinate::x)
                        .thenComparingInt(Coordinate::y)
        );

        return new Board(emptyState.merge(state));
    }

    private List<Spot> row(int y) {
        return state
                .filter(entry -> entry._1.y() == y)
                .map(Tuple2::_2)
                .toList();
    }

    private List<Spot> column(int x) {
        return state
                .filter(entry -> entry._1.x() == x)
                .map(Tuple2::_2)
                .toList();
    }

    public int empties() {
        return state.values().count(Spot::isEmpty);
    }
}
