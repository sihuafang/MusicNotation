package Music_Annotation.Sandbox;
import Music_Annotation.Reaction.Ink;
import Music_Annotation.UC;
import Music_Annotation.graphicsLib.G;
import Music_Annotation.graphicsLib.Window;
import Music_Annotation.Reaction.Shape;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ShapeTrainer extends Window{
    public static final String ILLEGAL = "This name is illegal.";
    public static final String KNOWN = "This name is known.";
    public static final String UNKNOWN = "This name is unknown.";

    public static String currentName = "Hello";
    public static String currentStatus = ILLEGAL;
    public static Shape.Prototype.List pList = null;

    public ShapeTrainer(){
        super("Shape Trainer", UC.screenWidth,UC.screenHeight);
    }

    public static void setStatus(){
        currentStatus = !Shape.Database.isLegal(currentName) ? ILLEGAL : UNKNOWN;
        // you have to use .equals() to determine the equality of strings
        //== means the same object
        //.equals() means the same value
        if (currentStatus == UNKNOWN){
            if (Shape.DB.containsKey(currentName)){
                currentStatus = KNOWN;
                pList = Shape.DB.get(currentName).prototypes;
            }
            else{pList = null;}
        }
    }

    public void paintComponent(Graphics g){
        G.fillBackground(g, Color.WHITE);
        g.setColor(Color.black);
        g.drawString(currentName, 600, 30);
        g.drawString(currentStatus,600,60);
        Ink.BUFFER.show(g);
        if (pList != null){ pList.show(g);}
    }

    public void keyTyped(KeyEvent ke){
        char c = ke.getKeyChar();
        System.out.println("Type: " + c);
        currentName = c == ' ' ? "" : currentName + c;
        if (c == 10 || c== 13){
            currentName = "";
            Shape.saveDB();
        }
        setStatus();
        repaint();
    }

    public void mousePressed(MouseEvent me){Ink.BUFFER.dn(me.getX(),me.getY()); }
    public void mouseDragged(MouseEvent me){Ink.BUFFER.drag(me.getX(),me.getY()); }
    public void mouseReleased(MouseEvent me){
//        if (currentStatus != ILLEGAL){
////            Ink ink = new Ink();
////            Shape.Prototype proto;
////            if (pList == null){
////                Shape s = new Shape(currentName);
////                Shape.DB.put(currentName, s);
////                pList = s.prototypes;
////            }
////            if (pList.bestDist(ink.norm) < UC.noMatchDist){
////                proto = Shape.Prototype.List.bestMatch;
////                proto.blend(ink.norm);
////            }
////            else{
////                proto = new Shape.Prototype();
////                pList.add(proto);
////            }
//            setStatus();
//        }
        Ink ink = new Ink();
        Shape.DB.train(currentName, ink.norm);
        setStatus();
        repaint();
    }

}

//Serialization : turn the random access file
// system into serial access file system so that computer can read