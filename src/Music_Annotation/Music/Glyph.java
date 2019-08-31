package Music_Annotation.Music;

import Music_Annotation.UC;

import java.awt.*;

public class Glyph{
    // Offsets & Scales for Sinfonia True Type Font
    public static String fontName = UC.FontName;

    public static Glyph BRACE = new Glyph((char)61473, 16, 0, 4);

    public static Glyph CLEF_G = new Glyph((char)61479, 16, -3, 1);
    public static Glyph CLEF_F = new Glyph((char)61480, 16, -3, 4);
    public static Glyph CLEF_C = new Glyph((char)61481, 16, -3, 4);
    public static Glyph CLEF_TAB = new Glyph((char)61581, 10, -1, 1);
    public static Glyph CLEF_G8 = new Glyph((char)61639, 16, -3, 1);

    public static Glyph HEAD_HALF = new Glyph((char)61485, 16, 0, 7);
    public static Glyph HEAD_W = new Glyph((char)61484, 16, 0, 7);
    public static Glyph HEAD_Q = new Glyph((char)61486, 16, 0, 7);

    public static Glyph FLAG1D = new Glyph((char)61487, 16, 0, 8);
    public static Glyph FLAG1U = new Glyph((char)61488, 16, 0, 1);
    public static Glyph FLAG2D = new Glyph((char)61489, 16, 0, 8);
    public static Glyph FLAG2U = new Glyph((char)61490, 16, 0, 1);
    public static Glyph FLAG3D = new Glyph((char)61491, 16, 0, 8);
    public static Glyph FLAG3U = new Glyph((char)61492, 16, 0, -1);
    public static Glyph FLAG4D = new Glyph((char)61493, 16, 0, 8);
    public static Glyph FLAG4U = new Glyph((char)61494, 16, 0, -3);

    public static Glyph REST_W = new Glyph((char)61499, 16, 0, 6);
    public static Glyph REST_H = new Glyph((char)61499, 16, 0, 7);
    public static Glyph REST_Q = new Glyph((char)61501, 16, 0, 5);
    public static Glyph REST_1F = new Glyph((char)61502, 16, 0, 6);
    public static Glyph REST_2F = new Glyph((char)61503, 16, 0, 6);
    public static Glyph REST_3F = new Glyph((char)61504, 16, 0, 4);
    public static Glyph REST_4F = new Glyph((char)61505, 16, 0, 4);

    public static int theSize = -1;
    public static Font theFont;

    public char code;
    public double scale, dx, dy;

    private Glyph(char code, double scale, double dx, double dy){
        this.code = code;
        this.scale = scale; this.dx = dx; this.dy = dy;
    }

    public void showAt(Graphics g, int H, int x, int y){
        int size = (int)scale*H;
        if(size != theSize){
            theFont = new Font(fontName, 0, size);
            theSize = size;
        }
        Font f = g.getFont(); // fetch old font so that we can restore it
//        g.setColor(Color.BLACK);
        g.setFont(theFont);
        g.drawString(""+code, x + (int)Math.floor(dx*H), y + (int)Math.floor(dy*H));
//        g.setColor(Color.RED);
//        g.drawRect(xStem, y, H, H);
        g.setFont(f); // restore old font
    }
}