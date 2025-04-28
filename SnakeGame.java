import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame gameFrame = new JFrame("Snake Game");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setResizable(false);
            gameFrame.add(new GamePanel());
            gameFrame.pack();
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);
        });
    }
}

class GamePanel extends JPanel implements ActionListener {
    private static final int WIDTH = 600, HEIGHT = 600;
    private static final int BLOCK_SIZE = 25;
    private final ArrayList<Point> snake = new ArrayList<>(); 
    private Point food;
    private String direction = "RIGHT";
    private boolean gameOver = false;
    private int score = 0;
    private final Timer timer;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        snake.add(new Point(100, 100));
        snake.add(new Point(75, 100));
        snake.add(new Point(50, 100));

        spawnFood();

        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        moveSnake();
        checkCollision();
        checkFoodCollision();
        repaint();
    }

    private void moveSnake() {
        Point head = snake.get(0);

        Point newHead = switch (direction) {
            case "UP" -> new Point(head.x, head.y - BLOCK_SIZE);
            case "DOWN" -> new Point(head.x, head.y + BLOCK_SIZE);
            case "LEFT" -> new Point(head.x - BLOCK_SIZE, head.y);
            case "RIGHT" -> new Point(head.x + BLOCK_SIZE, head.y);
            default -> new Point(head); 
        };

        snake.add(0, newHead);

        if (!newHead.equals(food)) {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver = true;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
            }
        }
    }

    private void checkFoodCollision() {
        Point head = snake.get(0);
        if (head.equals(food)) {
            score++;
            spawnFood();
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / BLOCK_SIZE) * BLOCK_SIZE;
        int y = rand.nextInt(HEIGHT / BLOCK_SIZE) * BLOCK_SIZE;
        food = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.green);
        for (Point p : snake) {
            g.fillRect(p.x, p.y, BLOCK_SIZE, BLOCK_SIZE);
        }

        g.setColor(Color.red);
        if (food != null) {
            g.fillRect(food.x, food.y, BLOCK_SIZE, BLOCK_SIZE);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", WIDTH / 2 - 90, HEIGHT / 2);
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_UP && !direction.equals("DOWN")) {
                direction = "UP";
            } else if (keyCode == KeyEvent.VK_DOWN && !direction.equals("UP")) {
                direction = "DOWN";
            } else if (keyCode == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) {
                direction = "LEFT";
            } else if (keyCode == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) {
                direction = "RIGHT";
            }
        }
    }
}
