package Music_Annotation.Music;

import Music_Annotation.Reaction.Gesture;
import Music_Annotation.Reaction.Reaction;
import Music_Annotation.UC;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Stem extends Duration implements Comparable<Stem> {

    public ArrayList<Head> heads = new ArrayList<>();
    public boolean isUp;
    public Staff staff;
    public Beam beam = null;

    public Stem(Staff staff, ArrayList<Head> heads, boolean isUp) {
        super();
        this.staff = staff;
        this.isUp = isUp;
        for (Head h : heads) {
            h.unStem();
            h.stem = this;
        }
        this.heads = heads;
        staff.sys.addStem(this);
        setWrongSide();
//        staff.sys.stems.addStem(this); this is a bug. Stem is added in stemHead().
        addReaction(new Reaction("E-E") { // add flag
            @Override
            public int bid(Gesture g) {
                int x1 = g.vs.xLow(), x2 = g.vs.xHi(), y = g.vs.yMid();
                int xS = Stem.this.heads.get(0).time.x;
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHi();
                if (x1 > xS || x2 < xS) {
                    return UC.noBid;
                }
                if (y < y1 || y > y2) {
                    return UC.noBid;
                }
                return 60 + Math.abs(y - (y1 + y2) / 2);
            }

            @Override
            public void act(Gesture g) {
                Stem.this.incFlag();
            }
        });
        addReaction(new Reaction("W-W") { // decrease flag
            @Override
            public int bid(Gesture g) {
                int x1 = g.vs.xLow(), x2 = g.vs.xHi(), y = g.vs.yMid();
                int xS = Stem.this.heads.get(0).time.x;
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHi();
                if (x1 > xS || x2 < xS) {
                    return UC.noBid;
                }
                if (y < y1 || y > y2) {
                    return UC.noBid;
                }
                return 60 + Math.abs(y - (y1 + y2) / 2);
            }

            @Override
            public void act(Gesture g) {
                Stem.this.decFlag();
            }
        });
    }

    public void removeHead(Head head) { head.remove(head); }

    @Override
    public int compareTo(Stem stem) {
        return xStem() - stem.xStem();
    }

    public void deleteStem() {
        staff.sys.removeStem(this);
        if (beam != null) {
            beam.removeStem(this);
        }
        deleteMass();
    }

    public static Stem getStem(Staff staff, Time time, int y1, int y2, boolean up) {
        ArrayList<Head> heads = new ArrayList<>();
        for (Head h : time.heads) {
            int yH = h.y();
            if (yH > y1 && yH < y2) {
                heads.add(h);
            }
        }
        if (heads.size() == 0) {
            return null;
        }
        Beam beam = internalStem(staff.sys, time.x, y1, y2);
        Stem res = new Stem(staff, heads, up);
        if (beam != null) {
            beam.addStem(res);
            res.nFlag = 1;
        }
        return res;
    }

    public static Beam internalStem(Sys sys, int x, int y1, int y2) {
        for (Stem s : sys.stems) {
            if (s.beam != null && s.xStem() < x && s.yLow() < y2 && s.yHi() > y1) {
                int bX = s.beam.first().xStem(), bY = s.beam.first().yBeamEnd();
                int eX = s.beam.last().xStem(), eY = s.beam.last().yBeamEnd();
                if (Beam.verticalLineCrossSegment(x, y1, y2, bX, bY, eX, eY)) {
                    return s.beam;
                }
            }
        }
        return null;
    }

    @Override
    public void show(Graphics g) {
        if (nFlag > -2 && heads.size() > 0) {
            int x = xStem(), yH = yFirstHead(), yB = yBeamEnd(), h = staff.H();
            g.drawLine(x, yH, x, yB);
            if (nFlag > 0 && beam == null) {
                if (nFlag == 1) {
                    (isUp ? Glyph.FLAG1D : Glyph.FLAG1U).showAt(g, h, x, yB);
                }
                if (nFlag == 2) {
                    (isUp ? Glyph.FLAG2D : Glyph.FLAG2U).showAt(g, h, x, yB);
                }
                if (nFlag == 3) {
                    (isUp ? Glyph.FLAG3D : Glyph.FLAG3U).showAt(g, h, x, yB);
                }
                if (nFlag == 4) {
                    (isUp ? Glyph.FLAG4D : Glyph.FLAG4U).showAt(g, h, x, yB);
                }
            }
        }
    }

    public Head firstHead() {
        return heads.get(isUp ? heads.size() - 1 : 0);
    }

    public Head lastHead() {
        return heads.get(isUp ? 0 : heads.size() - 1);
    }

    public int yLow() {
        return isUp ? yBeamEnd() : yFirstHead();
    }

    public int yHi() {
        return isUp ? yFirstHead() : yBeamEnd();
    }

    public int xStem() {
        if (heads.size() == 0) {
            return 100;
        }
        Head h = firstHead();
        return h.time.x + (isUp ? h.w() : 0);
    }

    public int yFirstHead() {
        if (heads.size() == 0) {
            return 100;
        }
        Head h = firstHead();
        return h.staff.yLine(h.line);
    }

    public int yBeamEnd() {
        if (heads.size() == 0) {
            return 100;
        }
        if (beam == null || beam.first() == this || beam.last() == this) {
            Head h = lastHead();
            int line = h.line;
            line += isUp ? -7 : 7;
            int flagInc = nFlag > 2 ? 2 * (nFlag - 2) : 0;
            line += isUp ? -flagInc : flagInc;
            if (isUp && line > 4 || !isUp && line < 4) {
                line = 4;
            }
            return staff.yLine(line);
        } else {
            beam.setMasterBeam();
            return Beam.yOfX(xStem());
        }
    }

    public void setWrongSide() {
        Collections.sort(heads);
        int i, last, next;
        if (isUp) {
            i = heads.size() - 1;
            last = 0;
            next = -1;
        } else {
            i = 0;
            last = heads.size() - 1;
            next = 1;
        }
        Head pH = heads.get(i);
        pH.wrongSide = false;
        while (i != last) {
            i += next;
            Head nH = heads.get(i);
            nH.wrongSide = nH.staff == pH.staff && Math.abs(nH.line - pH.line) == 1 && !pH.wrongSide;
            pH = nH;
        }
    }

    public static class List extends ArrayList<Stem> {

        public int yMin = 10000000, yMax = -10000000;

        public void addStem(Stem s) {
            add(s);
            int yF = s.yFirstHead(), yB = s.yBeamEnd();
            if (yF < yMin) {
                yMin = yF;
            }
            if (yF > yMax) {
                yMax = yF;
            }
            if (yB < yMin) {
                yMin = yB;
            }
            if (yB > yMax) {
                yMax = yB;
            }
        }

        public boolean fastReject(int y1, int y2) {
            return y1 > yMax || y2 < yMin;
        }

        public ArrayList<Stem> allIntersectors(int x1, int y1, int x2, int y2) {
            ArrayList<Stem> res = new ArrayList<>();
            for (Stem s : this) {
                int x = s.xStem(), y = Beam.yOfX(x, x1, y1, x2, y2);
                if (x > x1 && x < x2 && y > s.yLow() && y < s.yHi()) {
                    res.add(s);
                }
            }
            System.out.println("intersectors: " + res.size());
            return res;
        }

        public void sort() {
            Collections.sort(this);
        }
    }
}