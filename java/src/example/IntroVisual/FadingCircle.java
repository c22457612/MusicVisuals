package example.IntroVisual;

import processing.core.PApplet;

public class FadingCircle {
    float circleOpacity=0;
    float circleMaxRadius;
    IntroVisualScreen parent;  // Reference to the PApplet for drawing and accessing other variables like frameCount

    public FadingCircle(IntroVisualScreen introVisualScreen,float circleOpacity,float circleMaxRadius) {
        this.parent = introVisualScreen;
        this.circleOpacity=circleOpacity;
        this.circleMaxRadius=circleMaxRadius;
    }

    public void drawFadingCircleWithTiming() {
        if (this.circleOpacity < 255) {
            this.circleOpacity += 5; // Control the speed of the fade-in effect
            PApplet.println(this.circleOpacity);
        }
    
        // Draw the circle with the current opacity
        int colorValue = (int) (128 + 128 * PApplet.sin(parent.mv.frameCount * 0.05f));
        parent.mv.noFill(); // Do not fill the circle
        parent.mv.stroke(parent.mv.color(255 - colorValue, colorValue, 255), this.circleOpacity); // Set the stroke color and opacity
        parent.mv.strokeWeight(2); // Set the stroke width
        parent.mv.ellipse(parent.mv.width / 2, parent.mv.height / 2, this.circleMaxRadius * 2, this.circleMaxRadius * 2); // Draw the circle centered
    }

}
