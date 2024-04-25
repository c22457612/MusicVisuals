package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

import example.*;

public class Pyramids {
    // Pyramid properties
    private MyVisual mv; // Reference to the PApplet (to use Processing's drawing functions)
    private FFT fft; // Reference to the FFT object for audio analysis
    float rotationSpeed;
    float pyramidRotation;
    float pyramidXPosTop;
    float pyramidXPosBottom;
    float pyramidMoveSpeed;
    float currentRotationY;
    float transparentColour;
    AudioPlayer song;
    float pyramidFillAlpha;
    float pyramidSize;
    boolean playIntro;
    boolean startDrawingShapes;
    boolean[] modes;
    boolean extremeColour;
    boolean pyramidsVisible;
    boolean spinning;
    boolean fillActivated;

    // Constructor
    public Pyramids(MyVisual _mv, FFT fft, boolean spinning, float rotationSpeed, float pyramidRotation,
            float pyramidXPosTop, float pyramidXPosBottom, float pyramidMoveSpeed, boolean pyramidsVisible,
            float currentRotationY, AudioPlayer song, float pyramidFillAlpha, float pyramidSize, boolean playIntro,
            boolean startDrawingShapes, boolean[] modes, boolean extremeColour, float transparentColour,
            boolean fillActivated) {
        this.mv = _mv;
        this.fft = fft;
        this.spinning = spinning;
        this.rotationSpeed = rotationSpeed;
        this.pyramidRotation = pyramidRotation;
        this.pyramidXPosTop = pyramidXPosTop;
        this.pyramidXPosBottom = pyramidXPosBottom;
        this.pyramidMoveSpeed = pyramidMoveSpeed;
        this.pyramidsVisible = pyramidsVisible;
        this.currentRotationY = currentRotationY;
        this.song = song;
        this.modes = modes;
        this.transparentColour = transparentColour;
        this.pyramidSize = pyramidSize;
    }

