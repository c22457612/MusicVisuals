package example;

import ie.tudublin.*;

import example.*;
import example.screens.IntroVisual.IntroVisualScreen;
import example.screens.PolygonEye.PolygonEyeScreen;

public class MyVisual extends Visual {

    public int NUM_SCREENS = 2;

    PolygonEyeScreen polygonEyeScreen;
    IntroVisualScreen introVisual;

    float offset = height / 0.6f;

    public int currScreen = 0;

    // WaveForm wf;

    public void settings() {
        fullScreen(P3D);

        // Use this to make fullscreen
        // fullScreen();

        // Use this to make fullscreen and use P3D for 3D graphics
        // fullScreen(P3D, SPAN);

    }

    public void setup() {
        startMinim();

        colorMode(HSB);

        introVisual = new IntroVisualScreen(this);
        polygonEyeScreen = new PolygonEyeScreen(this);

    }

    public void draw() {
        if (currScreen == 0) {
            introVisual.render();
        } else if (currScreen == 1) {
            polygonEyeScreen.render();
        }
    }

    public void keyPressed() {
        if (currScreen == 0) {
            introVisual.keyPressed();
        } else if (currScreen == 1) {
            polygonEyeScreen.keyPressed();
        }

    }

    public void mouseClicked() {
        introVisual.mouseClicked();
    }
}