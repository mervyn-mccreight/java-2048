import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
    public static void main(String[] args) {
        Board board = Board.createEmpty(4);

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("README:");
        System.out.println("Type w, a, s, d to tilt the board and press enter to make the move.");

        while (true) {
            try {
                board.show();
                String input = keyboard.readLine();
                board.add();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
