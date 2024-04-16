package example;

import ddf.minim.Minim;

import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PVector;

public class Cube extends PApplet {

    Minim minim;
    AudioPlayer song;
    FFT fft;

    int colour1 = 150;
    int colour2 = 255;
    int colour3 = 170;

    
    float faceOpacity = 0;
    float[] faceOpacities = new float[6]; // Opacity for each face
    boolean fadeInActive = false;
    int faceColorIndex = 0;
    int[] colors = {color(255, 0, 0), color(0, 255, 0), color(0, 0, 255), color(255, 255, 0), color(0, 255, 255), color(255, 0, 255)};
    float cubeSize = 150; // Initial size of the cube
    boolean shrinkActive = false; // Control whether the cube should shrink


    ArrayList<FloatingCube> floatingCubes = new ArrayList<FloatingCube>();


    public void settings() {
        size(800, 600, P3D);
    }

    public void setup() {
        minim = new Minim(this);
        song = minim.loadFile("Radiohead.mp3", 512);
        song.play();
        fft = new FFT(song.bufferSize(), song.sampleRate());
        textSize(16);
    }
    
    public void draw() {
        background(0);
        lights();
        
        fft.forward(song.mix);
        translate(width / 2, height / 2, 0);
        rotateX(frameCount * 0.01f);
        rotateY(frameCount * 0.01f);
        
        if (fadeInActive && faceOpacity < 255) {
            faceOpacity += 2;
        }
    
        if (shrinkActive && cubeSize > 10) { // Ensure the cube does not disappear entirely
            cubeSize -= 0.5; // Gradually decrease the cube size
        }
    
        drawCube(cubeSize); // Pass the dynamic size to the drawing function

        for (FloatingCube fc : floatingCubes) {
            fc.update();
            fc.display();
        }
    }
    
    
    public void keyPressed() {
        if (key == '1') {
            fadeInActive = !fadeInActive;
            faceColorIndex = (faceColorIndex + 1) % colors.length;
            shrinkActive = !shrinkActive; // Toggle the shrinking effect
        }
        if (key == '2') { 
            // Add a new cube at the center with a random size and color
            addCube(new PVector(width/2, height/2, 0), random(30, 100), color(random(255), random(255), random(255)));
        }
    }
    

    void drawCube(float side) {
        float halfSide = side / 2;
        
        // Amplify the FFT values for more noticeable color changes
        float bass = fft.getBand(0) * 20;  // Using linear scale for simplicity
        float mid = fft.getBand(fft.specSize() / 2) * 20;
        float treble = fft.getBand(fft.specSize() - 1) * 20;
        
        // Calculate total loudness for stroke weight
        float totalLoudness = bass + mid + treble;
        strokeWeight(map(totalLoudness, 0, 1000, 1, 10)); // Adjust these ranges based on actual loudness levels
    
        // Dynamic color based on FFT data, modulated by current selected color index for vibrancy
        int r = (int) map(bass, 0, 100, red(colors[faceColorIndex]), 255);
        int g = (int) map(mid, 0, 100, green(colors[faceColorIndex]), 255);
        int b = (int) map(treble, 0, 100, blue(colors[faceColorIndex]), 255);
        stroke(r, g, b); // Set the outline color based on the audio
    
        if (fadeInActive) {
            for (int i = 0; i < faceOpacities.length; i++) {
                if (faceOpacities[i] < 255) {
                    faceOpacities[i] += 2; // Increase opacity
                } else {
                    faceOpacities[i] = 0; // Reset when max is reached
                }
            }
        }
        
        beginShape(QUADS);
        
        // Draw each face with separate color and opacity
        for (int i = 0; i < 6; i++) {
            fill(colors[i], faceOpacities[i]);
            switch (i) {
                case 0: // Front face
                    vertex(-halfSide, -halfSide, halfSide);
                    vertex(halfSide, -halfSide, halfSide);
                    vertex(halfSide, halfSide, halfSide);
                    vertex(-halfSide, halfSide, halfSide);
                    break;
                case 1: // Back face
                    vertex(halfSide, -halfSide, -halfSide);
                    vertex(-halfSide, -halfSide, -halfSide);
                    vertex(-halfSide, halfSide, -halfSide);
                    vertex(halfSide, halfSide, -halfSide);
                    break;
                case 2: // Top face
                    vertex(-halfSide, -halfSide, -halfSide);
                    vertex(halfSide, -halfSide, -halfSide);
                    vertex(halfSide, -halfSide, halfSide);
                    vertex(-halfSide, -halfSide, halfSide);
                    break;
                case 3: // Bottom face
                    vertex(-halfSide, halfSide, halfSide);
                    vertex(halfSide, halfSide, halfSide);
                    vertex(halfSide, halfSide, -halfSide);
                    vertex(-halfSide, halfSide, -halfSide);
                    break;
                case 4: // Right face
                    vertex(halfSide, -halfSide, halfSide);
                    vertex(halfSide, -halfSide, -halfSide);
                    vertex(halfSide, halfSide, -halfSide);
                    vertex(halfSide, halfSide, halfSide);
                    break;
                case 5: // Left face
                    vertex(-halfSide, -halfSide, -halfSide);
                    vertex(-halfSide, -halfSide, halfSide);
                    vertex(-halfSide, halfSide, halfSide);
                    vertex(-halfSide, halfSide, -halfSide);
                    break;
            }
        }
        
        endShape(CLOSE); // Finish defining the shape and close it
    }
    
    
    public void addCube(PVector position, float size, int color) {
        floatingCubes.add(new FloatingCube(position, size, color));
    }
    

    public static void main(String[] args) {
        PApplet.main("example.Cube");
    }


    class FloatingCube {
        PVector position;
        float size;
        int color;
    
        FloatingCube(PVector position, float size, int color) {
            this.position = position;
            this.size = size;
            this.color = color;
        }
    
        void display() {
            pushMatrix(); // Save the current state of the matrix
            translate(position.x, position.y, position.z);
            noFill(); // Or use fill(color) if you want a solid cube
            stroke(color);
            box(size); // Draw a cube
            popMatrix(); // Restore the matrix state
        }
    
        void update() {
            position.x += random(-1, 1);
            position.y += random(-1, 1);
            position.z += random(-1, 1);

            position.x = constrain(position.x, 0, width);
            position.y = constrain(position.y, 0, height);
            position.z = constrain(position.z, -500, 500); 
        }
    }
    
    
}
