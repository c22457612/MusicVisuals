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

    //diamond variables
    boolean spinning = false; // Start without spinning
    boolean stopping = false;
    float currentRotationY = 0;
    float targetRotationY = PI / 37; // Default upright position
    float rotationIncrement = 0.01f;
    float rotationSpeed = 0.01f;
    float stoppingSpeed = 0.005f;
    float transparentColour = 255f; // Start fully opaque

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

    PFont font;
    int fontSize = 48; // Adjust size as needed

    boolean playIntro=true; 
    boolean startDrawingShapes=false;
    AudioPlayer soundToVisualize = null;

    boolean startFading = false; // This flag will start the fading process.


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
        
        if (spinning) {
            if (rotationSpeed < 0.05) {
                rotationSpeed += 0.0001;
            }
            pyramidRotation += rotationSpeed; // Keep rotating the pyramids continuously
            if (abs(pyramidXPosTop) < width / 3) {
                pyramidXPosTop -= pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            if (abs(pyramidXPosBottom) < width / 3) {
                pyramidXPosBottom += pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            currentRotationY += rotationSpeed;
            if (transparentColour > 0) {
                transparentColour -= 0.5;
            }
        }
        
        currentRotationY %= TWO_PI;
        if (!startDrawingShapes){ //logic for intro
            drawDiamond();
            drawPyramids();
            drawSoundWave();

            if (soundToVisualize != null) {
                fft.forward(soundToVisualize.mix);
                drawRainbowWave();
                circleVisible = false; // Hide the circle when the rainbow wave is drawn
            } else {
                // No sound is playing from the interactive sounds, check for first soundtrack finish
                if (isFirstSoundtrackFinished && !isInteractiveSoundFinished) {
                    isInteractiveSoundFinished = true;
                    circleVisible = true; // Show circle since interactive sounds have finished
                }
            }

            // Ensure circle visibility logic
            if (!player.isPlaying() && hasStartedPlaying && !isFirstSoundtrackFinished) {
                isFirstSoundtrackFinished = true; // Mark that the first soundtrack has finished
                circleVisible = true; // Show circle since the soundtrack finished
            }
            if (circleFadeStartTime > 0 && millis() > circleFadeStartTime &&!startFading) {// initial fade in
                drawFadingCircleWithTiming();
            }

            // Draw the fading circle if visible
            if (circleVisible && !startFading) {
                drawFadingCircleWithTiming();
            }

            if (spinning && player.position() > 9000) { // Check if 8 seconds have passed
                // Draw the neon text at the bottom center of the screen
                drawNeonTextWithFade("Press Enter:", width / 2, height - fontSize, color(0, 255, 255), player.position() - 9000);
            }
        }
    
            
          
       
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

        // bottom left pyramid
        float bottomWaveAmplitudeScale = 0.02f; // Adjust this to make the bottom waves less reactive 

        for (float y = height; y >= height - waveLength; y -= 0.1) {
            int index = (int) map(height - y, 0, height, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = sin(((height - y) * waveFrequency - frameCount * 0.2f)) * amplitude * maxAmplitude; 
            x += 208;

            int colorValue = (int) map(amplitude, 0, maxAmplitude, 0, 255);
            stroke(color(255 - colorValue, colorValue, 255));

            line(x, y, x, y + 0.1f);
        }

        for (float y = height; y >= height - waveLength; y -= 0.1) {
            int index = (int) map(height - y, 0, height, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = sin(((height - y) * waveFrequency - frameCount * 0.2f)) * amplitude * maxAmplitude; 
            x += 593;

            int colorValue = (int) map(amplitude, 0, maxAmplitude, 0, 255);
            stroke(color(255 - colorValue, colorValue, 255));

            line(x, y, x, y + 0.1f);
        }
    }

    public void drawRainbowWave() {
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        float baseRadius = 110; // Base radius of the wave
        float maxWaveAmplitude = 150; // Max additional amplitude, adjusted for smoother wave
        float angleStep = TWO_PI / fft.specSize();
    
        float smoothingFactor = 0.2f; // Increase for smoother transitions
        float maxFFTAmplitude = 0; // We'll calculate this each frame
    
        // Find the maximum FFT amplitude for normalization
        for (int i = 0; i < fft.specSize(); i++) {
            maxFFTAmplitude = max(maxFFTAmplitude, fft.getBand(i));
        }
    
        if (maxFFTAmplitude == 0) maxFFTAmplitude = 1; // Prevent division by zero
    
        beginShape();
        noFill();
        strokeWeight(2); // Stroke thickness
    
        for (int i = 0; i < fft.specSize(); i++) {
            float amplitude = fft.getBand(i);
    
            // Smooth the amplitude over time
            prevAmplitudes[i] = lerp(prevAmplitudes[i], amplitude, smoothingFactor);
    
            // Normalize the smoothed amplitude
            float normalizedAmplitude = map(prevAmplitudes[i], 0, maxFFTAmplitude, 0, maxWaveAmplitude);
    
            // Calculate the coordinates
            float x = centerX + (baseRadius + normalizedAmplitude) * cos(i * angleStep);
            float y = centerY + (baseRadius + normalizedAmplitude) * sin(i * angleStep);
    
            // Dynamic color based on frameCount, similar to the circle's stroke color
            int colorValue = (int) (128 + 128 * sin(i * 0.1f + frameCount * 0.02f));
            stroke(color(255 - colorValue, colorValue, 255));
    
            vertex(x, y); // Place the vertex for the wave
        }
    
        endShape(CLOSE);
    }

    public void drawFadingCircle() {
        if (circleOpacity < 255) {
            circleOpacity += 5; // Control the speed of the fade-in effect
        }
        int colorValue = (int) (128 + 128 * sin(frameCount * 0.05f));
        noFill();
        stroke(color(255 - colorValue, colorValue, 255), circleOpacity);
        strokeWeight(2);
        ellipse(width / 2, height / 2, circleMaxRadius * 2, circleMaxRadius * 2);
    }

    public void drawFadingCircleWithTiming() {
        if (circleOpacity < 255) {
            circleOpacity += 5; // Control the speed of the fade-in effect
        }
    
        // Draw the circle with the current opacity
        int colorValue = (int) (128 + 128 * sin(frameCount * 0.05f));
        noFill(); // Do not fill the circle
        stroke(color(255 - colorValue, colorValue, 255), circleOpacity); // Set the stroke color and opacity
        strokeWeight(2); // Set the stroke width
        ellipse(width / 2, height / 2, circleMaxRadius * 2, circleMaxRadius * 2); // Draw the circle centered
    }

    void drawNeonTextWithFade(String text, float x, float y, int glowColor, float time) {
        // Calculate fade in based on time
        float fade = map(time, 0, 2000, 0, 255); // fade in over 2 seconds
        fade = constrain(fade, 0, 255); // Make sure fade doesn't go beyond 255
    
        // Set the text size for the solid text on top
        textSize(fontSize);
    
        // Draw the glowing text with fewer layers for a simpler look
        fill(glowColor, fade); // Use fade for alpha
        for (int i = 3; i > 0; i--) { // Only 3 layers of glow for simplicity
            text(text, x, y + i); // Slight offset for the glow layers
        }
    
        // Draw the solid text on top
        fill(255, fade); // Use fade for alpha
        text(text, x, y); // Draw the text at the original position
    }
    

    public void stop() {
        player.close();
        minim.stop();
        super.stop();
    }
    
    

    public void keyPressed() {
        if (key == ' ') {
            spinning = !spinning; // Toggle spinning state
            if(spinning){
                player.play();
                hasStartedPlaying = true;
                isFirstSoundtrackFinished = false; // Reset this flag when the first soundtrack starts
                circleOpacity = 0; // Reset opacity to allow fade-in effect
                // Set the start time for the circle to fade in (e.g., after 10 seconds)
                circleFadeStartTime = millis() + 10000; // 10,000 milliseconds from now
            } else {
                player.pause();
            }
        }
    }
    
    public static void main(String[] args) {
        PApplet.main("example.IntroVisual");
    }
    

}
