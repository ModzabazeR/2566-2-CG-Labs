import java.awt.*;
import javax.swing.*;

public class Lab1_65050581 extends JPanel {
    public static void main(String[] args) {
        Lab1_65050581 panel = new Lab1_65050581();

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("First Swing");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void paintComponent(Graphics g) {
        g.drawString("Hello", 40, 40);
        g.setColor(Color.BLUE);
        g.fillRect(130, 30, 100, 80);
        g.drawOval(30, 130, 50, 60);
        g.setColor(Color.RED);
        g.drawLine(0, 0, 200, 30);
        g.fillOval(130, 130, 50, 60);
        g.drawArc(30, 200, 40, 50, 90, 60);
        g.fillArc(30, 130, 40, 50, 180, 40);

        // plot 10 points in different colors
        int x = 300;
        int y = 300;
        for (int i = 0; i < 10; i++) {
            int r = (int) (Math.random() * 256);
            int gr = (int) (Math.random() * 256);
            int b = (int) (Math.random() * 256);
            Color randomColor = new Color(r, gr, b);
            g.setColor(randomColor);
            plot(g, x, y);
            x += 10;
            y += 10;
        }
    }

    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }
}

/* ข้อ 3

เนื่องจากหน้าจอมีขนาด 600x600 มุมบนซ้ายก็คือจุด (0,0) และมุมบนขวาก็คือ (600,600)
เราอาจจะวาดเส้นได้โดยการเขียน for loop คล้าย ๆ ในแล็บนี้โดยให้ x กับ y เริ่มต้นที่ 0 แล้วเพิ่มขึ้นทีละ 1 จนถึง 600
ตาก็จะเห็นจุดเป็นเส้นตรงที่วาดขึ้นมา

*/ 