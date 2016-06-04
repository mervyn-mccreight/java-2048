package logic;

import javaslang.Tuple;
import javaslang.collection.List;
import javaslang.collection.Stream;
import logic.content.Coordinate;
import logic.content.Spot;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static java.util.Comparator.naturalOrder;
import static javaslang.collection.List.ofAll;

public class Board {
    private final Map<Coordinate, Spot> state;
    private final int dimension;
    private final Random random = new SecureRandom();

    public static Board createEmpty(int dimension) {
        Map<Coordinate, Spot> entries = Stream.from(0).take(dimension).crossProduct().map(
                t -> Tuple.of(t._1, t._2, Spot.EMPTY)
        ).toJavaMap(
                t -> Tuple.of(new Coordinate(t._1, t._2), t._3)
        );

        return new Board(entries, dimension);
    }

    private Board(Map<Coordinate, Spot> state, int dimension) {
        this.state = state;
        this.dimension = dimension;
    }

    private List<Spot> row(int y) {
        return ofAll(state.entrySet())
                .filter(entry -> entry.getKey().y() == y)
                .sorted(Comparator.comparing(entry -> entry.getKey().x(), naturalOrder()))
                .map(Entry::getValue);
    }

    private List<Spot> column(int x) {
        return ofAll(state.entrySet())
                .filter(entry -> entry.getKey().x() == x)
                .sorted(Comparator.comparing(entry -> entry.getKey().y(), naturalOrder()))
                .map(Entry::getValue);
    }

    public void show() {
        Stream.from(0).take(dimension).map(
                i -> row(i).map(Spot::print).intersperse(" | ").foldLeft("", (a, l) -> a + l)
        ).forEach(System.out::println);
    }

    public long emptyFields() {
        return state.entrySet()
                .stream()
                .filter(coordinateSpotEntry -> coordinateSpotEntry.getValue().equals(Spot.EMPTY))
                .count();
    }

    public void left() {
        Stream.from(0).take(dimension).forEach(
                y -> {
                    List<Spot> result = collapse(row(y));

                    Stream.from(0).take(result.size()).forEach(
                            x -> state.replace(new Coordinate(x, y), result.get(x))
                    );
                }
        );
    }

    public void right() {
        Stream.from(0).take(dimension).forEach(
                y -> {
                    List<Spot> result = collapse(row(y).reverse()).reverse();

                    Stream.from(0).take(result.size()).forEach(
                            x -> state.replace(new Coordinate(x, y), result.get(x))
                    );
                }
        );
    }

    public void up() {
        Stream.from(0).take(dimension).forEach(
                x -> {
                    List<Spot> result = collapse(column(x));

                    Stream.from(0).take(result.size()).forEach(
                            y -> state.replace(new Coordinate(x, y), result.get(y))
                    );
                }
        );
    }

    public void down() {
        Stream.from(0).take(dimension).forEach(
                x -> {
                    List<Spot> result = collapse(column(x).reverse()).reverse();
                    Stream.from(0).take(result.size()).forEach(
                            y -> state.replace(new Coordinate(x, y), result.get(y))
                    );
                }
        );
    }

    public void add() {
        List<Coordinate> collect = List.ofAll(state.entrySet())
                .filter(coordinateSpotEntry -> coordinateSpotEntry.getValue().equals(Spot.EMPTY))
                .map(Entry::getKey);

        // can not add something to an full board.
        if (collect.isEmpty()) {
            return;
        }

        Coordinate randomCoordinate = collect.get(random.nextInt(collect.size()));

        Spot spot = state.get(randomCoordinate);
        Spot improved = spot.improve();
        state.replace(randomCoordinate, improved);
    }

    public static List<Spot> collapse(List<Spot> line) {
        List<Spot> filtered = line.filter(s -> !s.equals(Spot.EMPTY));
        List<Spot> list = filtered.foldLeft(List.empty(),
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

        return Stream.ofAll(list).extend(Spot.EMPTY).take(line.length()).toList();
    }
}
