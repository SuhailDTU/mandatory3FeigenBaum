package mandatory3;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public class Painter extends JPanel {

    public static final int window_width = 1440;
    public static final int window_height = 900;

    public DrawPanel drawPanel = new DrawPanel(window_width,window_height);
    //public Brush brush = new Brush(drawPanel);

    private Point point = new Point();

    public Painter() {
        JFrame frame = new JFrame("Points");
        frame.getContentPane().add(drawPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void drawPoint(double x, double y) {
        x = x * window_width;
        y = y * window_height;
        point.setLocation(new Point((int)x,(int)y));
        drawPanel.curveStart(point);
        drawPanel.curveAdd(point);
        drawPanel.curveEnd(point);
    }

    public void clear() {
        drawPanel.clearOverwrite();
    }

}

class DrawPanel extends JPanel {
    private static int ST_WIDTH;
    private static int ST_HEIGHT;
    private static final Color BACKGROUND_COLOR = Color.black;
    private static final float STROKE_WIDTH = 4f;
    private static final Stroke STROKE = new BasicStroke(STROKE_WIDTH,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    //private static final Color[] colors = {Color.black, Color.blue, Color.red,Color.green, Color.orange, Color.MAGENTA};

    //private BufferedImage bImage = new BufferedImage(ST_WIDTH, ST_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private BufferedImage bImage;
    private Color color = Color.black;
    private Color drawColor = Color.black;
    private ArrayList<Point> points = new ArrayList<Point>();
    //private int colorIndex = 0;

    public DrawPanel(int width, int height) {
        this.ST_WIDTH = width;
        this.ST_HEIGHT = height;
        bImage = new BufferedImage(ST_WIDTH, ST_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = bImage.getGraphics();
        clearOverwrite();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bImage, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g;
        drawCurve(g2);
    }

    private void addCurveToBufferedImage() {
        Graphics2D g2 = bImage.createGraphics();
        drawCurve(g2);
        g2.dispose();
    }

    private void drawCurve(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(STROKE);

        if (points != null && points.size() > 1) {
            for (int i = 0; i < points.size() - 1; i++) {
                drawColor = Color.getHSBColor(((float)points.get(i).x/(float)ST_WIDTH),1.0f,0.8f);
                g2.setColor(drawColor);
                int x1 = points.get(i).x;
                int y1 = points.get(i).y;
                int x2 = points.get(i + 1).x;
                int y2 = points.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(ST_WIDTH, ST_HEIGHT);
    }

    public void curveStart(Point point) {
        points.clear();
        points.add(point);
    }

    public void curveEnd(Point point) {
        points.add(point);
        addCurveToBufferedImage();
        points.clear();
        repaint();

      /*
      colorIndex++;
      colorIndex %= colors.length;
      setColor(colors[colorIndex]);
      */
    }

    public void curveAdd(Point point) {
        points.add(point);
        repaint();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void clearOverwrite() {
        Graphics g = bImage.getGraphics();
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, ST_WIDTH, ST_HEIGHT);
        g.dispose();
    }
}

/*
class Brush {
   private STDrawPanel drawPanel;
   public Brush(STDrawPanel drawPanel) {
      this.drawPanel = drawPanel;
   }
   public void drawPoint(Point point) {
      drawPanel.curveStart(point);
      drawPanel.curveAdd(point);
      drawPanel.curveEnd(point);
   }
}
*/