import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Example extends JPanel implements Runnable {
    int x = 0;
    public static void main(String[] args) {
        Example m = new Example();

        JFrame frame = new JFrame("Example");
        frame.add(m);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }

    @Override
    public void run() {
        double lastTime = System.currentTimeMillis();
        while (true) {
            double currentTime = System.currentTimeMillis();
            double elapsedTime = currentTime - lastTime;

            x += 100;

            repaint();

            lastTime = currentTime;
        }
    }

    public void paintComponent(Graphics g) {
        g.drawOval(x, 0, 100, 100);
    }


}