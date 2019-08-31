package Music_Annotation.Music;

import Music_Annotation.Reaction.Gesture;
import Music_Annotation.Reaction.Mass;
import Music_Annotation.Reaction.Reaction;
import Music_Annotation.UC;

import java.awt.*;

public class Bar extends Mass {

    private Sys sys;
    private int x, barType;
    private int LEFT = 4, RIGHT = 8;

    public void toggleLeft() {
        barType = barType ^ LEFT;
    }

    public void toggleRight() {
        barType = barType ^ RIGHT;
    }

    public Bar(Sys sys, int x) {
        super("BACK");
        this.sys = sys;
        this.x = x;
        if (Math.abs(x - sys.page.right()) < UC.barToMarginSnap) {
            this.x = sys.page.right();
        }
        this.barType = 0;
        addReaction(new Reaction("S-S") {//cycling the bartype
            @Override
            public int bid(Gesture gesture) {
                int x = gesture.vs.xMid();
                if (Math.abs(x - Bar.this.x) > UC.barToMarginSnap) {
                    return UC.noBid;
                }
                int y1 = gesture.vs.yLow(), y2 = gesture.vs.yHi();

                if (y1 < Bar.this.sys.yTop() - 20 || y2 > Bar.this.sys.yBot() + 20) {
                    return UC.noBid;
                }
                return Math.abs(x - Bar.this.x);
            }

            @Override
            public void act(Gesture gesture) {
                Bar.this.cycleType();
            }
        });

        addReaction(new Reaction("DOT") { // Dot this Bar
            public int bid(Gesture g) {
                int x = g.vs.xMid();
                int y = g.vs.yMid();
                if (y < Bar.this.sys.yTop() || y > Bar.this.sys.yBot()) {
                    return UC.noBid;
                }
                int dist = Math.abs(x - Bar.this.x);
                if (dist > 3 * Bar.this.sys.page.sysfmt().maxH) {
                    return UC.noBid;
                }
                return dist;
            }

            public void act(Gesture g) {
                if (g.vs.xMid() < Bar.this.x) {
                    Bar.this.toggleLeft();
                } else {
                    Bar.this.toggleRight();
                }
            }
        });

    }

    public void cycleType() {
        barType++;
        if (barType > 2) {
            barType = 0;
        }
    }

    public static void wings(Graphics g, int x, int y1, int y2, int dx, int dy) {
        g.drawLine(x, y1, x + dx, y1 - dy);
        g.drawLine(x, y2, x + dx, y2 + dy);
    }

    public static void fatBar(Graphics g, int x, int y1, int y2, int dx) {
        g.fillRect(x, y1, dx, y2 - y1);
    }

    public static void thinBar(Graphics g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
    }

    public void drawLines(Graphics g, int x, int y1, int y2) {
        int H = sys.page.sysfmt().maxH;
        if (barType == 0) {
            thinBar(g, x, y1, y2);
        }
        if (barType == 1) {
            thinBar(g, x, y1, y2);
            thinBar(g, x - H, y1, y2);
        }
        if (barType == 2) {
            fatBar(g, x - H, y1, y2, H);
            thinBar(g, x - 2 * H, y1, y2);
        }
        if (barType >= 4) {
            fatBar(g, x - H, y1, y2, H); // all repeats have fat bar
            if ((barType & LEFT) != 0) {
                thinBar(g, x - 2 * H, y1, y2);
                wings(g, x - 2 * H, y1, y2, -H, H);
            }
            if ((barType & RIGHT) != 0) {
                thinBar(g, x + H, y1, y2);
                wings(g, x + H, y1, y2, H, H);
            }
        }
    }

    public void drawDots(Graphics g, int x, int top) { // from top of single staff
        // notice - this code ASSUMES nLine is 5. We will need to fix if we ever allow
        // not-standard staffs.
        int H = sys.page.sysfmt().maxH;
        if ((barType & LEFT) != 0) {
            g.fillOval(x - 3 * H, top + 11 * H / 4, H / 2, H / 2);
            g.fillOval(x - 3 * H, top + 19 * H / 4, H / 2, H / 2);
        }
        if ((barType & RIGHT) != 0) {
            g.fillOval(x + 3 * H / 2, top + 11 * H / 4, H / 2, H / 2);
            g.fillOval(x + 3 * H / 2, top + 19 * H / 4, H / 2, H / 2);
        }
    }

    public void show(Graphics g) {
        int yTop = sys.yTop(), y1 = 0, y2 = 0; // y1,y2 mark top and bot of connected component
        boolean justSawBreak = true; // signals when we are at the top of a new connected component
        for (Staff.Fmt sf : sys.page.sysfmt()) {
            if (justSawBreak) {
                y1 = yTop + sf.dy;
            } // remember start of connected component
            int top = yTop + sf.dy; // top of this staff
            y2 = top + sf.height();  // bottom of this staff
            if (!sf.barContinues) { // we now have a connected component from y1 to y2
//                        if(y1 == y2){y1 -= 2*H; y2 += 2*H;} // this is a fix for isolated drum, single line staffs.
                //.. without that fix, you can't see staff lines on a drum staff.
                drawLines(g, x, y1, y2);  // lines show only at end of connected components
            }
            justSawBreak = !sf.barContinues;
            if (barType > 3) {
                drawDots(g, x, top);
            }
        }
    }

}