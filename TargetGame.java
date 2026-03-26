import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;

public class TargetGame extends JPanel implements MouseListener {

    // 🎯 Target class
    static class Target {
        int x, y, size;
        Color color;

        Target(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = new Color(
                    (int)(Math.random() * 255),
                    (int)(Math.random() * 255),
                    (int)(Math.random() * 255)
            );
        }

        void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x, y, size, size);
        }

        boolean isHit(int mx, int my) {
            return (mx >= x && mx <= x + size &&
                    my >= y && my <= y + size);
        }
    }

    ArrayList<Target> targets = new ArrayList<>();
    Random rand = new Random();

    int score = 0;
    int highScore = 0;
    int time = 30;

    Timer gameTimer;
    Timer spawnTimer;

    boolean gameStarted = false;
    boolean gameOver = false;

    JButton startButton;

    public TargetGame() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
        setLayout(null);

        addMouseListener(this);

        // Start Button
        startButton = new JButton("Start Game");
        startButton.setBounds(220, 250, 150, 40);
        add(startButton);

        startButton.addActionListener(e -> startGame());

        // Game Timer (1 sec)
        gameTimer = new Timer(1000, e -> {
            time--;
            if (time <= 0) {
                endGame();
            }
            repaint();
        });

        // Target Spawn Timer
        spawnTimer = new Timer(1000, e -> spawnTarget());
    }

    void startGame() {
        score = 0;
        time = 30;
        targets.clear();

        gameStarted = true;
        gameOver = false;

        startButton.setVisible(false);

        gameTimer.start();
        spawnTimer.start();
    }

    void endGame() {
        gameStarted = false;
        gameOver = true;

        gameTimer.stop();
        spawnTimer.stop();

        if (score > highScore) {
            highScore = score;
        }

        startButton.setText("Restart");
        startButton.setVisible(true);
    }

    void spawnTarget() {
        int size = rand.nextInt(30) + 30;
        int x = rand.nextInt(600 - size);
        int y = rand.nextInt(600 - size);

        targets.add(new Target(x, y, size));

        if (targets.size() > 5) {
            targets.remove(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameStarted && !gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("🎯 Target Shooting Game", 120, 150);
        }

        if (gameStarted) {
            for (Target t : targets) {
                t.draw(g);
            }

            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 20, 20);
            g.drawString("Time: " + time, 20, 40);
            g.drawString("High Score: " + highScore, 20, 60);
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", 200, 250);
            g.drawString("Score: " + score, 220, 300);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameStarted) return;

        int mx = e.getX();
        int my = e.getY();

        boolean hit = false;

        Iterator<Target> iterator = targets.iterator();
        while (iterator.hasNext()) {
            Target t = iterator.next();
            if (t.isHit(mx, my)) {
                score += 10;
                iterator.remove();
                hit = true;
                break;
            }
        }

        if (!hit) {
            score -= 5;
        }

        repaint();
    }

    // unused methods
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("🎯 Target Game Pro");
        TargetGame game = new TargetGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}