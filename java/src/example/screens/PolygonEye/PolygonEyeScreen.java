package example.screens.PolygonEye;

import example.MyVisual;
import example.ScreenIndex;
import example.Drawable;

import java.util.ArrayList;
import java.util.List;

// This is an example of a visual that renders the Visual1
public class PolygonEyeScreen extends Drawable {

    Eye centerEye, leftEye, rightEye; // 3 eyes
    List<Polygon> myPolygons = new ArrayList<Polygon>(); // List of polygons
    float cx; // Center of screen (x)
    float cy; // Center of screen (y)
    int numPolygons = 9; // Num of polygons
    int polygonJump = 92; // Space between 2 consecutive polygons5

    // Constructor
    public PolygonEyeScreen(ScreenIndex _sIndex, MyVisual _mv) {
        this.sIndex = _sIndex; // Screen index
        this.mv = _mv; // MyVisual/PApplet object
        this.cx = mv.width / 2; // Get center (x)
        this.cy = mv.height / 2; // Get center (y)

        // Create 3 eyes
        centerEye = new Eye(mv, cx, cy, 38000, 500, 1200, 400, 20);
        leftEye = new Eye(mv, cx - cx / 3, cy, 10000, 180, 380, 90, 255);
        rightEye = new Eye(mv, cx + cx / 3, cy, 10000, 180, 380, 90, 255);

        // Fill polygons' list
        for (int i = 0; i < numPolygons; i++) {
            Polygon myPolygon = new Polygon(mv, cx, cy, 100 + polygonJump * i, 300 + polygonJump * i, 0, 0.4f, 3, 30, i,
                    numPolygons);
            myPolygons.add(myPolygon);
        }

    }

    // Render function
    public void render() {

        centerEye.drawEye();

        // Display each polygon
        for (int i = numPolygons - 1; i >= 0; i--) {
            myPolygons.get(i).drawPolygon();
        }

        // Display the 3 eyes

        leftEye.drawEye();
        rightEye.drawEye();

    }

}