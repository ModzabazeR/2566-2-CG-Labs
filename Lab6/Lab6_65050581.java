import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class Lab6_65050581 extends JPanel {
    public static void main(String[] args) {
        Lab6_65050581 panel = new Lab6_65050581();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Lab6_65050581");
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setTransform(new AffineTransform(1, 0, 0, 1, 100, 100));

        // q1(g2);
        q2(g2);

        g2.drawRect(200, 200, 200, 200);

        g2.setColor(Color.RED);
        g2.fillRect(300, 300, 5, 5);
        g2.fillRect(200, 200, 5, 5);
    }

    private void q1(Graphics2D g2) {
        AffineTransform rotationTransform = new AffineTransform();
        rotationTransform.rotate(Math.toRadians(30), 300, 300);
        g2.transform(rotationTransform);
    }

    private void q2(Graphics2D g2) {
        AffineTransform scaleTransform = new AffineTransform();

        scaleTransform.translate(-200, -200);
        scaleTransform.scale(2, 2);
        
        scaleTransform.translate(-50, 50);
        g2.transform(scaleTransform);
    }
}