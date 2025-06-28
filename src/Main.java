import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        int boardWidth = 600;
        int boardHeight = 600;

        JFrame frame = new JFrame("Snake");
        frame.setSize(boardWidth, boardHeight);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game snake = new Game(boardWidth, boardHeight);
        frame.add(snake);
        frame.pack();
        snake.requestFocus();
    }
}