import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Lab4_65050581 extends JPanel {
    public static void main(String[] args) {
        // สร้างหน้าต่าง JFrame
        JFrame frame = new JFrame("Lab4_65050581");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(new Lab4_65050581());
    }

    public void paintComponent(Graphics g) {
        // midpointCircle(g, 300, 300, 200);
        midpointEllipse(g, 300, 300, 200, 100);
    }

    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    private void midpointCircle(Graphics g, int xc, int yc, int r) {
        int x = 0;
        int y = r;
        int Dx = 2 * x;
        int Dy = 2 * y;
        int D = 1 - r;

        while (x <= y) {
            plot(g, xc + x, yc + y);
            plot(g, xc + x, yc - y);
            plot(g, xc - x, yc - y);
            plot(g, xc - x, yc + y);

            plot(g, xc + y, yc + x);
            plot(g, xc + y, yc - x);
            plot(g, xc - y, yc - x);
            plot(g, xc - y, yc + x);

            x++;
            Dx += 2;
            D += Dx + 1;

            if (D >= 0) {
                y--;
                Dy -= 2;
                D -= Dy;
            }
        }
    }

    private void midpointEllipse(Graphics g, int xc, int yc, int a, int b) {
        int a2 = a * a, b2 = b * b;
        int twoA2 = 2 * a2, twoB2 = 2 * b2;

        // region 1
        int x = 0;
        int y = b;
        int D = Math.round(b2 - a2*b + a2/4);
        int Dx = 0, Dy = twoA2 * y;

        while (Dx <= Dy) {
            plot(g, xc + x, yc + y);
            plot(g, xc + x, yc - y);
            plot(g, xc - x, yc - y);
            plot(g, xc - x, yc + y);

            x++;
            Dx += twoB2;
            D += Dx + b2;

            if (D >= 0) {
                y--;
                Dy -= twoA2;
                D -= Dy;
            }
        }

        // region 2
        x = a;
        y = 0;
        D = Math.round(a2 - b2*a + b2/4);
        Dx = twoB2 * x;
        Dy = 0;

        while (Dx >= Dy) {
            plot(g, xc + x, yc + y);
            plot(g, xc + x, yc - y);
            plot(g, xc - x, yc - y);
            plot(g, xc - x, yc + y);

            y++;
            Dy += twoA2;
            D += Dy + a2;

            if (D >= 0) {
                x--;
                Dx -= twoB2;
                D -= Dx;
            }
        }
    }

}