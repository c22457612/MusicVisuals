package example.screens.PolygonEye;

import example.*;

public class Polygon extends Drawable {

    // Circle container
    float minR;
    float maxR;
    float r = 100;
    float cx;
    float cy;

    // Amplitude
    float minAmp = 0;
    float maxAmp = 0.35f;
    float amp = 0;

    // Angle var
    float ang = 0;

    // Sides
    float maxSides = 30;
    float minSides = 3;
    int numSides;
    float totalAng;

    float hue = 0;

    int i;

    public Polygon(MyVisual _mv, float cx, float cy, float minR, float maxR, float minAmp, float maxAmp, float minSides,
            float maxSides, int i, int num) {

        this.mv = _mv;
        this.cx = cx;
        this.cy = cy;
        this.minR = minR;
        this.maxR = maxR;
        this.minAmp = minAmp;
        this.maxAmp = maxAmp;
        this.minSides = minSides;
        this.maxSides = maxSides;
        this.i = i;
        this.totalAng = i * (mv.TWO_PI / num);
        this.hue = (5 * i) % 360;
    }

    // Polygon with variable num of sides
    public void render() {

        mv.translate(0, 0); // Move the origin to the center of the window
        float angle = mv.radians(mv.frameCount); // Convert frame count to radians for continuous rotation

        // Get num of sides of polygon based on avg amplitude
        numSides = (int) mv.map(mv.getSmoothedAmplitude(), minAmp, maxAmp, minSides, maxSides);
        // mv.rotateZ(mv.frameCount); // Rotate around the Z-axis

        // Angle
        ang = mv.TWO_PI / numSides;

        totalAng++; // Random point of start
        float cos, sin;
        float x, y;

        // Color
        // mv.colorMode(mv.HSB);
        mv.colorMode(mv.HSB, 360, 100, 100, 100);
        hue += 5;
        mv.noFill();
        // mv.fill(hue % 360, 255, 255);

        int numStrokes = 9;
        int weightStroke = 5;

        r = mv.map(mv.getSmoothedAmplitude(), minAmp, maxAmp, minR, maxR) - numStrokes * weightStroke;

        mv.strokeWeight(weightStroke);

        for (int i = -numStrokes; i < numStrokes; i++) {

            if (i < 0) {
                mv.stroke(hue % 360, 255, 255,
                        mv.map(mv.getSmoothedAmplitude(), minAmp, maxAmp, 0.1f, 0.5f)
                                * mv.map(i, -numStrokes, 0, 0, 255));
            } else if (i > 0) {
                mv.stroke(hue % 360, 255, 255,
                        mv.map(mv.getSmoothedAmplitude(), minAmp, maxAmp, 0.1f, 0.5f)
                                * (255 - mv.map(i, 0, numStrokes, 0, 255)));
            } else {
                mv.stroke(hue % 360, 255, 255, 255);
            }

            // Begin shape
            mv.beginShape();
            for (int s = 0; s < numSides + 1; s++) {

                // Get cos and sin
                cos = mv.cos(totalAng);
                sin = mv.sin(totalAng);

                // Get cordinates
                x = mv.map(cos, -1, 1, cx - r, cx + r);
                y = mv.map(sin, -1, 1, cy - r, cy + r);

                // Reginster point/vertex
                mv.vertex(x, y);

                // Sum angle
                totalAng += ang;

            }

            // End shape
            mv.endShape();

            totalAng %= mv.TWO_PI;
            r += weightStroke;
        }

    }

}