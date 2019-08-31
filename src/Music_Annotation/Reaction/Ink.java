package Music_Annotation.Reaction;

import Music_Annotation.I;
import Music_Annotation.UC;
import Music_Annotation.graphicsLib.G;

import java.awt.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Ink implements I.Show{
    public Norm norm;
    public G.VS vs;
    public static Buffer BUFFER = new Buffer();

    public Ink(){
        norm = new Norm(); // automatically loads from BUFFER
        vs = BUFFER.bbox.getNewVS(); // where the ink was on the screen
    }
    public void show(Graphics g){g.setColor(UC.inkColor); norm.drawAt(g,vs);}
    // ---- Buffer ------
    public static class Buffer extends G.PL implements I.Show, I.Area{
        public static final int MAX = UC.inkBufferMax;
        public int n = 0; // tracks how many points are in the buffer.
        public G.BBox bbox = new G.BBox();
        private Buffer(){super(MAX);}
        public void add(int x, int y){if(n<MAX){points[n].set(x,y); n++; bbox.add(x,y);}}
        public void clear(){n = 0;} // reset the buffer
        public void subSample(G.PL pl){
            int K = pl.points.length;

            for(int i = 0;i<K;i++){pl.points[i].set(points[i*(n-1)/(K-1)]);}
        }
        @Override //--I.Show interface
        public void show(Graphics g){this.drawN(g, n); }//bbox.draw(g);}// draw the n points as a line.
        @Override //--I.Area interface
        public boolean hit(int x, int y){return true;} // any point COULD go into ink
        public void dn(int x, int y){clear(); bbox.set(x,y); add(x,y);} // add first point
        public void drag(int x, int y){add(x,y);} // add each point as it comes is
        public void up(int x, int y){}
    }
    // ---- Norm ----
    public static class Norm extends G.PL implements Serializable {
        public static final int N = UC.normSampleSize, MAX = UC.normCoordMax;//25 subsample
        public static final G.VS CS = new G.VS(0,0,MAX,MAX); // the coordinate box for Transforms
        //CS coordinate system
        public Norm(){
            super(N); // creates the PL
            BUFFER.subSample(this);//subsample buffer into PL
            G.V.T.set(BUFFER.bbox, CS);
            this.transform();
        }
        public void drawAt(Graphics g, G.VS vs){ // expands Norm to fit in vs
            G.V.T.set(CS, vs); // prepare to move from normalized CS to vs. Draw the norm to this place
            for(int i = 1; i<N; i++){
                g.drawLine(points[i-1].tx(), points[i-1].ty(), points[i].tx(), points[i].ty());
            }
        }
        public int dist(Norm n){
            int res = 0;
            for(int i = 0; i<N; i++){
                int dx = points[i].x - n.points[i].x, dy = points[i].y - n.points[i].y;
                res += dx*dx + dy*dy;
            }
            return res;
        }

    }
    // ---- List ------
    public static class List extends ArrayList<Ink> implements I.Show{
        public void show(Graphics g){for(Ink ink : this){ink.show(g);}}
    }
}
