package Music_Annotation.Music;

import Music_Annotation.Reaction.Mass;

import java.awt.*;

public abstract class Duration extends Mass {

    private Time time;
    public int nFlag = 0;
    public int nDot = 0;

    public Duration() {
        super("NOTE");
    }

    public abstract void show(Graphics g);

    public void incFlag() {
        if (nFlag < 4) {
            nFlag++;
        }
    }

    public void decFlag() {
        if (nFlag > -2) {
            nFlag--;
        }
    }

    public void cycleDot() {
        nDot++;
        if (nDot > 3) {
            nDot = 0;
        }
    }
}