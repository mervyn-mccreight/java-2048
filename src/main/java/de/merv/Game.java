package de.merv;

import de.merv.logic.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
    public static void main(String[] args) {
        GameState gameState = GameState.initial(4);
        gameState.add();

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("README:");
        System.out.println("Type w, a, s, d to tilt the gameState and press enter to make the move.");

        while (!gameState.isLost()) {
            try {
                gameState.print(System.out);

                // todo (02.06.2016): implement movement
                String input = keyboard.readLine().toLowerCase();

                switch (input) {
                    case "a":
                        gameState.left();
                        break;
                    case "d":
                        gameState.right();
                        break;
                    case "w":
                        gameState.up();
                        break;
                    case "s":
                        gameState.down();
                        break;
                }

                gameState.add();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        gameState.print(System.out);
        System.out.println("The board is full. You lost!");
    }
}
