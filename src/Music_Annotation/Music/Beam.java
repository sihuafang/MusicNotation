package Music_Annotation.Music;

import Music_Annotation.Reaction.Mass;

import java.awt.*;

public class Beam extends Mass {


    private Stem.List stems = new Stem.List();
    private static Polygon poly;

    static {
        int[] foo = {0, 0, 0, 0};
        poly = new Polygon(foo, foo, 4);
    }

    public static void drawBeamStack(Graphics g, int n1, int n2, int x1, int x2, int h) {
        int y1 = yOfX(x1), y2 = yOfX(x2);
        for (int i = n1; i < n2; i++) {
            setPoly(x1, y1 + i * 2 * h, x2, y2 + i * 2 * h, h);
            g.fillPolygon(poly);
        }
    }

    public void addStem(Stem s) {
        if (s.beam == null) {
            stems.add(s);
            s.beam = this;
            stems.sort();
        }
    }

    public static void setPoly(int x1, int y1, int x2, int y2, int h) {
        int[] a = poly.xpoints;
        a[0] = x1;
        a[1] = x2;
        a[2] = x2;
        a[3] = x1;
//        poly.xpoints[0] = x1; poly.xpoints[1] = x2; poly.xpoints[2] = x2; poly.xpoints[3] = x1;
        a = poly.ypoints;
        a[0] = y1;
        a[1] = y2;
        a[2] = y2 + h;
        a[3] = y1 + h;
//        poly.ypoints[0] = y1; poly.ypoints[1] = y2; poly.ypoints[2] = y2 + h; poly.ypoints[3] = y1 + h;
    }

    public Beam(Stem s1, Stem s2) {
        super("NOTE");
        stems.addStem(s1);
        stems.addStem(s2);
        s1.nFlag = 1;
        s2.nFlag = 1;
        s1.beam = this;
        s2.beam = this;
        stems.sort();
    }

    public static int mX1, mY1, mX2, mY2;

    public static boolean verticalLineCrossSegment(int x, int y1, int y2, int bX, int bY, int eX,
                                                   int eY) {
        if (x < bX || x > eX) {
            return false;
        }
        int y = yOfX(x, bX, bY, eX, eY);
        if (y1 < y2) {
            return y1 < y && y < y2;
        }
        return y2 < y && y < y1;
    }

    public static int yOfX(int x, int x1, int y1, int x2, int y2) {
        int dy = y2 - y1, dx = x2 - x1;
        return (x - x1) * dy / dx + y1;
    }

    public static int yOfX(int x) {
        int dy = mY2 - mY1, dx = mX2 - mX1;
        return (x - mX1) * dy / dx + mY1;
    }

    public static void setMasterBeam(int x1, int y1, int x2, int y2) {
        mX1 = x1;
        mY1 = y1;
        mX2 = x2;
        mY2 = y2;
    }

    public void setMasterBeam() {
        mX1 = first().xStem();
        mY1 = first().yBeamEnd();
        mX2 = last().xStem();
        mY2 = last().yBeamEnd();
    }

    public Stem first() {
        return stems.get(0);
    }

    public Stem last() {
        return stems.get(stems.size() - 1);
    }

    public void deleteBeam() {
        for (Stem s : stems) {
            s.beam = null;
        }
        deleteMass();
    }

    public void removeStem(Stem s) {
        if (s == first() || s == last()) {
            this.deleteBeam();
        } else {
            stems.remove(s);
            stems.sort();
        }
    }

    @Override
    public void show(Graphics g) {
        drawBeamGroup(g);
    }

    public void drawBeamGroup(Graphics g) {
        setMasterBeam();
        Stem s1 = first(), s2 = last();
        int h = s1.staff.H(), sH = s1.isUp ? h : -h;
        int nPrev = 0, nCurr = s1.nFlag, nNext = stems.get(1).nFlag;
        int pX, cX = s1.xStem();
        int bX = 3 * h + cX;
        // Draw Beamlet on the first Stem
        if (nCurr > nNext) {
            drawBeamStack(g, nNext, nCurr, cX, bX, sH);
        }
        for (int cur = 1; cur < stems.size(); cur++) {
            Stem sCurr = stems.get(cur);
            pX = cX;
            cX = sCurr.xStem();
            nPrev = nCurr;
            nCurr = nNext;
            nNext = cur < stems.size() - 1 ? stems.get(cur + 1).nFlag : 0;
            int nBack = Math.min(nPrev, nCurr);
            drawBeamStack(g, 0, nBack, pX, cX, sH); // full beams from prev to curr
            if (nCurr > nPrev && nCurr > nNext) {
                if (nPrev < nNext) {
                    bX = cX + 3 * h;
                    drawBeamStack(g, nNext, nCurr, cX, bX, sH);
                } else {
                    bX = cX - 3 * h;
                    drawBeamStack(g, nPrev, nCurr, bX, cX, sH);
                }
            }
        }
    }
}