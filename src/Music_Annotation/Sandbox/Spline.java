package Music_Annotation.Sandbox;

import Music_Annotation.UC;
import Music_Annotation.graphicsLib.G;
import Music_Annotation.graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Spline extends Window {

    public Spline() {
        super("Splines", UC.screenWidth, UC.screenHeight);
    }

    public static Point[] points = {new Point(100, 100), new Point(100, 200), new Point(300, 300)};
    public static int cPoint = 0;
    public void paintComponent(Graphics g) {
        G.fillBackground(g, Color.WHITE);
        g.setColor(Color.RED);
//        g.fillRect(100, 100, 100, 100);
        G.poly.reset();
        G.pSpline(points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y, 4);
        g.fillPolygon(G.poly);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        cPoint = closestPoint(me.getX(), me.getY());
        repaint();
    }

    private int closestPoint(int x, int y) {
        int result = 0;
        int closestDistance = 1000000;
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            int d = (p.x - x) * (p.x - x) + (p.y - y) * (p.y - y);
            if (d < closestDistance) {
                closestDistance = d;
                result = i;
            }
        }
        return result;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        points[cPoint].x = me.getX();
        points[cPoint].y = me.getY();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        repaint();
    }
}
