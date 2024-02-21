import java.awt.*;
import javax.swing.*;

public class Lab2_65050581 extends JPanel {
    public static void main(String[] args) {
        Lab2_65050581 panel = new Lab2_65050581();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Lab2_65050581");
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);

        // naiveLine(g, 100, 100, 400, 200);
        // naiveLine(g, 400, 300, 100, 200);
        // naiveLine(g, 100, 100, 200, 400);

        // DDALine(g, 100, 100, 400, 200);
        // DDALine(g, 400, 300, 100, 200);
        // DDALine(g, 100, 100, 200, 400);

        bresenhamLine(g, 100, 100, 400, 200);
        bresenhamLine(g, 400, 300, 100, 200);
        bresenhamLine(g, 100, 100, 200, 400);
    }

    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    private void naiveLine(Graphics g, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double b = y1 - (dy / dx) * x1;

        for (int x = x1; x <= x2; x++) {
            int y = (int) (Math.round((dy / dx) * x) + b);
            plot(g, x, y);
        }
    }

    private void DDALine(Graphics g, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        int x = x1;
        int y = y1;
        double m = dy / dx;

        if (m <= 1 && m >= 0) {
            for (int xx = x1; xx <= x2; xx++) {
                y = (int) (Math.round(y + m));
                plot(g, xx, y);
            }
        } else if (m <= -1) {
            for (int xx = x2; xx >= x1; xx--) {
                y = (int) (Math.round(y + m));
                plot(g, xx, y);
            }
        } else if (m > 1) {
            for (int yy = y1; yy <= y2; yy++) {
                x = (int) (Math.round(x + 1 / m));
                plot(g, x, yy);
            }
        } else {
            for (int yy = y2; yy >= y1; yy--) {
                x = (int) (Math.round(x + 1 / m));
                plot(g, x, yy);
            }
        }
    }

    private void bresenhamLine(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean isSwap = false;

        if (dy > dx) {
            int temp = dx;
            dx = dy;
            dy = temp;
            isSwap = true;
        }

        int D = 2 * dy - dx;

        int x = x1;
        int y = y1;

        for (int i = 1; i <= dx; i++) {
            plot(g, x, y);

            if (D > 0) {
                if (isSwap) x += sx;
                else y += sy;
                D -= 2 * dx;
            }

            if (isSwap) y += sy;
            else x += sx;

            D += 2 * dy;
        }
    }
}