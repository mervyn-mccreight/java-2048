package de.merv.logic.content;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.Set;
import javaslang.collection.SortedMap;
import javaslang.collection.Stream;
import javaslang.collection.TreeMap;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Random;

public class Board {
    private static final Comparator<Coordinate> SORTING_COMPARATOR = Comparator
            .comparingInt(Coordinate::x)
            .thenComparingInt(Coordinate::y);
    private final SortedMap<Coordinate, Spot> state;

    private Board(SortedMap<Coordinate, Spot> state) {
        this.state = state;
    }

    public static Board empty(int dimension) {
        Map<Coordinate, Spot> state = Stream.from(0).take(dimension).crossProduct(2).toMap(
                pair -> Tuple.of(new Coordinate(pair.get(0), pair.get(1)), Spot.EMPTY)
        );

        SortedMap<Coordinate, Spot> sortedStateMap = TreeMap.empty(SORTING_COMPARATOR);

        return new Board(sortedStateMap.merge(state));
    }

    private List<Spot> row(int y) {
        return state
                .filter(entry -> entry._1.y() == y)
                .map(Tuple2::_2)
                .toList();
    }

    private int dimension() {
        Option<Coordinate> xDimension = state.keySet().maxBy(Comparator.comparingInt(Coordinate::x));
        Option<Coordinate> yDimension = state.keySet().maxBy(Comparator.comparingInt(Coordinate::y));

        if (xDimension.getOrElseThrow(RuntimeException::new).x() != yDimension.getOrElseThrow(RuntimeException::new).y()) {
            throw new RuntimeException("not equally dimensioned board.");
        }

        return yDimension.get().y() + 1;
    }

    private List<Spot> column(int x) {
        return state
                .filter(entry -> entry._1.x() == x)
                .map(Tuple2::_2)
                .toList();
    }

    public Set<Tuple2<Coordinate, Spot>> empties() {
        return state
                .filter(entry -> entry._2().isEmpty())
                .toSet();
    }

    public Board left() {
        Stream<Tuple2<Coordinate, Spot>> newContent = Stream.from(0).take(dimension()).flatMap(
                y -> {
                    List<Spot> result = collapse(row(y));

                    List<Coordinate> coordinates = Stream.from(0).take(result.size()).map(
                            x -> new Coordinate(x, y)
                    ).toList();

                    return coordinates.zip(result);
                }
        );

        return new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newContent));
    }

    public Board right() {
        Stream<Tuple2<Coordinate, Spot>> newContent = Stream.from(0).take(dimension()).flatMap(
                y -> {
                    List<Spot> result = collapse(row(y).reverse()).reverse();

                    List<Coordinate> coordinates = Stream.from(0).take(result.size()).map(
                            x -> new Coordinate(x, y)
                    ).toList();

                    return coordinates.zip(result);
                }
        );

        return new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newContent));
    }

    public Board up() {
        Stream<Tuple2<Coordinate, Spot>> newContent = Stream.from(0).take(dimension()).flatMap(
                x -> {
                    List<Spot> result = collapse(column(x));

                    List<Coordinate> coordinates = Stream.from(0).take(result.size()).map(
                            y -> new Coordinate(x, y)
                    ).toList();

                    return coordinates.zip(result);
                }
        );

        return new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newContent));
    }

    public Board down() {
        Stream<Tuple2<Coordinate, Spot>> newContent = Stream.from(0).take(dimension()).flatMap(
                x -> {
                    List<Spot> result = collapse(column(x).reverse()).reverse();

                    List<Coordinate> coordinates = Stream.from(0).take(result.size()).map(
                            y -> new Coordinate(x, y)
                    ).toList();

                    return coordinates.zip(result);
                }
        );

        return new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newContent));
    }

    private List<Spot> collapse(List<Spot> toCollapse) {
        List<Spot> collapsed = toCollapse
                .filter(s -> !s.equals(Spot.EMPTY))
                .foldLeft(List.empty(),
                        (a, l) -> {
                            if (a.isEmpty()) {
                                return a.append(l);
                            }

                            if (a.last().equals(l)) {
                                return a.init().append(l.improve());
                            }

                            return a.append(l);
                        }
                );

        return Stream.ofAll(collapsed)
                .extend(Spot.EMPTY)
                .take(toCollapse.length())
                .toList();
    }

    public List<String> printableRows() {
        return Stream.from(0).take(dimension()).map(
                i -> row(i)
                        .map(Spot::print).intersperse("|")
                        .foldLeft("", (a, l) -> a + l)
        ).toList();
    }

    public Board add(Random random) {
        Set<Tuple2<Coordinate, Spot>> empties = empties();
        Coordinate randomCoordinate = empties.map(Tuple2::_1).toList().get(random.nextInt(empties.length()));
        Spot randomSpot = state.get(randomCoordinate).getOrElseThrow(RuntimeException::new);

        return new Board(state.replace(Tuple.of(randomCoordinate, randomSpot),
                Tuple.of(randomCoordinate, randomSpot.improve())));
    }
}
