package Music_Annotation.Sandbox;

import Music_Annotation.Reaction.Ink;
import Music_Annotation.Reaction.Shape;
import Music_Annotation.UC;
import Music_Annotation.graphicsLib.G;
import Music_Annotation.graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static Ink.List inkList = new Ink.List();
    public static Shape.Prototype.List pList = new Shape.Prototype.List();
    public static String recognized = "";
    public PaintInk(){super("PaintInk", UC.screenWidth,UC.screenHeight);}

    public void paintComponent(Graphics g){
        G.fillBackground(g,Color.white);
        g.setColor(Color.red);
//        g.drawString("points :" + Ink.BUFFER.n, 600,30);
//        inkList.show(g);
//        pList.show(g);
        Ink.BUFFER.show(g);
        g.drawString(recognized,700,40);
    }
    public void mousePressed (MouseEvent me){ Ink.BUFFER.dn(me.getX(),me.getY()); repaint();}
    public void mouseDragged (MouseEvent me){ Ink.BUFFER.drag(me.getX(),me.getY()); repaint();}
    public void mouseReleased(MouseEvent me){
        Ink ink = new Ink();
        Shape s = Shape.recognize(ink);
        recognized = "Recognized: " + ((s == null) ? "UNKNOWN":s.name);
//        inkList.add(ink);
//        Shape.Prototype proto;
//        if(pList.bestDist(ink.norm) < UC.noMatchDist){
//            Shape.Prototype.List.bestMatch.blend(ink.norm);
//            proto = Shape.Prototype.List.bestMatch;
//        }
//        else{
//            proto = new Shape.Prototype();
//            pList.add(proto);
//        }
//        ink.norm = proto;
        repaint();
    }

}
