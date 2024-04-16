package example;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.util.ArrayList;

import ddf.minim.*;
import ddf.minim.analysis.FFT;

public class IntroVisual extends PApplet {
    Minim minim;
    AudioPlayer player;
    FFT fft;
    int bands;
    float[] bandWidth;
    float[] smoothedBands;
    float waveHeight = 2; // Height of the soundwave
    
    float[] prevAmplitudes;

    boolean hasStartedPlaying = false;
    boolean isFirstSoundtrackFinished = false; // Flag to indicate the first soundtrack is finished
    AudioPlayer sound1; // Player for the interactive sound
    AudioPlayer sound2;
    AudioPlayer sound3;
    AudioPlayer sound4;
    AudioPlayer sound5;
    AudioPlayer song;
    boolean isInteractiveSoundFinished = false;
    boolean rainbowWaveVisible = false;
    ArrayList<PVector> wavePoints = new ArrayList<PVector>();

    // Pyramid variables
    float pyramidSize = 90; // half the size of the diamond
    float pyramidXPosTop = 0;
    float pyramidXPosBottom = 0;
    float pyramidRotation = 0;
    float pyramidMoveSpeed = 1;
    boolean pyramidsVisible=false;

    float pyramidCenterX = 208;
    float pyramidCenterY = height / 2;

    // Determine the range of Y values to draw the squiggly line.
    float startY = pyramidCenterY - pyramidSize;
    float endY = pyramidCenterY + pyramidSize;
    

    boolean circleVisible = false; //circle visualizer variables
    float circleOpacity = 0;
    float circleMaxRadius = 110; 
    long circleFadeStartTime = -1;

    float currentRotationY = 0; //diamond variable
    float transparentColour = 255f; // Start fully opaque

    PFont font;
    int fontSize = 48; // Adjust size as needed

    boolean playIntro=true; 



    public static void main(String[] args) {
        PApplet.main("example.playIntro");
    }

    public void settings() {
        size(800, 600, P3D);
    }

    public void setup() {
        minim = new Minim(this);
        player = minim.loadFile("data/introSound.mp3", 2048);
        sound1 = minim.loadFile("data/8bitSound1.mp3", 2048);//interactive sounds
        sound2 = minim.loadFile("data/8bitSound2.mp3", 2048);
        sound3 = minim.loadFile("data/8bitSound3.mp3", 2048);
        sound4 = minim.loadFile("data/8bitSound4.mp3", 2048);
        sound5 = minim.loadFile("data/8bitSound5.mp3", 2048);// continue to audio visualizer maybe constraint so can only be pressed once
        song = minim.loadFile("data/Radiohead.mp3", 2048);

    
        fft = new FFT(sound1.bufferSize(), sound1.sampleRate());
        bands = 256;
        bandWidth = new float[bands];
        smoothedBands = new float[bands];
        fft.logAverages(22, 5);
    
        prevAmplitudes = new float[fft.specSize()];
        font = createFont("Monospaced.bold", fontSize);
        textFont(font);
        
        // Font settings
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(0); // Set background to black
        
    
            
          
       
    }

    public void drawDiamond() {
        pushMatrix();
        translate(width / 2, height / 2, -400);
        rotateX(currentRotationY);
        rotateY(currentRotationY);
        
        stroke((frameCount % 255), 255, 255);
        strokeWeight(2);
        fill(255, 0, 0, transparentColour); // Use current transparency level
        
        float size = 180;
        float mid = size / 2;
        
        // Draw the diamond
        beginShape(TRIANGLES);
        vertex(0, -size, 0);// top pyramid 1
        vertex(-mid, 0, -mid);
        vertex(mid, 0, -mid);

        vertex(0, -size, 0); //top pyramid 2
        vertex(mid, 0, -mid);
        vertex(mid, 0, mid);

        vertex(0, -size, 0); // top pyramid 3
        vertex(mid, 0, mid);
        vertex(-mid, 0, mid);

        vertex(0, -size, 0); //top pyramid 4
        vertex(-mid, 0, mid);
        vertex(-mid, 0, -mid);

        vertex(0, size, 0); //bottom pyramid 1
        vertex(-mid, 0, -mid);
        vertex(mid, 0, -mid);

        vertex(0, size, 0); //bottom pyramid 2
        vertex(mid, 0, -mid);
        vertex(mid, 0, mid);

        vertex(0, size, 0);//bottom pyramid 3
        vertex(mid, 0, mid);
        vertex(-mid, 0, mid);

        vertex(0, size, 0);//bottom pyramid 4
        vertex(-mid, 0, mid);
        vertex(-mid, 0, -mid);

        endShape(CLOSE);
        
        popMatrix();
    }

