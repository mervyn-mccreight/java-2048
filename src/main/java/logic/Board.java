package logic;

import logic.content.Coordinate;
import logic.content.Spot;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Board {
    private final Map<Coordinate, Spot> state;
    private final int dimension;

    public static Board createEmpty(int dimension) {
        List<Entry<Coordinate, Spot>> entries = new ArrayList<>();

        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                entries.add(new SimpleImmutableEntry<>(new Coordinate(x, y), Spot.EMPTY));
            }
        }

        return new Board(entries
                .stream()
                .collect(toMap(Entry::getKey, Entry::getValue)),
                dimension
        );
    }

    private Board(Map<Coordinate, Spot> state, int dimension) {
        this.state = state;
        this.dimension = dimension;
    }

    public List<Spot> row(int y) {
        return state.entrySet()
                .stream()
                .filter(entry -> entry.getKey().y() == y)
                .sorted(Comparator.comparing(entry -> entry.getKey().x(), naturalOrder()))
                .map(Entry::getValue)
                .collect(toList());
    }

    public List<Spot> column(int x) {
        return state.entrySet()
                .stream()
                .filter(entry -> entry.getKey().x() == x)
                .sorted(Comparator.comparing(entry -> entry.getKey().y(), naturalOrder()))
                .map(Entry::getValue)
                .collect(toList());
    }

    public void show() {
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            rows.add(row(i).stream().map(Spot::print).collect(Collectors.joining(" | ")));
        }
        rows.forEach(System.out::println);
    }

    public long emptyFields() {
        return state.entrySet()
                .stream()
                .filter(coordinateSpotEntry -> coordinateSpotEntry.getValue().equals(Spot.EMPTY))
                .count();
    }

    public void left() {
        for (int y = 0; y < dimension; y++) {
            List<Spot> row = row(y);
            List<Spot> collapsedRow = collapse(row);

            for (int x = 0; x < collapsedRow.size(); x++) {
                state.replace(new Coordinate(x, y), collapsedRow.get(x));
            }
        }
    }

    public void right() {
        for (int y = 0; y < dimension; y++) {
            List<Spot> row = row(y);
            Collections.reverse(row);

            List<Spot> collapsedRow = collapse(row);
            Collections.reverse(collapsedRow);

            for (int x = 0; x < collapsedRow.size(); x++) {
                state.replace(new Coordinate(x, y), collapsedRow.get(x));
            }
        }
    }

    public void up() {
        for (int x = 0; x < dimension; x++) {
            List<Spot> column = column(x);
            List<Spot> collapsed = collapse(column);

            for (int y = 0; y < collapsed.size(); y++) {
                state.replace(new Coordinate(x, y), collapsed.get(y));
            }
        }
    }

    public void down() {
        for (int x = 0; x < dimension; x++) {
            List<Spot> column = column(x);
            Collections.reverse(column);

            List<Spot> collapsed = collapse(column);
            Collections.reverse(collapsed);

            for (int y = 0; y < collapsed.size(); y++) {
                state.replace(new Coordinate(x, y), collapsed.get(y));
            }
        }
    }

    public void add() {
        List<Coordinate> collect = state.entrySet()
                .stream()
                .filter(coordinateSpotEntry -> coordinateSpotEntry.getValue().equals(Spot.EMPTY))
                .map(Entry::getKey)
                .collect(toList());

        if (collect.isEmpty()) {
            return;
        }

        Collections.shuffle(collect);

        Coordinate coordinate = collect.get(0);
        Spot spot = state.get(coordinate);
        Spot improved = spot.improve();
        state.replace(coordinate, improved);
    }

    public static List<Spot> collapse(List<Spot> line) {
        List<Spot> filtered = line.stream()
                .sequential()
                .filter(s -> !s.equals(Spot.EMPTY)).collect(Collectors.toList());

        // a full line can not collapse
        if (filtered.size() == line.size()) {
            return line;
        }

        // todo (04.06.2016): this re-implements a fold-left (but only if it is a sequential stream!)
        List<Spot> reduce = filtered
                .stream()
                .sequential()
                .reduce(new LinkedList<>(),
                        (acc, next) -> {
                            if (acc.isEmpty()) {
                                acc.add(next);
                                return acc;
                            }

                            if (acc.getLast().equals(next)) {
                                acc.removeLast();
                                acc.add(next.improve());
                            } else {
                                acc.add(next);
                            }
                            return acc;
                        },
                        (identity, acc) -> acc
                );

        while (reduce.size() != line.size()) {
            reduce.add(Spot.EMPTY);
        }
        return reduce;
    }
}
