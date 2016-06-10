package de.merv.logic.content;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BoardTest {

    @Test
    public void createEmptyBoard_boardIsEmptyWithCorrectDimension() {
        Board empty = Board.empty(4);
        assertThat(empty.empties()).isEqualTo(4 * 4);
    }
}