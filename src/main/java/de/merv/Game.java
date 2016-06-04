package de.merv;

import de.merv.logic.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
    public static void main(String[] args) {
        Board board = Board.createEmpty(4);
        board.add();

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("README:");
        System.out.println("Type w, a, s, d to tilt the board and press enter to make the move.");

        while (true) {
            try {
                board.show();

                if (board.emptyFields() == 0) {
                    System.out.println("You lost.");
                    return;
                }

                // todo (02.06.2016): implement movement
                String input = keyboard.readLine().toLowerCase();

                switch (input) {
                    case "a":
                        board.left();
                        break;
                    case "d":
                        board.right();
                        break;
                    case "w":
                        board.up();
                        break;
                    case "s":
                        board.down();
                        break;
                }

                board.add();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
