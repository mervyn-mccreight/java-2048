package de.merv.logic;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class GameStateTest {

    @Test
    public void emptyGrid_gameIsNotLost() throws Exception {
        int dimension = 4;
        GameState empty = GameState.initial(dimension);

        assertThat(empty.isLost()).isFalse();
    }
}
