package example.screens.IntroVisual;

import processing.core.PApplet;

public class FadingCircle {
    float circleOpacity=0;
    float circleMaxRadius;
    PApplet parent;  // Reference to the PApplet for drawing and accessing other variables like frameCount

    public FadingCircle(PApplet parent,float circleOpacity,float circleMaxRadius) {
        this.parent = parent;
        this.circleOpacity=circleOpacity;
        this.circleMaxRadius=circleMaxRadius;
    }

    public void drawFadingCircleWithTiming() {
        if (this.circleOpacity < 255) {
            this.circleOpacity += 5; // Control the speed of the fade-in effect
            PApplet.println(this.circleOpacity);
        }
    
        // Draw the circle with the current opacity
        int colorValue = (int) (128 + 128 * PApplet.sin(parent.frameCount * 0.05f));
        parent.noFill(); // Do not fill the circle
        parent.stroke(parent.color(255 - colorValue, colorValue, 255), this.circleOpacity); // Set the stroke color and opacity
        parent.strokeWeight(2); // Set the stroke width
        parent.ellipse(parent.width / 2, parent.height / 2, this.circleMaxRadius * 2, this.circleMaxRadius * 2); // Draw the circle centered
    }

}