    // Method to draw the pyramid
    public void drawPyramids() {

        if (this.spinning) {
            // PApplet.println("in spinning");
            // println(transparentColour);
            if (this.rotationSpeed < 0.05) {
                this.rotationSpeed += 0.0001;
            }
            this.pyramidRotation += this.rotationSpeed; // Keep rotating the pyramids continuously
            if (PApplet.abs(pyramidXPosTop) < mv.width / 3) {
                this.pyramidXPosTop -= this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            if (PApplet.abs(pyramidXPosBottom) < mv.width / 3) {
                this.pyramidXPosBottom += this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            this.currentRotationY += this.rotationSpeed;
            if (this.transparentColour > 0) {
                this.transparentColour -= 0.5;
            }
        }

        if (this.song.isPlaying()) { // rotate
            if (this.rotationSpeed < 0.05) {
                this.rotationSpeed += 0.0001;
            }
            this.pyramidRotation += this.rotationSpeed; // Keep rotating the pyramids continuously
            if (PApplet.abs(pyramidXPosTop) < mv.width / 2) {
                this.pyramidXPosTop -= this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            if (PApplet.abs(pyramidXPosBottom) < mv.width / 2) {
                this.pyramidXPosBottom += this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            this.currentRotationY += this.rotationSpeed;
            if (this.transparentColour > 0) {
                this.transparentColour -= 1;
            } else {// else stay
                this.pyramidsVisible = true;
            }
            this.pyramidFillAlpha += 2; // Increase alpha gradually
            this.pyramidFillAlpha = PApplet.constrain(pyramidFillAlpha, 0, 255); // Limit alpha to max 255

        }

        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }

        // Top pyramid and its additional bottom pyramid
        mv.pushMatrix();
        mv.translate(mv.width / 2 + this.pyramidXPosTop, mv.height / 2, -200);
        mv.rotateY(pyramidRotation);

        // Draw the top pyramid (inverted)
        mv.pushMatrix();
        mv.translate(0, -this.pyramidSize, 0);
        mv.rotateX(PConstants.PI);
        if (this.playIntro && !this.startDrawingShapes) { // intro
            mv.fill(totalAmplitude, 50, totalAmplitude, this.transparentColour);
            // println(transparentColour);
            // PApplet.println("in intro"); //debugging statement
            // PApplet.println(totalAmplitude);
        }
        if (this.modes[0] && this.startDrawingShapes)// colour scheme for mode 0
        {
            // PApplet.println("in visualizer");
            if (this.extremeColour) {
                float hue = PApplet.map(totalAmplitude, 0, 2000, 40, 180); // Ranges from all colours aggresively
                hue = hue % 360; // Ensure the hue wraps around correctly
                mv.fill(hue, 100, 100);
                // println("extreme colours");
            } else if (!this.extremeColour) {
                float hue = PApplet.map(totalAmplitude, 0, 2000, 240, 360); // Ranges from purple to pink aggresively
                hue = hue % 360; // Ensure the hue wraps around correctly
                mv.fill(hue, 100, 100);
                // println("not extreme colours");
            }

        } else if (this.modes[1]) {
            // println(totalAmplitude); //debugging statement
            if (totalAmplitude > 1500) {
                mv.fill(totalAmplitude / 2, 50, totalAmplitude / 4, pyramidFillAlpha);
                mv.stroke(0);
            } else if (totalAmplitude > 1800) {
                mv.fill(totalAmplitude / 6, 50, totalAmplitude / 6, pyramidFillAlpha);
                mv.stroke(0);
            } else {
                mv.fill(totalAmplitude / 3, 50, totalAmplitude / 3, pyramidFillAlpha);
                mv.stroke(0);
            }

        }

        this.drawPyramid(this.pyramidSize);
        mv.popMatrix();

        // Draw the additional bottom pyramid (upright) underneath the top pyramid
        if (PApplet.abs(this.pyramidXPosTop) >= mv.width / 3) {
            mv.translate(0, this.pyramidSize, 0);
            this.drawPyramid(this.pyramidSize);
        }
        mv.popMatrix();

        // Bottom pyramid and its additional top pyramid
        mv.pushMatrix();
        mv.translate(mv.width / 2 + this.pyramidXPosBottom, mv.height / 2, -200);
        mv.rotateY(this.pyramidRotation);

        // Draw the bottom pyramid (upright)
        mv.pushMatrix();
        mv.translate(0, this.pyramidSize, 0);
        this.drawPyramid(this.pyramidSize);
        mv.popMatrix();

        // Draw the additional top pyramid (inverted) above the bottom pyramid
        if (PApplet.abs(this.pyramidXPosBottom) >= mv.width / 3) {
            mv.translate(0, -this.pyramidSize, 0);
            mv.rotateX(PConstants.PI);
            this.drawPyramid(this.pyramidSize);
        }
        mv.popMatrix();
    }

    public void drawPyramid(float size) {
        // PApplet.println("in draw pyramid method");
        mv.beginShape(PConstants.TRIANGLES);
        // PApplet.println(size);
        if (this.pyramidsVisible) {
            mv.strokeWeight(2); // Outlines visible
        } else {
            mv.strokeWeight(0); // Outlines invisible
        }
        this.mv.vertex(-size / 2, -size / 2, -size / 2);
        this.mv.vertex(size / 2, -size / 2, -size / 2);
        this.mv.vertex(0, size / 2, 0);

        this.mv.vertex(size / 2, -size / 2, -size / 2);
        this.mv.vertex(size / 2, -size / 2, size / 2);
        this.mv.vertex(0, size / 2, 0);

        this.mv.vertex(size / 2, -size / 2, size / 2);
        this.mv.vertex(-size / 2, -size / 2, size / 2);
        this.mv.vertex(0, size / 2, 0);

        this.mv.vertex(-size / 2, -size / 2, size / 2);
        this.mv.vertex(-size / 2, -size / 2, -size / 2);
        this.mv.vertex(0, size / 2, 0);
        this.mv.endShape(PConstants.CLOSE);
    }

    public void setPlayIntro(boolean playIntro) {
        this.playIntro = playIntro;
    }

    public void setSpinning(boolean spinning) {
        this.spinning = spinning;
    }

    public void setDrawingShapes(boolean startDrawingShapes) {
        this.startDrawingShapes = startDrawingShapes;
        // PApplet.println(this.startDrawingShapes);
    }

    public void setPyramidsVisible(boolean pyramidsVisible) {
        this.pyramidsVisible = pyramidsVisible;
    }

    public void setExtremeColour(boolean extremeColour) {
        this.extremeColour = extremeColour;
    }

    public void setfillActivated(boolean fillActivated) {
        this.fillActivated = fillActivated;
    }
}
