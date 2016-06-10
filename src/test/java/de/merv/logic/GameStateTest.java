package de.merv.logic;

import de.merv.logic.content.Spot;
import javaslang.collection.List;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GameStateTest {

    @Test
    public void emptyGrid_allFieldsAreEmpty() throws Exception {
        int dimension = 4;
        GameState empty = GameState.createEmpty(dimension);

        assertThat(empty.emptyFields(), is(4L * 4L));
    }

    @Test
    public void add_oneFieldIsFilled() throws Exception {
        GameState empty = GameState.createEmpty(4);
        assertThat(empty.emptyFields(), is(16L));
        empty.add();
        assertThat(empty.emptyFields(), is(15L));
    }

    @Test
    public void collapseSimpleLine() throws Exception {
        List<Spot> line = List.of(new Spot(2), Spot.EMPTY, new Spot(2), Spot.EMPTY);
        List<Spot> expected = List.of(new Spot(4), Spot.EMPTY, Spot.EMPTY, Spot.EMPTY);

        assertThat(GameState.collapse(line), is(expected));
    }

    @Test
    public void collapseComplexLine() throws Exception {
        List<Spot> line = List.of(new Spot(2), Spot.EMPTY, new Spot(2), new Spot(4));
        List<Spot> expected = List.of(new Spot(8), Spot.EMPTY, Spot.EMPTY, Spot.EMPTY);

        assertThat(GameState.collapse(line), is(expected));
    }

    @Test
    public void collapseFullLine() throws Exception {
        List<Spot> line = List.of(new Spot(2), new Spot(2), new Spot(2), new Spot(2));
        List<Spot> expected = List.of(new Spot(4), new Spot(4), Spot.EMPTY, Spot.EMPTY);

        assertThat(GameState.collapse(line), is(expected));
    }
}
