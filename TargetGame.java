import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class TargetGame extends JPanel implements MouseListener {

    class Target {
        int x, y, size;
        Image image;

        Target(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.image = new ImageIcon("images/target.png").getImage();
        }

        void draw(Graphics g) {
            g.drawImage(image, x, y, size, size, null);
        }

        boolean isHit(int mx, int my) {
            return (mx >= x && mx <= x + size &&
                    my >= y && my <= y + size);
        }
    }

    class Explosion {
        int x, y;
        int life = 10;
        Image img = new ImageIcon("images/explosion.png").getImage();

        Explosion(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void draw(Graphics g) {
            g.drawImage(img, x - 25, y - 25, 60, 60, null);
            life--;
        }
    }

    ArrayList<Target> targets = new ArrayList<>();
    ArrayList<Explosion> explosions = new ArrayList<>();

    Random rand = new Random();

    int score = 0, highScore = 0, time = 30;

    Timer gameTimer, spawnTimer;

    boolean gameStarted = false, gameOver = false;

    JButton startButton;

    public TargetGame() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
        setLayout(null);

        addMouseListener(this);

        // 🔫 Custom Cursor
        ImageIcon icon = new ImageIcon("images/crosshair.png");
    Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);

Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
        img,
        new Point(16, 16),
        "Crosshair"
);

setCursor(cursor);

        // Start button
        startButton = new JButton("Start Game");
        startButton.setBounds(220, 250, 150, 40);
        add(startButton);

        startButton.addActionListener(e -> startGame());

        // Timers
        gameTimer = new Timer(1000, e -> {
            time--;
            if (time <= 0) endGame();
            repaint();
        });

        spawnTimer = new Timer(800, e -> spawnTarget());
    }

    void startGame() {
        score = 0;
        time = 30;
        targets.clear();
        explosions.clear();

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

        if (score > highScore) highScore = score;

        startButton.setText("Restart");
        startButton.setVisible(true);
    }

    void spawnTarget() {
        int size = rand.nextInt(30) + 40;
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

            // draw explosions
            Iterator<Explosion> it = explosions.iterator();
            while (it.hasNext()) {
                Explosion ex = it.next();
                ex.draw(g);
                if (ex.life <= 0) it.remove();
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
                explosions.add(new Explosion(mx, my)); // 💥 effect
                iterator.remove();
                hit = true;
                break;
            }
        }

        if (!hit) score -= 5;

        repaint();
    }

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