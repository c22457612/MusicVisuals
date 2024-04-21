package example;

import ddf.minim.*;

// Class to form array of screens and standarize requirements for each screen
public abstract class Drawable {

    public ScreenIndex sIndex;
    public MyVisual mv;
    Minim minim;

    // Abstract because although render method is mandatory, it is defined by
    // 'child' class
    public abstract void render();
}
