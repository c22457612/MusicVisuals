package example.IntroVisual;

import processing.core.PApplet;
import ddf.minim.analysis.FFT;

public class DrawSphere {
    PApplet parent;
    FFT fft;
    float sphereRadius;
    boolean startDrawingShapes;
    boolean[] modes;
    boolean extremeColour;
    float angle = 0; // Rotation angle for the sphere

    // Constructor
    public DrawSphere(PApplet parent, FFT fft, float sphereRadius, boolean startDrawingShapes, boolean[] modes, boolean extremeColour) {
        this.parent = parent;
        this.fft = fft;
        this.sphereRadius = sphereRadius;
        this.startDrawingShapes = startDrawingShapes;
        this.modes = modes;
        this.extremeColour = extremeColour;
    }

    public void draw(float x, float y) {
        parent.pushMatrix(); // Save the current state of transformations
        parent.translate(x, y); // Use the dynamic `y` for y-position

        if (!startDrawingShapes){
            angle += 0.01; // Continuously rotate the sphere
        } else if (modes[0] && startDrawingShapes) {
            angle += 0.001;
        }

        if (parent.millis() > 1000 || !startDrawingShapes) { // Modify condition based on your setup
            parent.rotateX(angle);

            float bassAmplitude = 0, trebleAmplitude = 0;
            int bassCount = 0, trebleCount = 0;

            for (int i = 0; i < fft.specSize(); i++) {
                float freq = fft.indexToFreq(i);
                float amplitude = fft.getBand(i);

                if (freq < 150) {
                    bassAmplitude += amplitude;
                    bassCount++;
                } else if (freq > 4000) {
                    trebleAmplitude += amplitude;
                    trebleCount++;
                }
            }

            float hue = (trebleCount > 0) ? PApplet.map(trebleAmplitude / trebleCount, 0, 10, 0, 360) : 0;
            float strokeWeightValue = PApplet.map(trebleAmplitude, 0, 10, 0.5f, 15);
            parent.noFill();
            parent.strokeWeight(strokeWeightValue);
            parent.stroke(hue, 100, 100);

            // Draw the sphere with the constant radius
            parent.sphere(sphereRadius);
        } else {
            parent.noFill();
            parent.stroke(255);
            parent.strokeWeight(1);
            parent.sphere(sphereRadius);
        }

        parent.popMatrix(); // Restore original state of transformations
    }
}
