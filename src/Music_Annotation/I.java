package Music_Annotation;

import Music_Annotation.Reaction.Gesture;
import Music_Annotation.Music.Sys;

import java.awt.*;
import java.util.ArrayList;

public interface I {
    public interface Show{
        public void show(Graphics g);
    }
    public interface Area{
        public boolean hit(int x,int y);
        public void dn(int x, int y);
        public void drag(int x, int y);
        public void up(int x, int y);

    }
    public interface Act{
        public void act(Gesture g);
    }
    public interface React extends Act{
        public int bid(Gesture g);
    }

    public interface Margin {
        public int top();
        public int bot();
        public int left();
        public int right();
    }

    public interface Page extends Margin {
        public Sys.Fmt sysfmt();
        public ArrayList<Sys> systems();
    }

    public interface MusicApp {
        public ArrayList<Page> pages();
        public Sys.Fmt sysfmt(Page page);
        public ArrayList<Sys> systems(Page page);
    }

}
