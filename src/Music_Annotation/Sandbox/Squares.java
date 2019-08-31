package Music_Annotation.Sandbox;
import Music_Annotation.graphicsLib.Window;
import Music_Annotation.graphicsLib.G;
import Music_Annotation.UC;
import Music_Annotation.I;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Squares extends Window implements ActionListener {
    public Squares(){
        super("square",UC.screenWidth,UC.screenHeight);
        timer = new Timer(33, this);
        timer.setInitialDelay(5000);
        timer.start();
    }
    public static Timer timer;
    public static G.VS theVS = new G.VS(100,100,200,300);
    public static Color theCOLOR = G.rndColor();
    public static Square.List theList = new Square.List();;
    public static Square backgroundSquare = new Square(0,0){


        @Override
        public void dn(int x, int y){
            theList.add(new Square(x,y));

        }
        public void drag(int x, int y){
            Square s = theList.get(theList.size()-1);
            int w = Math.abs(x - s.loc.x);
            int h = Math.abs(y - s.loc.y);
            s.resize(w,h);
        }
        public void up(int x, int y){
            firstpressed.set(x,y);
        }

    };
    static{
        theList.add(backgroundSquare);
        backgroundSquare.size.set(3000,3000);
        backgroundSquare.c = Color.blue;

    }
    public static boolean dragging = false;
    public static Square theSQUARE = new Square(200,328);
    public static G.V  mousePosition = new G.V(0,0);
    public static G.V firstpressed = new G.V(0,0);
    public static I.Area currentArea;

    public void paintComponent(Graphics g){
        //G.fillBackground(g,Color.blue);
        //g.setColor(Color.blue);
        //g.fillRect(100,100,200,300);
        //Color c = Color.blue;
        //theVS.fill(g,theCOLOR);
        //theSQUARE.draw(g);
        theList.draw(g);
    }

    public void mousePressed(MouseEvent me){
        //if(theVS.hit(me.getX(),me.getY())){ theCOLOR = G.rndColor(); }
        int x = me.getX(), y = me.getY();
        firstpressed.set(x,y);
        theSQUARE = theList.hit(x,y);
        currentArea = theSQUARE;
        currentArea.dn(x,y);

        repaint();
    }
    @Override
    public void mouseDragged(MouseEvent me){
        int x = me.getX(), y = me.getY();
        currentArea.drag(x,y);

        repaint();
    }
    public void mouseReleased(MouseEvent me) {
        currentArea.up(me.getX(),me.getY());
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

    public static class Square extends G.VS implements I.Area {

        public Color c = G.rndColor();
        public G.V dv = new G.V(G.rnd(20)-10,G.rnd(20)-10);
        public Square(int x,int y){super(x,y,100,100);}
        public void moveAndBounce(){
            loc.add(dv);
            if(xLow() < 0 && dv.x <0){dv.x = -dv.x;}
            if(yLow() < 0 && dv.y <0){dv.y = -dv.y;}
            if(xHi() > 1000 && dv.x >0){dv.x = -dv.x;}
            if(yHi() > 800 && dv.y >0){dv.y = -dv.y;}
        }

        public void draw(Graphics g){fill(g,c);moveAndBounce();}
        public void resize(int x,int y){size.x = x;size.y = y;}
        public void dn(int x, int y){
            theSQUARE.dv.set(0,0);
            mousePosition.x = x - theSQUARE.loc.x;
            mousePosition.y = y - theSQUARE.loc.y;}
        public void drag(int x, int y){
            theSQUARE.loc.x  = x - mousePosition.x;
            theSQUARE.loc.y = y -mousePosition.y;
        }
        public void up(int x, int y){
            theSQUARE.dv.set(x - firstpressed.x, y - firstpressed.y);
        }

        public static class List extends ArrayList<Square>{
            public void draw(Graphics g){for(Square s: this){s.draw(g);} }
            public Square hit(int x, int y){
                Square result = null;
                for(Square s: this){
                    if(s.hit(x,y)){result = s; }
                }
                return result;
            }
}
}}
