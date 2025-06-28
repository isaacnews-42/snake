import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {

    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile apple;
    Random random;

    int velocityx;
    int velocityy;

    Timer gameLoop;

    boolean gameOver = false;
    boolean gameWon = false;

    boolean canChangeDirection = true;


    Game(int boardWidth, int boardHeight){

        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        apple = new Tile(10, 10);
        random = new Random();
        placeApple();

        velocityx = 1;
        velocityy = 0;

        gameLoop = new Timer(75, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g){

        for(int i = 0; i < boardWidth/tileSize; i++){
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        g.setColor(Color.RED);
        g.fill3DRect(apple.x * tileSize, apple.y * tileSize, tileSize, tileSize, true);

        g.setColor(Color.GREEN);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        if(gameOver){
            g.setColor(Color.PINK);
            g.drawString("Game Over, final score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            g.drawString("Press Space to restart", tileSize - 16, tileSize + 20);
            g.drawString("Press Esc to exit", tileSize - 16, tileSize + 40);

        }

        else if(gameWon){
            g.setColor(Color.BLUE);
            g.drawString("Congrats! You won the Game! " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            g.drawString("Press Space to restart", tileSize - 16, tileSize + 20);
            g.drawString("Press Esc to exit", tileSize - 16, tileSize + 40);
        }

        else{
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeApple(){
        boolean onSnake;
        do {
            onSnake = false;
            apple.x = random.nextInt(boardWidth / tileSize);
            apple.y = random.nextInt(boardHeight / tileSize);

            if(collision(apple, snakeHead)){
                onSnake = true;
            }

            for(Tile part : snakeBody){
                if (collision(apple, part)){
                    onSnake = true;
                    break;
                }
            }
        } while(onSnake);
    }

    public void moveMechanic(){
        if(collision(snakeHead, apple)){
            snakeBody.add(new Tile(apple.x, apple.y));
            placeApple();
        }

        if(snakeBody.size() == 576){
            gameWon = true;
            gameOver = true;
            gameLoop.stop();
        }

        for(int i = snakeBody.size() - 1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityx;
        snakeHead.y += velocityy;

        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);

            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        if(snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize
          || snakeHead.y < 0 || snakeHead.y  >= boardHeight / tileSize){
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void restartGame(){
        snakeHead = new Tile(5, 5);
        snakeBody.clear();

        velocityx = 1;
        velocityy = 0;

        placeApple();

        gameOver = false;
        canChangeDirection = true;

        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        moveMechanic();
        repaint();

        canChangeDirection = true;

        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }

        if (!canChangeDirection) return;

        if(e.getKeyCode() == KeyEvent.VK_SPACE && gameOver){
            restartGame();
            return;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP && velocityy != 1){
            velocityx = 0;
            velocityy = -1;
            canChangeDirection = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityy != -1){
            velocityx = 0;
            velocityy = 1;
            canChangeDirection = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityx != 1){
            velocityx = -1;
            velocityy = 0;
            canChangeDirection = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityx != -1){
            velocityx = 1;
            velocityy = 0;
            canChangeDirection = false;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
