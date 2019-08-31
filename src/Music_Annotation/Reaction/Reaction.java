package Music_Annotation.Reaction;

import Music_Annotation.I;
import Music_Annotation.UC;

import java.util.ArrayList;
import java.util.HashMap;
//REMEMBER WHERE all reactions are
public abstract class Reaction implements I.React{
    public Shape shape;
    public static List initialReactions = new List();

    public static I.Act initialAction = new I.Act(){
        @Override
        public void act(Gesture g) {}
    };

    private static Map byShapeMap = new Map();
    public Reaction(String shapeName){
        this.shape = Shape.DB.get(shapeName);
        if(this.shape == null){
            System.out.println("Shape not in DB" + shapeName);
        }
    }

    public void enable(){
        List list = byShapeMap.getList(shape);
        if(!list.contains(this)){
            list.add(this);
        }
    }

    public void disable(){
        List list = byShapeMap.getList(shape);
        list.remove(this);
    }

    public static Reaction best(Gesture gesture){
        return byShapeMap.getList(gesture.shape).lowBid(gesture);
    }

    public static void nuke(){
        byShapeMap = new Map();
        initialReactions.enable();
    }

    public static class List extends ArrayList<Reaction>{
        public void addReaction(Reaction r){
            this.add(r);
            r.enable();
        }
        public void removeReaction(Reaction r){
            this.remove(r);
            r.disable();
        }
        public void clearAll(){
            for(Reaction r : this){
                r.disable();
            }
            this.clear();
        }
        public Reaction lowBid(Gesture gesture){
            Reaction res = null;
            int bestSoFar = UC.noBid;
            for (Reaction r : this){
                int b = r.bid(gesture);
                if(b < bestSoFar){
                    bestSoFar = b;
                    res = r;
                }
            }
            return res;
        }

        public void enable(){
            for(Reaction r: this){
                r.enable();
            }
        }
    }
    public static class Map extends HashMap<Shape, List>{
        public List getList(Shape shape){
            List res = get(shape);
            if(res == null){
                res = new List();
                put(shape,res);
            }
            return res;
        }

    }

}
