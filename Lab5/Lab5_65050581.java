import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Lab5_65050581 extends JPanel implements Runnable {
    double circleMove = 0;

    public static void main(String[] args) {
        Lab5_65050581 m = new Lab5_65050581();

        JFrame frame = new JFrame("Lab5_65050581");
        frame.add(m);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new Thread(m).start();
    }

    @Override
    public void run() {
        double lastTime = System.currentTimeMillis();
        double currentTime, elapsedTime;

        while (true) {
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            // TODO: update logics
            // Change milliseconds to seconds
            // Move 50 pixel per second
            circleMove += 50.0 * elapsedTime / 1000.0;

            // Display
            repaint();
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 600, 600);
        g2.setColor(Color.BLACK);
        g2.translate(circleMove, 0);
        g.drawOval(0, 0, 100, 100);
    }

    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

}