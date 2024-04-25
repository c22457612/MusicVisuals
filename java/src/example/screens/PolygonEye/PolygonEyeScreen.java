package example.screens.PolygonEye;

import example.*;

import java.util.ArrayList;
import java.util.List;

// PolygonEyeScreen
public class PolygonEyeScreen {

    MyVisual mv;

    Eye centerEye, leftEye, rightEye; // 3 eyes
    List<Polygon> myPolygons = new ArrayList<Polygon>(); // List of polygons
    float cx; // Center of screen (x)
    float cy; // Center of screen (y)
    int numPolygons = 9; // Num of polygons
    int polygonJump = 92; // Space between 2 consecutive polygons5
    int minSides = 3;
    int maxSides = 30;
    float glowIntensity = 0.5f;

    int t = 0;

    // Constructor
    public PolygonEyeScreen(MyVisual _mv) {

        this.mv = _mv; // MyVisual/PApplet object
        this.cx = mv.width / 2; // Get center (x)
        this.cy = mv.height / 2; // Get center (y)

        mv.fill(255, 255, 255, 255);
        mv.rect(0, 0, mv.width, mv.height);

        // Create 3 eyes
        centerEye = new Eye(mv, cx, cy, 38000, 500, 1200, 400, 20);
        leftEye = new Eye(mv, cx - cx / 3, cy, 10000, 180, 380, 90, 255);
        rightEye = new Eye(mv, cx + cx / 3, cy, 10000, 180, 380, 90, 255);

        // Fill polygons' list
        for (int i = 0; i < numPolygons; i++) {
            Polygon myPolygon = new Polygon(this, mv, cx, cy, 100 + polygonJump * i, 300 + polygonJump * i, 0, 0.4f, 3,
                    30, i,
                    numPolygons);
            myPolygons.add(myPolygon);
        }

    }

    void setup() {
        mv.startMinim();

        String fileName = "pushup.mp3";

        mv.loadAudio(fileName);
    }

    // Render function
    public void render() {
        if (t == 0) {
            setup();
            t++;
        }

        mv.background(0);

        mv.calculateAverageAmplitude();

        centerEye.render();

        // Display each polygon
        for (int i = numPolygons - 1; i >= 0; i--) {
            myPolygons.get(i).render();
        }

        // Display the 3 eyes

        leftEye.render();
        rightEye.render();

    }

    public void keyPressed() {
        if (mv.key == 'd' || mv.key == 'D' || mv.key == 'a' || mv.key == 'A') {
            t--;
            mv.getAudioPlayer().close();
            mv.currScreen++;
            mv.currScreen %= mv.NUM_SCREENS;
        } else if (mv.key == ' ') {
            mv.getAudioPlayer().cue(0);
            mv.getAudioPlayer().play();

        } else if (mv.key == 'm' || mv.key == 'M') {
            this.glowIntensity += 0.05f;
            if (glowIntensity > 0.95f) {
                glowIntensity = 0.95f;
            }
        } else if (mv.key == 'n' || mv.key == 'N') {
            this.glowIntensity -= 0.05f;
            if (glowIntensity < 0.11f) {
                glowIntensity = 0.11f;
            }
        } else if (mv.key == mv.CODED) {
            if (mv.keyCode == mv.UP) {
                this.minSides++;
                if (minSides == maxSides) {
                    minSides = maxSides - 1;
                }
                this.minSides %= maxSides - 2;
            } else if (mv.keyCode == mv.DOWN) {
                this.minSides--;
                if (minSides < 3) {
                    minSides = 3;
                }
            } else if (mv.keyCode == mv.RIGHT) {
                this.maxSides++;
                this.maxSides %= 100;
            } else if (mv.keyCode == mv.LEFT) {
                this.maxSides--;
                if (maxSides < 3) {
                    maxSides = 3;
                }
            }
        }
    }

}