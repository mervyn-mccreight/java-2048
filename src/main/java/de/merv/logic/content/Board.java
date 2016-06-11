package de.merv.logic.content;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Iterator;
import javaslang.collection.List;
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
        Iterator<Tuple2<Coordinate, Spot>> emptySpots = Stream.from(0).take(dimension).crossProduct(2).map(
                pair -> Tuple.of(new Coordinate(pair.get(0), pair.get(1)), Spot.EMPTY)
        );
        return new Board(TreeMap.ofEntries(SORTING_COMPARATOR, emptySpots));
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

    public Tuple2<Integer, Board> left() {
        Stream<Tuple2<Integer, List<Tuple2<Coordinate, Spot>>>> withCoordinates = Stream.from(0).take(dimension()).map(
                y -> {
                    Tuple2<Integer, List<Spot>> result = collapse(row(y));

                    List<Coordinate> coordinates = Stream.from(0).take(result._2().size()).map(
                            x -> new Coordinate(x, y)
                    ).toList();

                    return Tuple.of(result._1(), coordinates.zip(result._2()));
                }
        );

        Stream<Tuple2<Coordinate, Spot>> newSpots = withCoordinates.flatMap(Tuple2::_2);
        int points = withCoordinates.map(Tuple2::_1).sum().intValue();

        return Tuple.of(points, new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newSpots)));
    }

    public Tuple2<Integer, Board> right() {
        Stream<Tuple2<Integer, List<Tuple2<Coordinate, Spot>>>> withCoordinates = Stream.from(0).take(dimension()).map(
                y -> {
                    Tuple2<Integer, List<Spot>> result = collapse(row(y).reverse());
                    result = result.map(i -> i, List::reverse);

                    List<Coordinate> coordinates = Stream.from(0).take(result._2().size()).map(
                            x -> new Coordinate(x, y)
                    ).toList();

                    return Tuple.of(result._1(), coordinates.zip(result._2()));
                }
        );

        Stream<Tuple2<Coordinate, Spot>> newSpots = withCoordinates.flatMap(Tuple2::_2);
        int points = withCoordinates.map(Tuple2::_1).sum().intValue();

        return Tuple.of(points, new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newSpots)));
    }

    public Tuple2<Integer, Board> up() {
        Stream<Tuple2<Integer, List<Tuple2<Coordinate, Spot>>>> withCoordinates = Stream.from(0).take(dimension()).map(
                x -> {
                    Tuple2<Integer, List<Spot>> result = collapse(column(x));

                    List<Coordinate> coordinates = Stream.from(0).take(result._2().size()).map(
                            y -> new Coordinate(x, y)
                    ).toList();

                    return Tuple.of(result._1(), coordinates.zip(result._2()));
                }
        );

        Stream<Tuple2<Coordinate, Spot>> newSpots = withCoordinates.flatMap(Tuple2::_2);
        int points = withCoordinates.map(Tuple2::_1).sum().intValue();

        return Tuple.of(points, new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newSpots)));
    }

    public Tuple2<Integer, Board> down() {
        Stream<Tuple2<Integer, List<Tuple2<Coordinate, Spot>>>> withCoordinates = Stream.from(0).take(dimension()).map(
                x -> {
                    Tuple2<Integer, List<Spot>> result = collapse(column(x).reverse());
                    result = result.map(i -> i, List::reverse);

                    List<Coordinate> coordinates = Stream.from(0).take(result._2().size()).map(
                            y -> new Coordinate(x, y)
                    ).toList();

                    return Tuple.of(result._1(), coordinates.zip(result._2()));
                }
        );

        Stream<Tuple2<Coordinate, Spot>> newSpots = withCoordinates.flatMap(Tuple2::_2);
        int points = withCoordinates.map(Tuple2::_1).sum().intValue();

        return Tuple.of(points, new Board(TreeMap.ofEntries(SORTING_COMPARATOR, newSpots)));
    }

    private Tuple2<Integer, List<Spot>> collapse(List<Spot> toCollapse) {
        Tuple2<Integer, List<Spot>> collapsed = toCollapse
                .filter(s -> !s.equals(Spot.EMPTY))
                .foldLeft(Tuple.of(0, List.empty()),
                        (a, l) -> {
                            if (a._2().isEmpty()) {
                                return Tuple.of(a._1(), a._2().append(l));
                            }

                            if (a._2().last().equals(l)) {
                                return Tuple.of(a._1() + a._2().last().value() + l.value(), a._2().init().append(l.improve()));
                            }

                            return Tuple.of(a._1(), a._2().append(l));
                        }
                );

        return Tuple.of(collapsed._1(), Stream.ofAll(collapsed._2())
                .extend(Spot.EMPTY)
                .take(toCollapse.length())
                .toList());
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
