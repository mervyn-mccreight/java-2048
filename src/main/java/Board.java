import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class Board {
    private final Map<Coordinate, Spot> state;

    public static Board createEmpty(int dimension) {
        ArrayList<Map.Entry<Coordinate, Spot>> entries = new ArrayList<>();

        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                entries.add(new AbstractMap.SimpleImmutableEntry<>(new Coordinate(x, y), Spot.EMPTY));
            }
        }

        return new Board(entries
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    private Board(Map<Coordinate, Spot> state) {
        this.state = state;
    }

    public List<Spot> row(int x) {
        return state.entrySet()
                .stream()
                .filter(entry -> entry.getKey().x() == x)
                .sorted(Comparator.comparing(entry -> entry.getKey().y(), Comparator.naturalOrder()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Spot> column(int y) {
        return state.entrySet()
                .stream()
                .filter(entry -> entry.getKey().y() == y)
                .sorted(Comparator.comparing(entry -> entry.getKey().x(), Comparator.naturalOrder()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
