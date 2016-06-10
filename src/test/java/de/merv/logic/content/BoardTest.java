package de.merv.logic.content;

import javaslang.collection.List;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    @Test
    public void createEmptyBoard_boardIsEmptyWithCorrectDimension() {
        Board empty = Board.empty(4);
        assertThat(empty.empties()).hasSize(4 * 4);
    }

    @Test
    public void showEmptyBoard() {
        Board empty = Board.empty(4);

        List<String> rows = empty.printableRows();

        assertThat(rows).hasSize(4);
        assertThat(rows).containsExactly(
                " | | | ",
                " | | | ",
                " | | | ",
                " | | | ");
    }

    @Test
    public void add_oneFieldIsFilled() throws Exception {
        Board board = Board.empty(4);
        assertThat(board.add(new Random()).empties()).hasSize(4 * 4 - 1);
    }
}