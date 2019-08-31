package Music_Annotation.Reaction;

import Music_Annotation.I;
import Music_Annotation.UC;
import Music_Annotation.graphicsLib.G;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Shape implements Serializable {
    public String name;

    public Prototype.List prototypes = new Prototype.List();

    public Shape(String name){this.name = name;}

    public static class Database extends HashMap<String,Shape>{
        private Database(){
            super ();
            String dot = "DOT";
            put(dot, new Shape(dot));
        }
        private Shape forceShape(String name){
            if(!DB.containsKey(name)){
                put(name,new Shape(name));
            }
            return DB.get(name);
        }
        public void train(String name, Ink.Norm norm){
            if(isLegal(name)){
                forceShape(name).prototypes.train(norm);
            }
        }

        public static boolean isLegal(String name){
            return !name.equals("") && !name.equals("DOT");
        }
    }

    public static Database DB = loadDB();
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values();

    public static Database loadDB(){
        Database res;
//        res.put("DOT", new Shape("DOT"));
//        res.put("1", new Shape("1"));
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(UC.shapeDBFileName));
            res = (Database) ois.readObject();
            System.out.println("Loaded Shape DB.");
        }catch(Exception e){
            System.out.println("Failed loading Shape DB.");
            System.out.println(e);
            res = new Database();
        }
        return res;
    }

    public static void saveDB(){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(UC.shapeDBFileName));
            oos.writeObject(DB);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static Shape recognize(Ink ink){ // can return null
        if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold){
            return DOT;
        }
        Shape bestMatched = null;
        int bestSoFar = UC.noMatchDist;
        for (Shape s: LIST){
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar){
                bestSoFar = d;
                bestMatched = s;
            }
        }
        System.out.println("Saw "+ (bestMatched == null ? "null" :bestMatched.name));
        return bestMatched;
    }

    public static class Prototype extends Ink.Norm implements Serializable{
        public int nBlends = 1;//running average of the points for this shape

        public void blend(Ink.Norm norm){
            for(int i =0; i<N;i++){
                points[i].blend(norm.points[i], nBlends);
            }
            nBlends++;
        }

        public static class List extends ArrayList<Prototype> implements I.Show, Serializable{
            public static Prototype bestMatch;
            public int bestDist(Ink.Norm norm){
                bestMatch = null;
                int bestSoFar = UC.noMatchDist;
                for (Prototype p : this) {
                    int d = p.dist(norm);
                    if(d<bestSoFar){bestSoFar = d; bestMatch = p;}
                }
                return bestSoFar;
            }

            public void train(Ink.Norm norm){
                if(bestDist(norm) < UC.noMatchDist){
                    bestMatch.blend(norm);
                }else{
                    add(new Shape.Prototype());
                }
            }

            private static int m = 10, w = 60;
            private static G.VS showBox = new G.VS(m,m,w,w);
            @Override
            public void show(Graphics g) {
                g.setColor(Color.ORANGE);
                for(int i =0;i < size();i++){
                    int x = (i+1)*m + i*w;
                    showBox.set(x, m, w, w);
                    Prototype p = get(i);
                    p.drawAt(g,showBox);
                    g.drawString("" + p.nBlends, x, m);
                }

            }
        }


    }
}
