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
    float waveHeight = 3; // Height of the soundwave
    

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
    // Pyramid movement variables
    float pyramidTopTargetY;
    float pyramidBottomTargetY;
    boolean movePyramidTopUp = false;
    boolean movePyramidBottomDown = false;

    float pyramidFillAlpha = 0; // Transparency for the pyramid fill

    float pyramidCenterX = 208;
    float pyramidCenterY = height / 2;

    // Determine the range of Y values to draw the squiggly line.
    float startY = pyramidCenterY - pyramidSize;
    float endY = pyramidCenterY + pyramidSize;
    
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

    //rainbow wave variables
    float centerX = width * 0.5f;
    float centerY = height * 0.5f;
    float baseRadius = 140; // Base radius of the wave
    float maxWaveAmplitude = 5000; // Max additional amplitude, adjusted for smoother wave
    float smoothingFactor = 0.2f; // Increase for smoother transitions
    float maxFFTAmplitude = 0; // We'll calculate this each frame
    float rainbowWaveRotationAngle=0;

    boolean circleVisible = false;
    float circleOpacity = 0;
    float circleMaxRadius = 140; // Adjust this to your desired size
    long circleFadeStartTime = -1;

    PFont font;
    int fontSize = 48; // Adjust size as needed

    float fadeAmount = 0; // This controls the opacity of the black overlay.
    boolean startFading = false; // This flag will start the fading process.
    long fadeStartTime = 0; // This will store the time when fading starts.
    long fadeDuration=6000;

    boolean playIntro=false;
    boolean startDrawingShapes=false;
    AudioPlayer soundToVisualize = null;

    int colour1 = 150;
    int colour2 = 255;
    int colour3 = 170;

    
    float faceOpacity = 0;
    float[] faceOpacities = new float[6]; // Opacity for each face
    boolean fadeInActive = false;
    int faceColorIndex = 0;
    int[] colors = {color(255, 0, 0), color(0, 255, 0), color(0, 0, 255), color(255, 255, 0), color(0, 255, 255), color(255, 0, 255)};
    float bigCubeSize = 100; // Initial size of the big cube
    float smallCubeSize=50;
    float verySmallCubeSize=25;
    float offset=height/0.6f; // used for small cube offset
    float bigCubeSpeed=0.1f;
    float smallCubeSpeed=0.02f;
    boolean shrinkActive = false; // Control whether the cube should shrink
    float angle = 0; 
    //used in rotation
    float angleX;
    float angleY;
    float angleZ;

    float sphereY=height/2;         // Current y-position of the sphere
    float sphereRadius = 5000; // Radius of the sphere
    float movementSpeed = 2; // Speed of vertical movement
    boolean movingUp = true; // Direction control flag
    float sphereOffset=height/0.7f;// slightly more than pyramid offset so sphere hits top of pyramid
    float smallSphereRadius=100f;

    ArrayList<PVector> smallCubePositions = new ArrayList<PVector>();
    boolean cubeClicked=false;
    final int NUM_MODES = 3;  // Change this based on the number of modes you have
    boolean[] modes = new boolean[NUM_MODES];
    int currentModeIndex = 0;  // Index of the currently active mode

    boolean fillActivated=true;
    boolean extremeColour=false;
    boolean displayDiamond = false;  // True for diamond, false for cube
    boolean xRotateDiamond=false;
    boolean yRotateDiamond=false;

    Cube bigCube;
    Cube smallCubeAbove;
    Cube smallCubeAboveLeft;
    Cube smallCubeAboveRight;
    Cube smallCubeMiddleLeft;
    Cube smallCubeMiddleRight;
    Cube smallCubeBottom;
    Cube smallCubeBottomLeft;
    Cube smallCubeBottomRight;
    Cube smallCubeFurtherAbove;
    Cube smallCubeFurtherAboveLeft;
    Cube smallCubeFurtherAboveRight;
    Cube smallCubeFurtherBottom;
    Cube smallCubeFurtherBottomLeft;
    Cube smallCubeFurtherBottomRight;
    Cube verySmallCubeLeft;
    Cube verySmallCubeRight;

    Diamond diamond;


    public static void main(String[] args) {
        PApplet.main("example.IntroVisual");
    }

    public void settings() {
        //size(800, 600, P3D); // this caused mouse clicking issues
        
        fullScreen(P3D); // this fixed mouse clicking issues
    
        
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
        
        pyramidTopTargetY = height * 0.1f; // Move near the top of the window
        pyramidBottomTargetY = height * 0.9f; // Move near the bottom of the window

    
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

        smallCubePositions.add(new PVector(width / 1.14f, height / 2, 0)); 
        smallCubePositions.add(new PVector(width / 8.4f, height / 2, 0)); 
        for (int i = 0; i < NUM_MODES; i++) {
            modes[i] = false;
        }
        modes[currentModeIndex] = true;  // Activate the first mode initially
        

        //big cube initialization
        bigCube = new Cube(this, bigCubeSize, width/2, height/2, 0, bigCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeAbove = new Cube(this, smallCubeSize, width / 2, height / 2 - offset, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeAboveLeft = new Cube(this, smallCubeSize, width / 2.6f, height / 2 - offset, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeAboveRight = new Cube(this, smallCubeSize, width / 1.65f, height / 2 - offset, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeMiddleLeft = new Cube(this, smallCubeSize, width / 2.6f, height / 2, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeMiddleRight=new Cube(this, smallCubeSize, width / 1.65f, height / 2 , 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeBottomLeft=new Cube(this, smallCubeSize, width / 2.6f, height / 2 + offset, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeBottom =new Cube(this, smallCubeSize, width /2, height / 2+offset, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeBottomRight =new Cube(this, smallCubeSize, width / 1.65f, height / 2 + offset, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherAbove =new Cube(this, smallCubeSize, width / 2, height / 2 - offset*1.75f, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherAboveLeft =new Cube(this, smallCubeSize, width / 2.6f, height / 2 - offset*1.75f, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherAboveRight = new Cube(this, smallCubeSize, width / 1.65f, height / 2 -offset*1.75f, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherBottom =new Cube(this, smallCubeSize, width /2, height / 2+offset*1.75f, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherBottomLeft =new Cube(this, smallCubeSize, width / 2.6f, height / 2 +offset*1.75f, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherBottomRight =new Cube(this, smallCubeSize, width / 1.65f, height / 2 +offset*1.75f, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        verySmallCubeLeft=new Cube(this, verySmallCubeSize, width / 1.14f, height / 2, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        verySmallCubeRight=new Cube(this, verySmallCubeSize, width / 8.4f, height / 2, 0, smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);

        diamond = new Diamond(this, fft, currentRotationY, extremeColour, fillActivated, transparentColour, startDrawingShapes, xRotateDiamond, yRotateDiamond,playIntro);

    }

    public void draw() {
        background(0); // Set background to black

        if (playIntro){
            
            currentRotationY %= TWO_PI;
            if (!startDrawingShapes){ //logic for intro
                 
                //drawDiamond();
                diamond.drawDiamond();
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
            }else if (startDrawingShapes){ //logic for other shapes
                if (modes[0]){
                    colorMode(HSB, 360, 100, 100);  // Set HSB color mode
                    pushMatrix();  // Save the current transformation matrix state
                    drawMovingSphere(width /2, height/2, sphereRadius);

                    if (displayDiamond){
                        diamond.setDrawingShapes(startDrawingShapes);
                        diamond.setExtremeColour(extremeColour);
                        diamond.drawDiamond();
                    }
                    else {
                        bigCube.drawCube();
                    }

                    smallCubeAbove.drawCube();
                    smallCubeAboveLeft.drawCube();
                    smallCubeAboveRight.drawCube();
                    smallCubeMiddleLeft.drawCube();
                    smallCubeMiddleRight.drawCube();
                    smallCubeBottomLeft.drawCube();
                    smallCubeBottom.drawCube();
                    smallCubeBottomRight.drawCube();
                    smallCubeFurtherAbove.drawCube();
                    smallCubeFurtherAboveLeft.drawCube();
                    smallCubeFurtherAboveRight.drawCube();
                    smallCubeFurtherBottom.drawCube();
                    smallCubeFurtherBottomLeft.drawCube();
                    smallCubeFurtherBottomRight.drawCube();
                    drawPyramids();
                    
                    colorMode(RGB, 255, 255, 255);  // Switch back to RGB color mode for drawing other elements
                    popMatrix(); 
                    drawSoundWave();
                    verySmallCubeLeft.drawCube();
                    verySmallCubeRight.drawCube();
                    if (!song.isPlaying()){ //paused logic
                        if (displayDiamond){
                            diamond.drawDiamond();
                        }else{
                            bigCube.drawCube();
                            //drawCube(bigCubeSize,width/2,height/2,0,bigCubeSpeed);
                        }
                        drawSoundWave();
                        fill(173, 216, 230); //light blue
                        text("Paused",width/2,height-fontSize);
                    }
                }
            }
            
            if (sound1.isPlaying()) {
                soundToVisualize = sound1;
            } else if (sound2.isPlaying()) {
                soundToVisualize = sound2;
            } else if (sound3.isPlaying()) {
                soundToVisualize = sound3;
            } else if (sound4.isPlaying()) {
                soundToVisualize = sound4;
            } else if (sound5.isPlaying()) {
                soundToVisualize = sound5;
            }else if (song.isPlaying()) {
                soundToVisualize = song;
            }
    
    
            if (startFading) {
                circleVisible=false;
                long timeElapsed = millis() - fadeStartTime;
                fadeAmount = map(timeElapsed, 0, 3000, 0, 255); // Fade over 3 seconds
                fadeAmount = constrain(fadeAmount, 0, 255); // Ensure fadeAmount does not exceed 255
                updateFading();
                //println("fade amount"+fadeAmount); //debugging statement
                
                if (fadeAmount>=255){
                    startFading=false;
                    fadeAmount=0;// make black cover transparent again
                }
            }
        
            // Apply the fade effect by drawing a rectangle covering the entire screen
            if (fadeAmount > 0) {
                fill(0, fadeAmount);
                rect(0, 0, width, height);
            }
        }else{ // prompt user to start intro
            fill(180, 230, 230); //light blue
            text("press space",width/2,height-fontSize);
            diamond.drawDiamond();
            noFill();
            drawMovingSphere(width /2, height/2, sphereRadius);
        }
    }
    
    

    public void stop() {
        player.close();
        minim.stop();
        super.stop();
    }
    
    

    public void drawDiamond() {
        pushMatrix();
        translate(width / 2, height / 2, -400);
        

        if (startDrawingShapes){
            if (xRotateDiamond){
                if (extremeColour){
                    rotateX(currentRotationY*2);
                }else{
                    rotateX(currentRotationY);
                } 
            }
            if (yRotateDiamond){
                if (extremeColour){
                    rotateY(currentRotationY*2);
                }else{
                    rotateY(currentRotationY);
                } 
            }
        }else{
            rotateX(currentRotationY);
            rotateY(currentRotationY);
        }
        
        
        
        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }
        
        stroke((frameCount % 255), 255, 255);
        strokeWeight(2);
        if (!startDrawingShapes){
            fill(340, 100, 100, transparentColour);
        }else if(startDrawingShapes){
            if (extremeColour){
                if (fillActivated){
                    float hue = map(totalAmplitude, 0, 2000, 0, 360);  // custom hue for diamond
                    hue=hue%360;
                    fill(hue,100,100);
                }else{
                    noFill();
                }
                
            }else{
                if (fillActivated){
                    float hue = map(totalAmplitude, 0, 2000, 300, 360);  // custom hue for diamond
                    fill(hue,100,100);
                    stroke(255);//white outline
                }
                
            }
            
        }
        
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
        if (spinning){
            //println(transparentColour);
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

        if (song.isPlaying()){ //rotate
            if (rotationSpeed < 0.05) {
                rotationSpeed += 0.0001;
            }
            pyramidRotation += rotationSpeed; // Keep rotating the pyramids continuously
            if (abs(pyramidXPosTop) < width / 2) {
                pyramidXPosTop -= pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            if (abs(pyramidXPosBottom) < width / 2) {
                pyramidXPosBottom += pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            currentRotationY += rotationSpeed;
            if (transparentColour > 0) {
                transparentColour -= 1;
            }
            else{// else stay
                pyramidsVisible=true;
            }
            pyramidFillAlpha += 2; // Increase alpha gradually
            pyramidFillAlpha = constrain(pyramidFillAlpha, 0, 255); // Limit alpha to max 255
            
        }

        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }

        // Top pyramid and its additional bottom pyramid
        pushMatrix();
        translate(width / 2 + pyramidXPosTop, height / 2, -200);
        rotateY(pyramidRotation);
    
        // Draw the top pyramid (inverted)
        pushMatrix();
        translate(0, -pyramidSize, 0);
        rotateX(PI);
        if (playIntro &&!startDrawingShapes){
            fill(totalAmplitude, 50, totalAmplitude, transparentColour);
            //println(transparentColour);
            //println("in intro"); //debugging statement
        }
        if (modes[0] &&startDrawingShapes)// colour scheme for mode 0
        {
            //println("in visualizer");
            if (extremeColour){
                float hue = map(totalAmplitude, 0, 2000, 40, 180);  // Ranges from all colours aggresively
                hue = hue % 360;  // Ensure the hue wraps around correctly
                fill(hue,100,100);
                //println("extreme colours");
            }else if (!extremeColour){
                float hue = map(totalAmplitude, 0, 2000, 240, 360);  // Ranges from purple to pink aggresively
                hue = hue % 360;  // Ensure the hue wraps around correctly
                fill(hue,100,100);
                //println("not extreme colours");
            }
            
        }else if(modes[1]){
            //println(totalAmplitude); //debugging statement
            if (totalAmplitude>1500){
                fill(totalAmplitude/2, 50, totalAmplitude/4, pyramidFillAlpha);
                stroke(0);
            }else if(totalAmplitude>1800){
                fill(totalAmplitude/6, 50, totalAmplitude/6, pyramidFillAlpha);
                stroke(0);
            }else{ 
                fill(totalAmplitude/3, 50, totalAmplitude/3, pyramidFillAlpha);
                stroke(0);
            }
            
        }

        
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
    
    
    public void drawAdditionalPyramid(float size, boolean isTop) {
        translate(0, isTop ? size * 2 : -size * 2, 0); // Adjust position based on whether it's top or bottom pyramid
        drawPyramid(size); // Uses the same drawPyramid method for the new pyramid
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
        
        noiseDetail(10, 0.3f); // Adjust noise detail for smoother or rougher transitions

        float waveFrequency = 0.5f; // Controls the frequency of the sine wave
        float maxAmplitude = 1f; //double the frequency
        float waveLength = height/2.75f;// length to properly touch the pyramids

        if (!startDrawingShapes){
            fft.forward(player.mix);
        }else
        {
            fft.forward(song.mix);
            waveLength=height/2.1f;
        }

        if (modes[0] && startDrawingShapes){
            strokeWeight(15);
        }

        if (extremeColour){
            colorMode(HSB, 360, 100, 100);  // Set HSB color mode
            strokeWeight(28); // increase soundwaves naturally
        }
        
        
        //top left pyramid
        for (float y = 0; y <=waveLength ; y+=0.1) { //top left pyramid
            int index = (int) map(y, height, 0, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight; // Scale the amplitude based on FFT average
            // Use the x value as the input to the sin function to create a horizontal wave
            float x = sin((height - y) * waveFrequency + frameCount * 0.05f) * amplitude * maxAmplitude;
            x += width/4.1f; // line up with pyramid properly for full screen
        
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
            x += width/1.323f; // line up with pyramid full screen
        
            // Change the color over time based on the amplitude
            int colorValue = (int) map(amplitude, 0, maxAmplitude, 0, 255);
            stroke(color(255 - colorValue, colorValue, 255));
        
            // Draw the wave by connecting points vertically
            line(x, y, x, y - 0.1f); // Draw points upward for a vertical wave
        }

        // bottom left pyramid
        float bottomWaveAmplitudeScale = 0.1f; // Adjust this to make the bottom waves less reactive 

        for (float y = height; y >= height - waveLength; y -= 0.1) {
            int index = (int) map(height - y, 0, height, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = sin(((height - y) * waveFrequency - frameCount * 0.2f)) * amplitude * maxAmplitude; 
            x += width/4.1f;

            int colorValue = (int) map(amplitude, 0, maxAmplitude, 0, 255);
            stroke(color(255 - colorValue, colorValue, 255));

            line(x, y, x, y + 0.1f);
        }

        //bottom right pyramid
        for (float y = height; y >= height - waveLength; y -= 0.1) {
            int index = (int) map(height - y, 0, height, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = sin(((height - y) * waveFrequency - frameCount * 0.2f)) * amplitude * maxAmplitude; 
            x += width/1.323f;

            int colorValue = (int) map(amplitude, 0, maxAmplitude, 0, 255);
            stroke(color(255 - colorValue, colorValue, 255));

            line(x, y, x, y + 0.1f);
        }
    }

    // Assuming 'rotationAngle' is a global float variable initialized to 0.0

    public void drawRainbowWave() {
        colorMode(HSB, 360, 100, 100);
        // Recalculate angleStep based on the current fft specSize
        float angleStep = TWO_PI / fft.specSize();
    
        // Calculate maximum FFT amplitude for normalization
        for (int i = 0; i < fft.specSize(); i++) {
            maxFFTAmplitude = max(maxFFTAmplitude, fft.getBand(i));
        }
    
        // Ensure there's no divide by zero issue
        if (maxFFTAmplitude == 0) {
            maxFFTAmplitude = 1;
        }
    
        // Increment the rotation angle, adjust the speed as necessary
        rainbowWaveRotationAngle += 0.01;
    
        // Start matrix transformation
        pushMatrix();
        // Translate to the center of the screen
        translate(width/2, height/2);
        // Rotate the whole wave by the current rotation angle
        if (modes[0]){
            rotate(rainbowWaveRotationAngle);
        }
        noFill();
        strokeWeight(3);
        beginShape();
    
        for (int i = 0; i < fft.specSize(); i++) {
            float amplitude = fft.getBand(i);
            // Smooth the transition of amplitude values
            prevAmplitudes[i] = lerp(prevAmplitudes[i], amplitude, smoothingFactor);
            // Normalize and scale the amplitude
            float normalizedAmplitude = map(prevAmplitudes[i], 0, maxFFTAmplitude, 0, maxWaveAmplitude);
    
            // Calculate wave points
            float x = (baseRadius + normalizedAmplitude) * cos(i * angleStep);
            float y = (baseRadius + normalizedAmplitude) * sin(i * angleStep);
    
            // Dynamic coloring based on position
            int colorValue = (int) (128 + 128 * sin(i * 0.1f + frameCount * 0.02f));
            stroke(color(255 - colorValue, colorValue, 255));
    
            vertex(x, y);
        }
    
        endShape(CLOSE);
        popMatrix(); // Restore matrix state
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
    
    
    public void keyPressed() {
        if (key == ' ') {
            spinning = !spinning; // Toggle spinning state
            if(spinning){
                player.play();
                playIntro=true;
                diamond.setPlayIntro(playIntro);
                hasStartedPlaying = true;
                isFirstSoundtrackFinished = false; // Reset this flag when the first soundtrack starts
                circleOpacity = 0; // Reset opacity to allow fade-in effect
                // Set the start time for the circle to fade in (e.g., after 10 seconds)
                circleFadeStartTime = millis() + 10000; // 10,000 milliseconds from now
            } else {
                player.pause();
            }
        } else if (key == '1') {
            if (!startDrawingShapes){
                playSound(sound1);
            }
        } else if (key == '2') {
            if (!startDrawingShapes){
                playSound(sound2);
            }
        }else if(key=='3'){
            if (!startDrawingShapes){
                playSound(sound3);
            }
        }else if(key=='4'){
            if (!startDrawingShapes){
                playSound(sound4);
            }
        }else if(key==ENTER){
            playIntro=true;
            isFirstSoundtrackFinished=true;
            drawRainbowWave();
            playSound(sound5);
            startFading = true;
            fadeStartTime = millis(); //will eventually trigger startDrawingShapes boolean
            
        }else if(key=='5'){
            playSound(song);
            startFading=false;
            startDrawingShapes=true; //manually start
        }

        if (startDrawingShapes){ // when we are using audio visualization for shapes, we can pause and resume
            if (key == 'p' || key == 'P') {
                if (song.isPlaying()) {
                    song.pause();
                    movePyramidTopUp = false;
                    movePyramidBottomDown = false;
                } else {
                    song.play();
                }
            }

            if (keyCode == UP) {// controlling cube speeds
                if (bigCubeSpeed>-1){
                    bigCube.setCubeSpeedUp();
                    xRotateDiamond=true;
                    diamond.setxRotateDiamond(xRotateDiamond);
                }
            } else if (keyCode == DOWN) {
                if (bigCubeSpeed>0){
                    bigCube.setCubeSpeedDown();
                    xRotateDiamond=false;
                    diamond.setxRotateDiamond(xRotateDiamond);
                }
            } else if (keyCode == LEFT) {
                if (smallCubeSpeed>0){
                    smallCubeAbove.setCubeSpeedDown();
                    smallCubeAboveLeft.setCubeSpeedDown();
                    smallCubeAboveRight.setCubeSpeedDown();
                    smallCubeMiddleLeft.setCubeSpeedDown();
                    smallCubeMiddleRight.setCubeSpeedDown();
                    smallCubeBottom.setCubeSpeedDown();
                    smallCubeBottomLeft.setCubeSpeedDown();
                    smallCubeBottomRight.setCubeSpeedDown();
                    smallCubeFurtherAbove.setCubeSpeedDown();
                    smallCubeFurtherAboveLeft.setCubeSpeedDown();
                    smallCubeFurtherAboveRight.setCubeSpeedDown();
                    smallCubeFurtherBottom.setCubeSpeedDown();
                    smallCubeFurtherBottomLeft.setCubeSpeedDown();
                    smallCubeFurtherBottomRight.setCubeSpeedDown();
                    verySmallCubeLeft.setCubeSpeedDown();
                    verySmallCubeRight.setCubeSpeedDown();
                    yRotateDiamond=false;
                    diamond.setyRotateDiamond(yRotateDiamond);
                }
            } else if (keyCode == RIGHT) {
                if (smallCubeSpeed>-1){
                    smallCubeAbove.setCubeSpeedUp();
                    smallCubeAboveLeft.setCubeSpeedUp();
                    smallCubeAboveRight.setCubeSpeedUp();
                    smallCubeMiddleLeft.setCubeSpeedUp();
                    smallCubeMiddleRight.setCubeSpeedUp();
                    smallCubeBottom.setCubeSpeedUp();
                    smallCubeBottomLeft.setCubeSpeedUp();
                    smallCubeBottomRight.setCubeSpeedUp();
                    smallCubeFurtherAbove.setCubeSpeedUp();
                    smallCubeFurtherAboveLeft.setCubeSpeedUp();
                    smallCubeFurtherAboveRight.setCubeSpeedUp();
                    smallCubeFurtherBottom.setCubeSpeedUp();
                    smallCubeFurtherBottomLeft.setCubeSpeedUp();
                    smallCubeFurtherBottomRight.setCubeSpeedUp();
                    verySmallCubeLeft.setCubeSpeedUp();
                    verySmallCubeRight.setCubeSpeedUp();
                    yRotateDiamond=true;
                    diamond.setyRotateDiamond(yRotateDiamond);
                }
            }

            if (keyCode=='f'|| keyCode=='F'){
                fillActivated=!fillActivated;
                bigCube.setFillActivated(fillActivated);
                smallCubeAbove.setFillActivated(fillActivated);
                smallCubeAboveLeft.setFillActivated(fillActivated);
                smallCubeAboveRight.setFillActivated(fillActivated);
                smallCubeMiddleLeft.setFillActivated(fillActivated);
                smallCubeMiddleRight.setFillActivated(fillActivated);
                smallCubeBottom.setFillActivated(fillActivated);
                smallCubeBottomLeft.setFillActivated(fillActivated);
                smallCubeBottomRight.setFillActivated(fillActivated);
                smallCubeFurtherAbove.setFillActivated(fillActivated);
                smallCubeFurtherAboveLeft.setFillActivated(fillActivated);
                smallCubeFurtherAboveRight.setFillActivated(fillActivated);
                smallCubeFurtherBottom.setFillActivated(fillActivated);
                smallCubeFurtherBottomLeft.setFillActivated(fillActivated);
                smallCubeFurtherBottomRight.setFillActivated(fillActivated);
                verySmallCubeLeft.setFillActivated(fillActivated);
                verySmallCubeRight.setFillActivated(fillActivated);
            }
            if (keyCode=='e'|| keyCode=='E'){
                extremeColour = !extremeColour;
                println("extremeColour toggled to: " + extremeColour);
                bigCube.setExtremeColour(extremeColour);
                smallCubeAbove.setExtremeColour(extremeColour);
                smallCubeAboveLeft.setExtremeColour(extremeColour);
                smallCubeAboveRight.setExtremeColour(extremeColour);
                smallCubeMiddleLeft.setExtremeColour(extremeColour);
                smallCubeMiddleRight.setExtremeColour(extremeColour);
                smallCubeBottom.setExtremeColour(extremeColour);
                smallCubeBottomLeft.setExtremeColour(extremeColour);
                smallCubeBottomRight.setExtremeColour(extremeColour);
                smallCubeFurtherAbove.setExtremeColour(extremeColour);
                smallCubeFurtherAboveLeft.setExtremeColour(extremeColour);
                smallCubeFurtherAboveRight.setExtremeColour(extremeColour);
                smallCubeFurtherBottom.setExtremeColour(extremeColour);
                smallCubeFurtherBottomLeft.setExtremeColour(extremeColour);
                smallCubeFurtherBottomRight.setExtremeColour(extremeColour);
                verySmallCubeLeft.setExtremeColour(extremeColour);
                verySmallCubeRight.setExtremeColour(extremeColour);
            }
        }


        // You can add more conditions for additional sounds here
    }

    public void mouseClicked() {
        for (int i = 0; i < smallCubePositions.size(); i++) {
            PVector cubePos = smallCubePositions.get(i);
            float screenX = screenX(cubePos.x, cubePos.y, cubePos.z);
            float screenY = screenY(cubePos.x, cubePos.y, cubePos.z);
            float distance = dist(mouseX, mouseY, screenX, screenY);
            
            if (distance < 20) {
                println("Clicked on cube at " + cubePos);
                if (i == 0) {  // Assuming index 0 is the left cube
                    displayDiamond = false;
                } else if (i == 1) {  // Assuming index 1 is the right cube
                    displayDiamond = true;
                }
                break;
            }
        }
    }
    
    
    public void handleCubeClick(PVector cubePosition) {
        // Handle the event when a cube is clicked
        println("Cube clicked at: " + cubePosition);
        cycleModes();
        cubeClicked=true;
    }

    void cycleModes() {
        modes[currentModeIndex] = false;  // Deactivate the current mode
        //currentModeIndex = (currentModeIndex + 1) % NUM_MODES;  // Move to the next mode, wrapping around if necessary
        modes[currentModeIndex] = true;  // Activate the new mode
    }
    

    public void updateFading() {
        if (startFading && millis() - fadeStartTime > fadeDuration) {
            startFading = false; // Stop fading after 6 seconds
            startDrawingShapes=true;
            playSound(song);
        }

        if (startFading) {
            // Perform fading logic here
            fadeAmount = map(millis() - fadeStartTime, 0, fadeDuration, 0, 255);
            fadeAmount = constrain(fadeAmount, 0, 255);
        }
    }

    private void playSound(AudioPlayer sound) {
        if (sound == null) {
            println("Error: Attempted to play a null sound.");
            return;
        }
    
        // Stop all sounds and prepare to play the selected one.
        if (sound1.isPlaying()) {
            sound1.pause();
            sound1.rewind();
        }
        if (sound2.isPlaying()) {
            sound2.pause();
            sound2.rewind();
        }
        if (sound3.isPlaying()) {
            sound3.pause();
            sound3.rewind();
        }
        if (sound4.isPlaying()) {
            sound4.pause();
            sound4.rewind();
        }
        if (sound5.isPlaying()) {
            sound5.pause();
            sound5.rewind();
        }
        if (song.isPlaying()) {
            song.pause();
            song.rewind();
        }
        
    
        // Play the selected sound.
        sound.play();
        isInteractiveSoundFinished = false; // Set to false when any interactive sound starts
    
        // Reinitialize FFT with the current sound's buffer size and sample rate
        fft = new FFT(sound.bufferSize(), sound.sampleRate());
        fft.logAverages(10, 1);
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


    void drawMovingSphere(float x, float y, float r) {
        pushMatrix(); // Save the current state of transformations
        translate(x, y); // Use the dynamic `sphereY` for y-position
        
        if (!startDrawingShapes){
            angle += 0.01; // Continuously rotate the sphere
        }else if(modes[0]&&startDrawingShapes){
            angle+=0.001;
        }

        if (song.isPlaying() || !playIntro) {
            
            rotateX(angle);
    
            // Analyze the spectrum into bass, mid, and treble
            float bassAmplitude = 0, trebleAmplitude = 0;
            int bassCount = 0, trebleCount = 0;
            
            for (int i = 0; i < fft.specSize(); i++) {
                float freq = fft.indexToFreq(i);
                float amplitude = fft.getBand(i);
    
                // Define bass as frequencies below 150 Hz
                if (freq < 150) {
                    bassAmplitude += amplitude;
                    bassCount++;
                }
                // Define treble as frequencies above 4000 Hz
                else if (freq > 4000) {
                    trebleAmplitude += amplitude;
                    trebleCount++;
                }
            }
    
            // Calculate average amplitudes
            bassAmplitude = (bassCount > 0) ? bassAmplitude / bassCount : 0;
            trebleAmplitude = (trebleCount > 0) ? trebleAmplitude / trebleCount : 0;
    
            // Adjust movement speed based on bass amplitude
            movementSpeed = map(bassAmplitude, 0, 10, 1, 5);
            movementSpeed = constrain(movementSpeed, 1, 5);
    
            // Setting HSB color mode
            colorMode(HSB, 360, 100, 100);
    
            // Optional: Adjust color based on treble amplitude
            float hue = map(trebleAmplitude, 0, 2000, 0, 360); // Half range of hue
            float saturation = map(bassAmplitude, 0, 10, 20, 100); // Saturation increases with bass
            float brightness = 100; // Always full brightness for visibility
    

            float totalAmplitude = 0;

            for (int i = 0; i < fft.specSize(); i++) { // needs to be added for extreme colours to work
                totalAmplitude += fft.getBand(i);
            }

            // Adjust stroke width dynamically for a pulsing effect
            float strokeWeightValue = map(trebleAmplitude, 0, 10, 0.5f, 15);
            if (!fillActivated){
                fill(255);
            }else{
                noFill(); 
            }
            
            strokeWeight(strokeWeightValue);
            if (modes[0]&& startDrawingShapes){
                if (extremeColour){
                    hue = map(totalAmplitude, 0, 2000, 0, 360);  // Ranges from half colour wheel
                    hue = hue % 360;  // Ensure the hue wraps around correctly
                    stroke(hue,100,100);
                    r=r/5; //widen view for more aggresive effect
                }else if (!extremeColour){
                    hue = map(totalAmplitude, 0, 2000, 0, 80);  // Ranges from other half colour wheel
                    hue = hue % 360;  // Ensure the hue wraps around correctly
                    stroke(hue,100,100);
                    //println("not extreme colours");
                }
            }else{
                hue = map(totalAmplitude, 0, 2000, 300, 360);  // Ranges from half colour wheel
                hue = hue % 360;  // Ensure the hue wraps around correctly
                stroke(hue,100,100);
            }

                
    
            // Draw the sphere with the constant radius
            sphere(r);
        } else {
            // Default color when music is paused
            colorMode(RGB, 255); // Switch back to RGB for consistent color handling
            //fill(255, 0, 100, 100);
            noFill();
            stroke(255);
            strokeWeight(1);
            sphere(r);
        }
    
        popMatrix(); // Restore original state of transformations
    }

    public void getTransparentColour(float transparentColour){
        diamond.transparentColour=transparentColour;
    }
    
    
    
    
    
    
    
    

    


}
