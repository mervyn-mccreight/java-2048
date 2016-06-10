package de.merv.logic;

import de.merv.logic.content.Board;

import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.Random;

public class GameState {
    private Board state;
    private final Random random;

    private GameState(Board state) {
        this.state = state;
        this.random = new SecureRandom();
    }

    public static GameState initial(int boardDimension) {
        return new GameState(Board.empty(boardDimension));
    }

    public void add() {
        this.state = state.add(this.random);
    }

    // TODO: 6/10/16 implement and show score
    public void print(PrintStream out) {
        out.println("Board:");
        state.printableRows().forEach(out::println);
    }

    public boolean isLost() {
        return state.empties().length() == 0;
    }

    public void left() {
        state = state.left();
    }

    public void right() {
        state = state.right();
    }

    public void up() {
        state = state.up();
    }

    public void down() {
        state = state.down();
    }
}
