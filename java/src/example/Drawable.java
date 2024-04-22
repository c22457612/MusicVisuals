package example;

import ddf.minim.*;
import processing.core.PApplet;

// Class to form array of screens and standarize requirements for each screen
public abstract class Drawable {

    public ScreenIndex sIndex;
    public MyVisual mv;
    Minim minim;
    PApplet applet;
    
    public Drawable(MyVisual mv) {
        this.mv = mv;
        this.applet = mv; // Assume MyVisual is a subtype of PApplet
    }
    
    // Abstract because although render method is mandatory, it is defined by
    // 'child' class
    public abstract void render();
}
