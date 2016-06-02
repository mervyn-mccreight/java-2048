import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        ArrayList<Entry<Coordinate, Spot>> entries = new ArrayList<>();

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

    public List<Spot> row(int x) {
        return state.entrySet()
                .stream()
                .filter(entry -> entry.getKey().x() == x)
                .sorted(Comparator.comparing(entry -> entry.getKey().y(), naturalOrder()))
                .map(Entry::getValue)
                .collect(toList());
    }

    public List<Spot> column(int y) {
        return state.entrySet()
                .stream()
                .filter(entry -> entry.getKey().y() == y)
                .sorted(Comparator.comparing(entry -> entry.getKey().x(), naturalOrder()))
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
}
