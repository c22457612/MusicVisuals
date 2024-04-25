package example.screens.IntroVisual;

import processing.core.PApplet;

import example.*;

public class FadingCircle {
    float circleOpacity = 0;
    float circleMaxRadius;
    MyVisual mv; // Reference to the PApplet for drawing and accessing other variables like
                 // frameCount

    public FadingCircle(MyVisual _mv, float circleOpacity, float circleMaxRadius) {
        this.mv = _mv;
        this.circleOpacity = circleOpacity;
        this.circleMaxRadius = circleMaxRadius;
    }

    public void drawFadingCircleWithTiming() {
        if (this.circleOpacity < 255) {
            this.circleOpacity += 5; // Control the speed of the fade-in effect
            PApplet.println(this.circleOpacity);
        }

        // Draw the circle with the current opacity
        int colorValue = (int) (128 + 128 * PApplet.sin(mv.frameCount * 0.05f));
        mv.noFill(); // Do not fill the circle
        mv.stroke(mv.color(255 - colorValue, colorValue, 255), this.circleOpacity); // Set the stroke color and opacity
        mv.strokeWeight(2); // Set the stroke width
        mv.ellipse(mv.width / 2, mv.height / 2, this.circleMaxRadius * 2, this.circleMaxRadius * 2); // Draw the circle
                                                                                                     // centered
    }

}