    public void drawPyramids() {
        // Top pyramid and its additional bottom pyramid
        pushMatrix();
        translate(width / 2 + pyramidXPosTop, height / 2, -200);
        rotateY(pyramidRotation);
    
        // Draw the top pyramid (inverted)
        pushMatrix();
        translate(0, -pyramidSize, 0);
        rotateX(PI);
        drawPyramid(pyramidSize);
        popMatrix();
    
        // Draw the additional bottom pyramid (upright) underneath the top pyramid
        if (abs(pyramidXPosTop) >= width / 3) {
            translate(0, pyramidSize, 0);
            drawPyramid(pyramidSize);
        }
        popMatrix();
    
        // Bottom pyramid and its additional top pyramid
        pushMatrix();
        translate(width / 2 + pyramidXPosBottom, height / 2, -200);
        rotateY(pyramidRotation);
    
        // Draw the bottom pyramid (upright)
        pushMatrix();
        translate(0, pyramidSize, 0);
        drawPyramid(pyramidSize);
        popMatrix();
    
        // Draw the additional top pyramid (inverted) above the bottom pyramid
        if (abs(pyramidXPosBottom) >= width / 3) {
            translate(0, -pyramidSize, 0);
            rotateX(PI);
            drawPyramid(pyramidSize);
        }
        popMatrix();
    }

    public void drawPyramid(float size) {
        beginShape(TRIANGLES);
        if (pyramidsVisible) {
            strokeWeight(2); // Outlines visible
        } else {
            strokeWeight(0); // Outlines invisible
        }
        vertex(-size / 2, -size / 2, -size / 2);
        vertex(size / 2, -size / 2, -size / 2);
        vertex(0, size / 2, 0);

        vertex(size / 2, -size / 2, -size / 2);
        vertex(size / 2, -size / 2, size / 2);
        vertex(0, size / 2, 0);

        vertex(size / 2, -size / 2, size / 2);
        vertex(-size / 2, -size / 2, size / 2);
        vertex(0, size / 2, 0);

        vertex(-size / 2, -size / 2, size / 2);
        vertex(-size / 2, -size / 2, -size / 2);
        vertex(0, size / 2, 0);
        endShape(CLOSE);
    }

    public void drawSoundWave(){
        
        fft.forward(player.mix);
        noiseDetail(2, 0.2f); // Adjust noise detail for smoother or rougher transitions

        float waveFrequency = 0.5f; // Controls the frequency of the sine wave
        float maxAmplitude = 1f; //double the frequency
        float waveLength = 202;// length to touch the left pyramid top
        
        //top left pyramid
        for (float y = 0; y <=waveLength ; y+=0.1) { //top left pyramid
            int index = (int) map(y, height, 0, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight; // Scale the amplitude based on FFT average
            // Use the x value as the input to the sin function to create a horizontal wave
            float x = sin((height - y) * waveFrequency + frameCount * 0.05f) * amplitude * maxAmplitude;
            x += 208; // line up with pyramid ,width/3
        
            // Change the color over time based on the amplitude
            int colorValue = (int) map(amplitude, 0, maxAmplitude, 0, 255);
            stroke(color(255 - colorValue, colorValue, 255));
        
            // Draw the wave by connecting points vertically
            line(x, y, x, y - 0.1f); // Draw points upward for a vertical wave
        }

        //top right pyramid
        for (float y = 0; y <=waveLength ; y+=0.1) { //top left pyramid
            int index = (int) map(y, height, 0, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight; // Scale the amplitude based on FFT average
            // Use the x value as the input to the sin function to create a horizontal wave
            float x = sin((height - y) * waveFrequency + frameCount * 0.05f) * amplitude * maxAmplitude;
            x += 593; // line up with pyramid
        
            // Change the color over time based on the amplitude
            int colorValue = (int) map(amplitude, 0, maxAmplitude, 0, 255);
            stroke(color(255 - colorValue, colorValue, 255));
        
            // Draw the wave by connecting points vertically
            line(x, y, x, y - 0.1f); // Draw points upward for a vertical wave
        }

        
    }

    
    

    public void stop() {
        player.close();
        minim.stop();
        super.stop();
    }
    
    

    
    
    
    

}
