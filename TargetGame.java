import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TargetGame extends JPanel implements MouseListener, ActionListener {

    int x, y;              // target position
    int size = 50;         // target size
    int score = 0;
    int time = 30;         // game time
    Timer timer;
    Random rand = new Random();
    boolean gameOver = false;

    public TargetGame() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.WHITE);

        addMouseListener(this);

        // initial position
        x = rand.nextInt(550);
        y = rand.nextInt(550);

        // timer (1 second)
        timer = new Timer(1000, this);
        timer.start();
    }

    // Draw everything
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
            // draw target
            g.setColor(Color.RED);
            g.fillOval(x, y, size, size);

            // score
            g.setColor(Color.BLACK);
            g.drawString("Score: " + score, 20, 20);

            // time
            g.drawString("Time: " + time, 20, 40);
        } else {
            // game over screen
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", 200, 250);
            g.drawString("Final Score: " + score, 180, 300);
        }
    }

    // Timer event
    public void actionPerformed(ActionEvent e) {
        time--;
        if (time <= 0) {
            gameOver = true;
            timer.stop();
        }
        repaint();
    }

    // Mouse click
    public void mouseClicked(MouseEvent e) {
        if (gameOver) return;

        int clickX = e.getX();
        int clickY = e.getY();

        // check hit
        if (clickX >= x && clickX <= x + size &&
            clickY >= y && clickY <= y + size) {

            score += 10;

            // move target
            x = rand.nextInt(550);
            y = rand.nextInt(550);

            // increase difficulty
            if (size > 20) {
                size -= 2;
            }

        } else {
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
        JFrame frame = new JFrame("Target Shooting Game");
        TargetGame game = new TargetGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}