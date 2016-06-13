package de.merv.logic;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class GameStateTest {

    @Test
    public void emptyGrid_gameIsNotLost() throws Exception {
        int dimension = 4;
        GameState initial = GameState.initial(dimension);

        // todo (13.06.2016): this test is too weak.
        assertThat(initial.isLost()).isFalse();
    }
}
