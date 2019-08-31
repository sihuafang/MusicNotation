package Music_Annotation;

import java.awt.*;

public class UC {
    public static final int screenHeight = 800;
    public static final int screenWidth = 1000;
    public static final int inkBufferMax = 800;
    public static final int normSampleSize = 25;
    public static int normCoordMax = 500;
    public static int noMatchDist = 500000;//Based on norm sample size of 200 and coord Max of 500
    public static Color inkColor = Color.BLUE;
    public static final int dotThreshold = 6;
    public static final String shapeDBFileName = "/Users/nidingfan/Downloads/Music_Annotation/ShapeDB.bin";
    public static int noBid = 10000;
    public static int defaultStaffLineSpace = 8;
    public static int barToMarginSnap = 20;
    public static String FontName = "sinfonia";
    public static int snapTime = 20;
    public static int restFirstDot = 40;
    public static int dotSpace = 10;

}
