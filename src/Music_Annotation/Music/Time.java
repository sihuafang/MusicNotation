package Music_Annotation.Music;

import Music_Annotation.UC;

import java.util.ArrayList;

public class Time {

    public int x;
    public ArrayList<Head> heads = new ArrayList<>();

    private Time(int x, Sys sys) {
        this.x = x;
        sys.addTime(this);
    }

    public void unStemHeads(int y1, int y2) {
        for (Head h : heads) {
            int y = h.y();
            if (y > y1 && y < y2) {
                h.unStem();
            }
        }
    }

    public void addHead(Head head) { heads.add(head); }

    public void removeHead(Head head) { heads.remove(head); }


    // -------------List --------
    public static class List extends ArrayList<Time> {

        public Sys sys;

        public List(Sys sys) {
            this.sys = sys;
        }

        public Time getTime(int x) {
            if (size() == 0) {
                return new Time(x, sys);
            }
            Time t = getClosestTime(x);
            if (Math.abs(x - t.x) < UC.snapTime) {
                return t;
            } else {
                return new Time(x, sys);
            }
        }

        public Time getClosestTime(int x) {
            Time result = get(0);
            int bestSoFar = Math.abs(x - result.x);
            for (Time t : this) {
                int dist = Math.abs(x - t.x);
                if (dist < bestSoFar) {
                    bestSoFar = dist;
                    result = t;
                }
            }
            return result;
        }
    }
}