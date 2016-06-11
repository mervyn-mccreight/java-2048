package de.merv.logic;

import de.merv.logic.content.Board;
import javaslang.Tuple2;

import java.io.PrintStream;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Random;

public class GameState {
    private Board state;
    private final Random random;
    private int points;

    private GameState(Board state, int points) {
        this.state = state;
        this.random = new SecureRandom();
        this.points = points;
    }

    public static GameState initial(int boardDimension) {
        return new GameState(Board.empty(boardDimension), 0);
    }

    public void add() {
        this.state = state.add(this.random);
    }

    public void print(PrintStream out) {
        out.println(MessageFormat.format("Points: {0}", points));
        out.println("Board:");
        state.printableRows().forEach(out::println);
    }

    public boolean isLost() {
        return state.empties().length() == 0;
    }

    public void left() {
        Tuple2<Integer, Board> newState = state.left();
        state = newState._2();
        points += newState._1();
    }

    public void right() {
        Tuple2<Integer, Board> newState = state.right();
        state = newState._2();
        points += newState._1();
    }

    public void up() {
        Tuple2<Integer, Board> newState = state.up();
        state = newState._2();
        points += newState._1();
    }

    public void down() {
        Tuple2<Integer, Board> newState = state.down();
        state = newState._2();
        points += newState._1();
    }
}
