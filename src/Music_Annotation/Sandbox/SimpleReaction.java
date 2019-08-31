package Music_Annotation.Sandbox;

import Music_Annotation.I;
import Music_Annotation.Reaction.*;
import Music_Annotation.UC;
import Music_Annotation.graphicsLib.G;
import Music_Annotation.graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class SimpleReaction extends Window {
    static {
        new Layer("BACK");
        new Layer("FORE");
    }
    public static int SEED  = 1234;
    public SimpleReaction() {
        super("SimpleReaction", UC.screenWidth,UC.screenHeight);
        Reaction.initialReactions.addReaction(new Reaction("SW-SW"){
            @Override
            public int bid(Gesture g) { return 0;}
            @Override
            public void act(Gesture g) {new Box(g.vs);}
        });
        G.RND = new Random(SEED);
        Reaction.initialAction = new I.Act(){
            @Override
            public void act(Gesture g) {
                G.RND = new Random(SEED);
            }
        };
    }

    public void paintComponent(Graphics g){
        G.fillBackground(g, Color.WHITE);
        g.setColor(Color.BLUE);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
        repaint();
    }

    public void mousePressed(MouseEvent me){
        Gesture.AREA.dn(me.getX(),me.getY());repaint();
    }
    public void mouseDragged(MouseEvent me){
        Gesture.AREA.drag(me.getX(),me.getY());repaint();
    }
    public void mouseReleased(MouseEvent me){
        Gesture.AREA.up(me.getX(),me.getY());repaint();
    }

    public static class Box extends Mass{
        public G.VS vs;
        public Color c = G.rndColor();
        public Box(G.VS vs){
            super("BACK");
            this.vs = vs;
            addReaction(new Reaction("S-S") {
                @Override
                public int bid(Gesture g) {
                    int x = g.vs.xMid();
                    int y = g.vs.yLow();
                    if (Box.this.vs.hit(x,y)){
                        return Math.abs(x-Box.this.vs.xMid());
                    }else{
                        return UC.noBid;
                    }
                }

                @Override
                public void act(Gesture g) {
                    Box.this.deleteMass();
                }
            });

            addReaction(new Reaction("DOT") {
                @Override
                public int bid(Gesture g) {
                    int x = g.vs.xMid();
                    int y = g.vs.yLow();
                    if (Box.this.vs.hit(x,y)){
                        return Math.abs(x-Box.this.vs.xMid());
                    }else{
                        return UC.noBid;
                    }
                }

                @Override
                public void act(Gesture g) {
                    c = G.rndColor();
                }
            });
        }

        @Override
        public void show(Graphics g) {
            vs.fill(g,c);
        }
    }

}
