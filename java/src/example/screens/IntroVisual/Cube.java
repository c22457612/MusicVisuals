package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

import example.*;

public class Cube {
    private MyVisual mv; // Reference to PApplet (Processing sketch)
    private float x, y, z; // Position of the cube
    private float side; // Length of each side of the cube
    private float cubeSpeed; // Rotation speed of the cube
    private FFT fft; // FFT object to analyze the audio
    private AudioPlayer song; // The song being played
    private float angleX, angleY, angleZ; // Rotation angles for the cube
    private boolean extremeColour;
    private boolean[] modes;
    private boolean fillActivated;

    // Constructor
    public Cube(MyVisual _mv, float side, float x, float y, float z, float cubeSpeed, FFT fft,
            AudioPlayer song, boolean extremeColour, boolean modes[], boolean fillActivated) {
        this.mv = _mv;
        this.side = side;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cubeSpeed = cubeSpeed;
        this.fft = fft;
        this.song = song;
        this.angleX = 0;
        this.angleY = 0;
        this.angleZ = 0;
        this.extremeColour = extremeColour;
        this.modes = modes;
        this.fillActivated = fillActivated;
    }

    // Draw method for the cube
    void render() {

        float halfSide = this.side / 2;
        // println("cube size:"+side+"cubespeed:"+cubeSpeed); //debugging statement

        // Perform FFT analysis on the current audio playing
        this.fft.forward(this.song.mix);

        float bassSum = 0, midSum = 0, trebleSum = 0;
        int bassCount = 0, midCount = 0, trebleCount = 0;

        // Divide the frequency spectrum into bass, mid, and treble
        for (int i = 0; i < fft.specSize(); i++) {
            float freq = fft.indexToFreq(i);
            float amplitude = fft.getBand(i);

            if (freq < 150) { // Bass: below 150 Hz
                bassSum += amplitude;
                bassCount++;
            } else if (freq >= 150 && freq < 4000) { // Mid: 150 Hz to 4 kHz
                midSum += amplitude;
                midCount++;
            } else if (freq >= 4000) { // Treble: above 4 kHz
                trebleSum += amplitude;
                trebleCount++;
            }
        }

        // Calculate average amplitudes for bass, mid, and treble
        float bassAvg = (bassCount > 0) ? bassSum / bassCount : 0;
        float midAvg = (midCount > 0) ? midSum / midCount : 0;
        float trebleAvg = (trebleCount > 0) ? trebleSum / trebleCount : 0;

        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }

        float hue = PApplet.map(totalAmplitude, 50, 370, 180, 340); // Ranges from blue to purple to pink mostly
        if (this.extremeColour) {
            hue = PApplet.map(totalAmplitude, 50, 200, 0, 360); // Ranges from all colours aggresively
            // PApplet.println(this.extremeColour);
        }

        // Rotate based on the average amplitudes
        this.angleX += PApplet.map(bassAvg, 0, 10, 0, PConstants.PI / 200); // Scale these factors as needed
        this.angleY += PApplet.map(midAvg, 0, 10, 0, PConstants.PI / 200);
        this.angleZ += PApplet.map(trebleAvg, 0, 10, 0, PConstants.PI / 200);

        float totalLoudness = 0; // Initialize total loudness

        // Sum all amplitudes to calculate total loudness
        for (int i = 0; i < fft.specSize(); i++) {
            totalLoudness += fft.getBand(i);
        }

        float normalizedLoudness = PApplet.map(totalLoudness, 0, 200, 1, 10); // Adjust range 0-200 to 1-10,
        normalizedLoudness = PApplet.constrain(normalizedLoudness, 0, 3); // Ensure stroke weight doesn't get too high
        this.mv.pushMatrix();
        this.mv.translate(this.x, this.y, this.z);
        this.mv.rotateX(this.angleX * this.cubeSpeed);
        this.mv.rotateY(this.angleY * this.cubeSpeed);
        this.mv.rotateZ(this.angleZ * this.cubeSpeed);

        if (modes[0]) {
            mv.strokeWeight(normalizedLoudness); // Set the outline weight
            if (song.isPlaying()) {
                mv.strokeWeight(normalizedLoudness); // Set the outline weight
                mv.stroke(hue, 100, 100);
            } else {
                mv.strokeWeight(2); // thin outline
                mv.stroke(255);// white outline
            }
            if (this.fillActivated) {// if cubes are very small we want to fill them or if fill activated
                mv.fill(hue, 100, 100);
                mv.strokeWeight(2); // thin outline
                mv.stroke(255);// white outline
            } else {
                mv.noFill(); // Do not fill the shapes
            }
        } else if (modes[1]) {
            mv.fill(hue, 100, 100);
        }

        mv.beginShape(PConstants.QUADS);
        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0: // Front face
                    mv.vertex(-halfSide, -halfSide, halfSide);
                    mv.vertex(halfSide, -halfSide, halfSide);
                    mv.vertex(halfSide, halfSide, halfSide);
                    mv.vertex(-halfSide, halfSide, halfSide);
                    break;
                case 1: // Back face
                    mv.vertex(halfSide, -halfSide, -halfSide);
                    mv.vertex(-halfSide, -halfSide, -halfSide);
                    mv.vertex(-halfSide, halfSide, -halfSide);
                    mv.vertex(halfSide, halfSide, -halfSide);
                    break;
                // Add other faces similarly
                case 2: // Top face
                    mv.vertex(-halfSide, -halfSide, -halfSide);
                    mv.vertex(halfSide, -halfSide, -halfSide);
                    mv.vertex(halfSide, -halfSide, halfSide);
                    mv.vertex(-halfSide, -halfSide, halfSide);
                    break;
                case 3: // Bottom face
                    mv.vertex(-halfSide, halfSide, halfSide);
                    mv.vertex(halfSide, halfSide, halfSide);
                    mv.vertex(halfSide, halfSide, -halfSide);
                    mv.vertex(-halfSide, halfSide, -halfSide);
                    break;
                case 4: // Right face
                    mv.vertex(halfSide, -halfSide, halfSide);
                    mv.vertex(halfSide, -halfSide, -halfSide);
                    mv.vertex(halfSide, halfSide, -halfSide);
                    mv.vertex(halfSide, halfSide, halfSide);
                    break;
                case 5: // Left face
                    mv.vertex(-halfSide, -halfSide, -halfSide);
                    mv.vertex(-halfSide, -halfSide, halfSide);
                    mv.vertex(-halfSide, halfSide, halfSide);
                    mv.vertex(-halfSide, halfSide, -halfSide);
                    break;
            }
        }
        mv.endShape(PConstants.CLOSE);
        this.mv.popMatrix();
    }

    public void setFillActivated(boolean fill) {
        this.fillActivated = fill;
        // PApplet.println("Cube fillActivated set to: " + this.fillActivated); //
        // Debugging output
    }

    public void setExtremeColour(boolean extremeColour) {
        this.extremeColour = extremeColour;
    }

    public void setCubeSpeedUp() {
        this.cubeSpeed += 0.1;
    }

    public void setCubeSpeedDown() {
        this.cubeSpeed -= 0.1;
    }
}
