package example;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class Cube {
    PApplet parent; // The PApplet object we will render to
    float side;
    float x, y, z;
    float cubeSpeed;
    FFT fft;
    AudioPlayer song;
    float angleX,angleY,angleZ;
    // Additional properties

    public Cube(PApplet parent, float side, float x, float y, float z, float cubeSpeed, FFT fft, AudioPlayer song) {
        this.parent = parent;
        this.side = side;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cubeSpeed = cubeSpeed;
        this.fft = fft;
        this.song = song;
        this.angleX=0;
        this.angleY=0;
        this.angleZ=0;
        // Initialize other properties
    }

    public void draw(boolean extremeColour, boolean fillActivated, boolean[] modes,float angleX) {

        float halfSide = side / 2;
        //println("cube size:"+side+"cubespeed:"+cubeSpeed); //debugging statement

        // Perform FFT analysis on the current audio playing
        fft.forward(song.mix);

        float bassSum = 0, midSum = 0, trebleSum = 0;
        int bassCount = 0, midCount = 0, trebleCount = 0;

        // Divide the frequency spectrum into bass, mid, and treble
        for (int i = 0; i < fft.specSize(); i++) {
            float freq = fft.indexToFreq(i);
            float amplitude = fft.getBand(i);

            if (freq < 150) {  // Bass: below 150 Hz
                bassSum += amplitude;
                bassCount++;
            } else if (freq >= 150 && freq < 4000) {  // Mid: 150 Hz to 4 kHz
                midSum += amplitude;
                midCount++;
            } else if (freq >= 4000) {  // Treble: above 4 kHz
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

        float hue = PApplet.map(totalAmplitude, 0, 3000, 180, 360);  // Ranges from blue to purple to pink mostly
        if (extremeColour){
            hue = PApplet.map(totalAmplitude, 0, 200, 0, 360);  // Ranges from all colours aggresively
            hue = hue % 360;  // Ensure the hue wraps around correctly
        }
        

        // Rotate based on the average amplitudes
        angleX += PApplet.map(bassAvg, 0, 10, 0, PConstants.PI / 200);  // Scale these factors as needed
        angleY += PApplet.map(midAvg, 0, 10, 0, PConstants.PI / 200);
        angleZ += PApplet.map(trebleAvg, 0, 10, 0, PConstants.PI / 200);


        float totalLoudness = 0; // Initialize total loudness

        // Sum all amplitudes to calculate total loudness
        for (int i = 0; i < fft.specSize(); i++) {
            totalLoudness += fft.getBand(i);
        }

        float normalizedLoudness = PApplet.map(totalLoudness, 0, 200, 1, 10); // Adjust range 0-200 to 1-10, 
        normalizedLoudness = PApplet.constrain(normalizedLoudness, 0, 3); // Ensure stroke weight doesn't get too high
        parent.pushMatrix();
        parent.translate(x,y,z);
        parent.rotateX(angleX*cubeSpeed);
        parent.rotateY(angleY*cubeSpeed);
        parent.rotateZ(angleZ*cubeSpeed);

        if (modes[0]){
            parent.strokeWeight(normalizedLoudness); // Set the outline weight
            if (song.isPlaying()){
                parent.strokeWeight(normalizedLoudness); // Set the outline weight
                parent.stroke(hue,100,100); 
            }
            else
            {
                parent.strokeWeight(2); // thin outline
                parent.stroke(255);// white outline
            }
            if (side<=26f || fillActivated){// if cubes are very small we want to fill them or if fill activated
                parent.fill(hue,100,100);  
                parent.strokeWeight(2); // thin outline
                parent.stroke(255);// white outline
            }else{
                parent.noFill(); // Do not fill the shapes
            }
        }else if(modes[1]){
            parent.fill(hue,100,100);
        }
        
            
        
        parent.beginShape(PConstants.QUADS);
        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0: // Front face
                    parent.vertex(-halfSide, -halfSide, halfSide);
                    parent.vertex(halfSide, -halfSide, halfSide);
                    parent.vertex(halfSide, halfSide, halfSide);
                    parent.vertex(-halfSide, halfSide, halfSide);
                    break;
                case 1: // Back face
                    parent.vertex(halfSide, -halfSide, -halfSide);
                    parent.vertex(-halfSide, -halfSide, -halfSide);
                    parent.vertex(-halfSide, halfSide, -halfSide);
                    parent.vertex(halfSide, halfSide, -halfSide);
                    break;
                // Add other faces similarly
                case 2: // Top face
                    parent.vertex(-halfSide, -halfSide, -halfSide);
                    parent.vertex(halfSide, -halfSide, -halfSide);
                    parent.vertex(halfSide, -halfSide, halfSide);
                    parent.vertex(-halfSide, -halfSide, halfSide);
                    break;
                case 3: // Bottom face
                    parent.vertex(-halfSide, halfSide, halfSide);
                    parent.vertex(halfSide, halfSide, halfSide);
                    parent.vertex(halfSide, halfSide, -halfSide);
                    parent.vertex(-halfSide, halfSide, -halfSide);
                    break;
                case 4: // Right face
                    parent.vertex(halfSide, -halfSide, halfSide);
                    parent.vertex(halfSide, -halfSide, -halfSide);
                    parent.vertex(halfSide, halfSide, -halfSide);
                    parent.vertex(halfSide, halfSide, halfSide);
                    break;
                case 5: // Left face
                    parent.vertex(-halfSide, -halfSide, -halfSide);
                    parent.vertex(-halfSide, -halfSide, halfSide);
                    parent.vertex(-halfSide, halfSide, halfSide);
                    parent.vertex(-halfSide, halfSide, -halfSide);
                    break;
            }
        }
        parent.endShape(PConstants.CLOSE);
        parent.popMatrix();
    }
}
