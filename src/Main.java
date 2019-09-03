import Music_Annotation.Sandbox.Music2;
import Music_Annotation.Sandbox.PaintInk;
import Music_Annotation.Sandbox.ShapeTrainer;
import Music_Annotation.Sandbox.SimpleReaction;
import Music_Annotation.Sandbox.Spline;
import Music_Annotation.Sandbox.Squares;
import Music_Annotation.graphicsLib.Window;

public class Main {
    public static void main(String[] args){
//        Window.PANEL = new Squares();
//        Window.PANEL = new PaintInk();
//        Window.PANEL = new ShapeTrainer();
//        Window.PANEL = new SimpleReaction();
        Window.PANEL = new Music2();
//        Window.PANEL = new Spline();
        Window.launch();

    }
}
