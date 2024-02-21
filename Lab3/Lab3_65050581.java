import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import java.util.LinkedList;
import java.util.Queue;

public class Lab3_65050581 extends JPanel {
    public static void main(String[] args) {
        // สร้างหน้าต่าง JFrame
        JFrame frame = new JFrame("Lab3_65050581");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(new Lab3_65050581());
    }

    public void paintComponent(Graphics g) {
        // ex2(g);
        ex3(g);
        ex1(g);
    }

    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    private void bezierCurve(Graphics g, int[] ctrlX, int[] ctrlY) {
        // วาดเส้นโค้งโดยใช้จุดควบคุม ctrlX และ ctrlY
        if (ctrlX.length != 4 || ctrlY.length != 4) {
            System.out.println("Error: Invalid control points");
            return;
        }

        g.setColor(Color.BLACK);
        int n = 1000;
        for (int i = 0; i <= n; i++) {
            double t = (double) i / n;
            int x = (int) (Math.pow(1 - t, 3) * ctrlX[0] + 3 * t * Math.pow(1 - t, 2) * ctrlX[1]
                    + 3 * Math.pow(t, 2) * (1 - t) * ctrlX[2] + Math.pow(t, 3) * ctrlX[3]);
            int y = (int) (Math.pow(1 - t, 3) * ctrlY[0] + 3 * t * Math.pow(1 - t, 2) * ctrlY[1]
                    + 3 * Math.pow(t, 2) * (1 - t) * ctrlY[2] + Math.pow(t, 3) * ctrlY[3]);
            plot(g, x, y);
        }
    }

    private void markControlPoints(Graphics g, int[] ctrlX, int[] ctrlY) {
        // วาดจุดควบคุมและตัวอักษร P0, P1, P2, P3 ที่จุดควบคุม
        g.setColor(Color.RED);
        for (int i = 0; i < ctrlX.length; i++) {
            g.fillRect(ctrlX[i], ctrlY[i], 4, 4);
            g.drawChars(("P" + i).toCharArray(), 0, 2, ctrlX[i] + 5, ctrlY[i] + 5);
        }
    }

    public BufferedImage floodFill(BufferedImage m, int x, int y, Color target_colour, Color replacement_colour) {
        // ทำการเติมสี replacement_colour ในพื้นที่ที่มีสี target_colour ด้วยวิธี Flood Fill
        Queue<Point> q = new LinkedList<>();
        m.setRGB(x, y, replacement_colour.getRGB());
        q.add(new Point(x, y));

        while (!q.isEmpty()) {
            Point currentPoint = q.poll();
            Point south = new Point(currentPoint.x, currentPoint.y + 1);
            Point north = new Point(currentPoint.x, currentPoint.y - 1);
            Point east = new Point(currentPoint.x + 1, currentPoint.y);
            Point west = new Point(currentPoint.x - 1, currentPoint.y);

            if (m.getRGB(south.x, south.y) == target_colour.getRGB()) {
                m.setRGB(south.x, south.y, replacement_colour.getRGB());
                q.add(south);
            }
            if (m.getRGB(north.x, north.y) == target_colour.getRGB()) {
                m.setRGB(north.x, north.y, replacement_colour.getRGB());
                q.add(north);
            }
            if (m.getRGB(east.x, east.y) == target_colour.getRGB()) {
                m.setRGB(east.x, east.y, replacement_colour.getRGB());
                q.add(east);
            }
            if (m.getRGB(west.x, west.y) == target_colour.getRGB()) {
                m.setRGB(west.x, west.y, replacement_colour.getRGB());
                q.add(west);
            }
        }

        return m;
    }

    private void ex1(Graphics g) {
        int[] ctrlX = { 50, 80, 110, 140 };
        int[] ctrlY = { 50, 10, 10, 50 };
        markControlPoints(g, ctrlX, ctrlY);
        bezierCurve(g, ctrlX, ctrlY);

        ctrlX = new int[] { 300, 330, 360, 390 };
        ctrlY = new int[] { 100, 100, 150, 100 };
        markControlPoints(g, ctrlX, ctrlY);
        bezierCurve(g, ctrlX, ctrlY);

        ctrlX = new int[] { 50, 80, 110, 200 };
        ctrlY = new int[] { 200, 300, 300, 200 };
        markControlPoints(g, ctrlX, ctrlY);
        bezierCurve(g, ctrlX, ctrlY);
    }

    private void ex2(Graphics g) {
        int xPoly[] = { 150, 250, 325, 375, 400, 275, 100 };
        int yPoly[] = { 150, 100, 125, 225, 325, 375, 300 };
        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        g.drawPolygon(poly);
    }

    private void ex3(Graphics g) {
        // วาดรูปหลายเหลี่ยมและใช้ Flood Fill เติมสี
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 600, 600);

        g2.setColor(Color.BLACK);
        int xPoly[] = { 150, 250, 325, 375, 400, 275, 100 };
        int yPoly[] = { 150, 100, 125, 225, 325, 375, 300 };
        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        g2.drawPolygon(poly);

        g.drawImage(buffer, 0, 0, null);

        buffer = floodFill(buffer, 200, 150, Color.WHITE, Color.GREEN);
        g.drawImage(buffer, 0, 0, null);
    }
}