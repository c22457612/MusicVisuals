//THIS IS A TESTER FILE NOT MAIN SCREEN , MAIN SCREEN IS IntroVisual.java

package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.util.ArrayList;

import ddf.minim.*;
import ddf.minim.analysis.FFT;
import example.MyVisual;

public class playIntro {
    MyVisual mv;

    AudioPlayer player;
    FFT fft;
    int bands;
    float[] bandWidth;
    float[] smoothedBands;
    float waveHeight = 3; // papplet.papplet. Height of the soundwave

    // diamond variables
    boolean spinning = false; // Start without spinning
    boolean stopping = false;
    float currentRotationY = 0;
    float targetRotationY; // Default upright position
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
    boolean pyramidsVisible = false;
    // Pyramid movement variables
    float pyramidTopTargetY;
    float pyramidBottomTargetY;
    boolean movePyramidTopUp = false;
    boolean movePyramidBottomDown = false;

    float pyramidFillAlpha = 0; // Transparency for the pyramid fill

    float pyramidCenterX = 208;
    float pyramidCenterY;

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

    // rainbow wave variables
    float centerX;
    float centerY;
    float baseRadius = 140; // Base radius of the wave
    float maxWaveAmplitude = 5000; // Max additional amplitude, adjusted for smoother wave
    float smoothingFactor = 0.2f; // Increase for smoother transitions
    float maxFFTAmplitude = 0; // We'll calculate this each frame
    float rainbowWaveRotationAngle = 0;

    boolean circleVisible = false;
    float circleOpacity = 0;
    float circleMaxRadius = 140; // Adjust this to your desired size
    long circleFadeStartTime = -1;

    PFont font;
    int fontSize = 48; // Adjust size as needed

    float fadeAmount = 0; // This controls the opacity of the black overlay.
    boolean startFading = false; // This flag will start the fading process.
    long fadeStartTime = 0; // This will store the time when fading starts.
    long fadeDuration = 6000;

    boolean playIntro = false;
    boolean startDrawingShapes = false;
    AudioPlayer soundToVisualize = null;

    int colour1 = 150;
    int colour2 = 255;
    int colour3 = 170;

    float faceOpacity = 0;
    float[] faceOpacities = new float[6]; // Opacity for each face
    boolean fadeInActive = false;
    int faceColorIndex = 0;
    int[] colors;
    float bigCubeSize = 100; // Initial size of the big cube
    float smallCubeSize = 50;
    float verySmallCubeSize = 25;
    float offset; // used for small cube offset
    float bigCubeSpeed = 0.1f;
    float smallCubeSpeed = 0.02f;
    boolean shrinkActive = false; // Control whether the cube should shrink
    float angle = 0;
    // used in rotation
    float angleX;
    float angleY;
    float angleZ;

    float sphereY; // Current y-position of the sphere
    float sphereRadius = 5000; // Radius of the sphere
    float movementSpeed = 2; // Speed of vertical movement
    boolean movingUp = true; // Direction control flag
    float sphereOffset;// slightly more than pyramid offset so sphere hits top of pyramid
    float smallSphereRadius = 100f;

    ArrayList<PVector> smallCubePositions = new ArrayList<PVector>();
    boolean cubeClicked = false;
    final int NUM_MODES = 3; // Change this based on the number of modes you have
    boolean[] modes = new boolean[NUM_MODES];
    int currentModeIndex = 0; // Index of the currently active mode

    boolean fillActivated = true;
    boolean extremeColour = false;
    boolean displayDiamond = false; // True for diamond, false for cube
    boolean xRotateDiamond = false;
    boolean yRotateDiamond = false;

    playIntro(MyVisual _papplet) {
        this.mv = _papplet;

        targetRotationY = mv.PI / 37;
        pyramidCenterY = mv.height / 2;
        centerX = mv.width * 0.5f;
        centerY = mv.height * 0.5f;
        offset = mv.height / 0.6f;
        colors = new int[] { mv.color(255, 0, 0), mv.color(0, 255, 0), mv.color(0, 0, 255),
                mv.color(255, 255, 0), mv.color(0, 255, 255),
                mv.color(255, 0, 255) };
        sphereY = mv.height / 2;
        sphereOffset = mv.height / 0.7f;

        String path = "C:\\Users\\luisp\\Desktop\\MusicVisuals\\java\\data\\";
        player = mv.minim.loadFile(path + "introSound.mp3", 512);
        sound1 = mv.minim.loadFile(path + "8bitSound1.mp3", 512);// interactive sounds
        sound2 = mv.minim.loadFile(path + "8bitSound2.mp3", 512);
        sound3 = mv.minim.loadFile(path + "8bitSound3.mp3", 512);
        sound4 = mv.minim.loadFile(path + "8bitSound4.mp3", 512);
        sound5 = mv.minim.loadFile(path + "8bitSound5.mp3", 512);// continue to audio visualizer maybe constraint so
                                                                 // can
        // only be pressed once
        song = mv.minim.loadFile(path + "Radiohead.mp3", 512);

        pyramidTopTargetY = mv.height * 0.1f; // Move near the top of the window
        pyramidBottomTargetY = mv.height * 0.9f; // Move near the bottom of the window

        fft = new FFT(sound1.bufferSize(), sound1.sampleRate());
        bands = 256;
        bandWidth = new float[bands];
        smoothedBands = new float[bands];
        fft.logAverages(22, 5);

        prevAmplitudes = new float[fft.specSize()];
        font = mv.createFont("Monospaced.bold", fontSize);
        mv.textFont(font);

        // Font settings
        mv.textAlign(mv.CENTER, mv.CENTER);

        smallCubePositions.add(new PVector(mv.width / 1.14f, mv.height / 2, 0));
        smallCubePositions.add(new PVector(mv.width / 8.4f, mv.height / 2, 0));
        for (int i = 0; i < NUM_MODES; i++) {
            modes[i] = false;
        }
        modes[currentModeIndex] = true; // Activate the first mode initially
    }

    public void render() {
        mv.background(0); // Set background to black

        if (playIntro) {

            currentRotationY %= mv.TWO_PI;
            if (!startDrawingShapes) { // logic for intro

                drawDiamond();
                drawPyramids();
                drawSoundWave();

                if (soundToVisualize != null) {
                    fft.forward(soundToVisualize.mix);
                    drawRainbowWave();
                    circleVisible = false; // Hide the circle when the rainbow wave is drawn
                } else {
                    // No sound is playing from the interactive sounds, check for first soundtrack
                    // finish
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
                if (circleFadeStartTime > 0 && mv.millis() > circleFadeStartTime && !startFading) {// initial fade
                                                                                                   // in
                    drawFadingCircleWithTiming();
                }

                // Draw the fading circle if visible
                if (circleVisible && !startFading) {
                    drawFadingCircleWithTiming();
                }

                if (spinning && player.position() > 9000) { // Check if 8 seconds have passed
                    // Draw the neon text at the bottom center of the screen
                    drawNeonTextWithFade("Press Enter:", mv.width / 2, mv.height - fontSize,
                            mv.color(0, 255, 255),
                            player.position() - 9000);
                }
            } else if (startDrawingShapes) { // logic for other shapes
                if (modes[0]) {
                    mv.colorMode(mv.HSB, 360, 100, 100); // Set HSB color mode
                    mv.pushMatrix(); // Save the current transformation matrix state
                    drawMovingSphere(mv.width / 2, mv.height / 2, sphereRadius);

                    if (displayDiamond) {
                        drawDiamond();
                    } else {
                        drawCube(bigCubeSize, mv.width / 2, mv.height / 2, 0, bigCubeSpeed);
                    }

                    drawCube(smallCubeSize, mv.width / 2, mv.height / 2 - offset, 0, smallCubeSpeed); // Small
                                                                                                      // cube
                                                                                                      // above
                    drawCube(smallCubeSize, mv.width / 2.6f, mv.height / 2 - offset, 0, smallCubeSpeed); // Small
                                                                                                         // cube
                                                                                                         // above
                    drawCube(smallCubeSize, mv.width / 2, mv.height / 2 + offset, 0, smallCubeSpeed); // Small
                                                                                                      // cube
                                                                                                      // below
                    drawCube(smallCubeSize, mv.width / 2.6f, mv.height / 2 + offset, 0, smallCubeSpeed); // small
                                                                                                         // cube
                                                                                                         // bottom
                    // left
                    drawCube(smallCubeSize, mv.width / 2.6f, mv.height / 2, 0, smallCubeSpeed); // small cube
                                                                                                // middle left
                    drawCube(smallCubeSize, mv.width / 1.65f, mv.height / 2 - offset, 0, smallCubeSpeed); // small
                                                                                                          // cube
                                                                                                          // top
                    // right
                    drawCube(smallCubeSize, mv.width / 1.65f, mv.height / 2, 0, smallCubeSpeed);// small cube
                                                                                                // middle
                    drawCube(smallCubeSize, mv.width / 1.65f, mv.height / 2 + offset, 0, smallCubeSpeed);// small
                                                                                                         // cube
                                                                                                         // bottom
                    // right
                    drawCube(smallCubeSize, mv.width / 1.65f, mv.height / 2 + offset * 1.75f, 0,
                            smallCubeSpeed);// small cube
                    // further
                    // bottom
                    // right
                    drawCube(smallCubeSize, mv.width / 2.6f, mv.height / 2 + offset * 1.75f, 0,
                            smallCubeSpeed);// small cube
                    // further
                    // bottom left
                    drawCube(smallCubeSize, mv.width / 2, mv.height / 2 + offset * 1.75f, 0, smallCubeSpeed);// small
                                                                                                             // cube
                    // further bottom
                    // middle
                    drawCube(smallCubeSize, mv.width / 1.65f, mv.height / 2 - offset * 1.75f, 0,
                            smallCubeSpeed);// small cube
                    // further
                    // top right
                    drawCube(smallCubeSize, mv.width / 2f, mv.height / 2 - offset * 1.75f, 0, smallCubeSpeed);// small
                                                                                                              // cube
                    // further top
                    // middle
                    drawCube(smallCubeSize, mv.width / 2.6f, mv.height / 2 - offset * 1.75f, 0,
                            smallCubeSpeed);// small cube
                    // further top
                    // left
                    drawPyramids();

                    mv.colorMode(mv.RGB, 255, 255, 255); // Switch back to RGB color mode for drawing other
                                                         // elements
                    mv.popMatrix();
                    drawSoundWave();
                    drawCube(verySmallCubeSize, mv.width / 1.14f, mv.height / 2, 0, smallCubeSpeed);// cube
                                                                                                    // inside
                                                                                                    // pyramids
                    drawCube(verySmallCubeSize, mv.width / 8.4f, mv.height / 2, 0, smallCubeSpeed);
                    if (!song.isPlaying()) { // paused logic
                        if (displayDiamond) {
                            drawDiamond();
                        } else {
                            drawCube(bigCubeSize, mv.width / 2, mv.height / 2, 0, bigCubeSpeed);
                        }
                        drawCube(smallCubeSize, mv.width / 2, mv.height / 2 - offset, 0, smallCubeSpeed); // Small
                                                                                                          // cube
                                                                                                          // above
                        drawCube(smallCubeSize, mv.width / 2, mv.height / 2 + offset, 0, smallCubeSpeed); // Small
                                                                                                          // cube
                                                                                                          // below
                        drawSoundWave();
                        mv.fill(173, 216, 230); // light blue
                        mv.text("Paused", mv.width / 2, mv.height - fontSize);
                    }
                } else if (modes[1]) {// swapped modes
                    mv.colorMode(mv.HSB, 360, 100, 100); // Set HSB color mode
                    mv.pushMatrix(); // Save the current transformation matrix state
                    mv.noFill();
                    drawMovingSphere(mv.width / 2, mv.height / 2, smallSphereRadius);
                    drawPyramids();

                    mv.colorMode(mv.RGB, 255, 255, 255); // Switch back to RGB color mode for drawing other
                                                         // elements
                    mv.popMatrix();
                    drawSoundWave();
                    drawRainbowWave();
                    drawCube(verySmallCubeSize, mv.width / 1.14f, mv.height / 2, 0, smallCubeSpeed);// cube
                                                                                                    // inside
                                                                                                    // pyramids
                    drawCube(verySmallCubeSize, mv.width / 8.4f, mv.height / 2, 0, smallCubeSpeed);
                    drawCube(bigCubeSize, mv.width / 1.14f, mv.height / 4, 0, bigCubeSpeed);
                    if (!song.isPlaying()) { // paused logic
                        drawCube(smallCubeSize, mv.width / 2, mv.height / 2 - offset, 0, smallCubeSpeed); // Small
                                                                                                          // cube
                                                                                                          // above
                        drawCube(smallCubeSize, mv.width / 2, mv.height / 2 + offset, 0, smallCubeSpeed); // Small
                                                                                                          // cube
                                                                                                          // below
                        drawSoundWave();
                        mv.fill(173, 216, 230); // light blue
                        mv.text("Paused", mv.width / 2, mv.height - fontSize);
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
            } else if (song.isPlaying()) {
                soundToVisualize = song;
            }

            if (startFading) {
                circleVisible = false;
                long timeElapsed = mv.millis() - fadeStartTime;
                fadeAmount = mv.map(timeElapsed, 0, 3000, 0, 255); // Fade over 3 seconds
                fadeAmount = mv.constrain(fadeAmount, 0, 255); // Ensure fadeAmount does not exceed 255
                updateFading();
                // papplet. println("fade amount"+fadeAmount); //debugging statement

                if (fadeAmount >= 255) {
                    startFading = false;
                    fadeAmount = 0;// make black cover transparent again
                }
            }

            // Apply the fade effect by drawing a rectangle covering the entire screen
            if (fadeAmount > 0) {
                mv.fill(0, fadeAmount);
                mv.rect(0, 0, mv.width, mv.height);
            }
        } else { // prompt user to start intro
            mv.fill(180, 230, 230); // light blue
            mv.text("press space", mv.width / 2, mv.height - fontSize);
            drawDiamond();
            mv.noFill();
            drawMovingSphere(mv.width / 2, mv.height / 2, sphereRadius);
        }
    }

    public void stop() {
        player.close();
        mv.minim.stop();
        mv.stop();
    }

    public void drawDiamond() {
        mv.pushMatrix();
        mv.translate(mv.width / 2, mv.height / 2, -400);

        if (startDrawingShapes) {
            if (xRotateDiamond) {
                if (extremeColour) {
                    mv.rotateX(currentRotationY * 2);
                } else {
                    mv.rotateX(currentRotationY);
                }
            }
            if (yRotateDiamond) {
                if (extremeColour) {
                    mv.rotateY(currentRotationY * 2);
                } else {
                    mv.rotateY(currentRotationY);
                }
            }
        } else {
            mv.rotateX(currentRotationY);
            mv.rotateY(currentRotationY);
        }

        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }

        mv.stroke((mv.frameCount % 255), 255, 255);
        mv.strokeWeight(2);
        if (!startDrawingShapes) {
            mv.fill(340, 100, 100, transparentColour);
        } else if (startDrawingShapes) {
            if (extremeColour) {
                if (fillActivated) {
                    float hue = mv.map(totalAmplitude, 0, 2000, 0, 360); // custom hue for diamond
                    hue = hue % 360;
                    mv.fill(hue, 100, 100);
                } else {
                    mv.noFill();
                }

            } else {
                if (fillActivated) {
                    float hue = mv.map(totalAmplitude, 0, 2000, 300, 360); // custom hue for diamond
                    mv.fill(hue, 100, 100);
                    mv.stroke(255);// white outline
                }

            }

        }

        float size = 180;
        float mid = size / 2;

        // Draw the diamond
        mv.beginShape(mv.TRIANGLES);
        mv.vertex(0, -size, 0);// top pyramid 1
        mv.vertex(-mid, 0, -mid);
        mv.vertex(mid, 0, -mid);

        mv.vertex(0, -size, 0); // top pyramid 2
        mv.vertex(mid, 0, -mid);
        mv.vertex(mid, 0, mid);

        mv.vertex(0, -size, 0); // top pyramid 3
        mv.vertex(mid, 0, mid);
        mv.vertex(-mid, 0, mid);

        mv.vertex(0, -size, 0); // top pyramid 4
        mv.vertex(-mid, 0, mid);
        mv.vertex(-mid, 0, -mid);

        mv.vertex(0, size, 0); // bottom pyramid 1
        mv.vertex(-mid, 0, -mid);
        mv.vertex(mid, 0, -mid);

        mv.vertex(0, size, 0); // bottom pyramid 2
        mv.vertex(mid, 0, -mid);
        mv.vertex(mid, 0, mid);

        mv.vertex(0, size, 0);// bottom pyramid 3
        mv.vertex(mid, 0, mid);
        mv.vertex(-mid, 0, mid);

        mv.vertex(0, size, 0);// bottom pyramid 4
        mv.vertex(-mid, 0, mid);
        mv.vertex(-mid, 0, -mid);

        mv.endShape(mv.CLOSE);

        mv.popMatrix();
    }

    public void drawPyramids() {
        if (spinning) {
            mv.println(transparentColour);
            if (rotationSpeed < 0.05) {
                rotationSpeed += 0.0001;
            }
            pyramidRotation += rotationSpeed; // Keep rotating the pyramids continuously
            if (mv.abs(pyramidXPosTop) < mv.width / 3) {
                pyramidXPosTop -= pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            if (mv.abs(pyramidXPosBottom) < mv.width / 3) {
                pyramidXPosBottom += pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            currentRotationY += rotationSpeed;
            if (transparentColour > 0) {
                transparentColour -= 0.5;
            }
        }

        if (song.isPlaying()) { // rotate
            if (rotationSpeed < 0.05) {
                rotationSpeed += 0.0001;
            }
            pyramidRotation += rotationSpeed; // Keep rotating the pyramids continuously
            if (mv.abs(pyramidXPosTop) < mv.width / 2) {
                pyramidXPosTop -= pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            if (mv.abs(pyramidXPosBottom) < mv.width / 2) {
                pyramidXPosBottom += pyramidMoveSpeed;
            } else {
                pyramidsVisible = true;
            }
            currentRotationY += rotationSpeed;
            if (transparentColour > 0) {
                transparentColour -= 1;
            } else {// else stay
                pyramidsVisible = true;
            }
            pyramidFillAlpha += 2; // Increase alpha gradually
            pyramidFillAlpha = mv.constrain(pyramidFillAlpha, 0, 255); // Limit alpha to max 255

        }

        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }

        // Top pyramid and its additional bottom pyramid
        mv.pushMatrix();
        mv.translate(mv.width / 2 + pyramidXPosTop, mv.height / 2, -200);
        mv.rotateY(pyramidRotation);

        // Draw the top pyramid (inverted)
        mv.pushMatrix();
        mv.translate(0, -pyramidSize, 0);
        mv.rotateX(mv.PI);
        if (playIntro && !startDrawingShapes) {
            mv.fill(totalAmplitude, 50, totalAmplitude, transparentColour);
            mv.println(transparentColour);
            // papplet. println("in intro"); //debugging statement
        }
        if (modes[0] && startDrawingShapes)// colour scheme for mode 0
        {
            // papplet. println("in visualizer");
            if (extremeColour) {
                float hue = mv.map(totalAmplitude, 0, 2000, 40, 180); // Ranges from all colours aggresively
                hue = hue % 360; // Ensure the hue wraps around correctly
                mv.fill(hue, 100, 100);
                // papplet. println("extreme colours");
            } else if (!extremeColour) {
                float hue = mv.map(totalAmplitude, 0, 2000, 240, 360); // Ranges from purple to pink aggresively
                hue = hue % 360; // Ensure the hue wraps around correctly
                mv.fill(hue, 100, 100);
                // papplet. println("not extreme colours");
            }

        } else if (modes[1]) {
            // papplet. println(totalAmplitude); //debugging statement
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

        drawPyramid(pyramidSize);
        mv.popMatrix();

        // Draw the additional bottom pyramid (upright) underneath the top pyramid
        if (mv.abs(pyramidXPosTop) >= mv.width / 3) {
            mv.translate(0, pyramidSize, 0);
            drawPyramid(pyramidSize);
        }
        mv.popMatrix();

        // Bottom pyramid and its additional top pyramid
        mv.pushMatrix();
        mv.translate(mv.width / 2 + pyramidXPosBottom, mv.height / 2, -200);
        mv.rotateY(pyramidRotation);

        // Draw the bottom pyramid (upright)
        mv.pushMatrix();
        mv.translate(0, pyramidSize, 0);
        drawPyramid(pyramidSize);
        mv.popMatrix();

        // Draw the additional top pyramid (inverted) above the bottom pyramid
        if (mv.abs(pyramidXPosBottom) >= mv.width / 3) {
            mv.translate(0, -pyramidSize, 0);
            mv.rotateX(mv.PI);
            drawPyramid(pyramidSize);
        }
        mv.popMatrix();
    }

    public void drawAdditionalPyramid(float size, boolean isTop) {
        mv.translate(0, isTop ? size * 2 : -size * 2, 0); // Adjust position based on whether it's top or bottom
                                                          // pyramid
        drawPyramid(size); // Uses the same drawPyramid method for the new pyramid
    }

    public void drawPyramid(float size) {
        mv.beginShape(mv.TRIANGLES);
        if (pyramidsVisible) {
            mv.strokeWeight(2); // Outlines visible
        } else {
            mv.strokeWeight(0); // Outlines invisible
        }
        mv.vertex(-size / 2, -size / 2, -size / 2);
        mv.vertex(size / 2, -size / 2, -size / 2);
        mv.vertex(0, size / 2, 0);

        mv.vertex(size / 2, -size / 2, -size / 2);
        mv.vertex(size / 2, -size / 2, size / 2);
        mv.vertex(0, size / 2, 0);

        mv.vertex(size / 2, -size / 2, size / 2);
        mv.vertex(-size / 2, -size / 2, size / 2);
        mv.vertex(0, size / 2, 0);

        mv.vertex(-size / 2, -size / 2, size / 2);
        mv.vertex(-size / 2, -size / 2, -size / 2);
        mv.vertex(0, size / 2, 0);
        mv.endShape(mv.CLOSE);
    }

    public void drawSoundWave() {

        mv.noiseDetail(10, 0.3f); // Adjust noise detail for smoother or rougher transitions

        float waveFrequency = 0.5f; // Controls the frequency of the sine wave
        float maxAmplitude = 1f; // double the frequency
        float waveLength = mv.height / 2.75f;// length to properly touch the pyramids

        if (!startDrawingShapes) {
            fft.forward(player.mix);
        } else {
            fft.forward(song.mix);
            waveLength = mv.height / 2.1f;
        }

        if (modes[0] && startDrawingShapes) {
            mv.strokeWeight(15);
        }

        if (extremeColour) {
            mv.colorMode(mv.HSB, 360, 100, 100); // Set HSB color mode
            mv.strokeWeight(28); // increase soundwaves naturally
        }

        // top left pyramid
        for (float y = 0; y <= waveLength; y += 0.1) { // top left pyramid
            int index = (int) mv.map(y, mv.height, 0, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight; // Scale the amplitude based on FFT average
            // Use the x value as the input to the sin function to create a horizontal wave
            float x = mv.sin((mv.height - y) * waveFrequency + mv.frameCount * 0.05f) * amplitude
                    * maxAmplitude;
            x += mv.width / 4.1f; // line up with pyramid properly for full screen

            // Change the color over time based on the amplitude
            int colorValue = (int) mv.map(amplitude, 0, maxAmplitude, 0, 255);
            mv.stroke(mv.color(255 - colorValue, colorValue, 255));

            // Draw the wave by connecting points vertically
            mv.line(x, y, x, y - 0.1f); // Draw points upward for a vertical wave
        }

        // top right pyramid
        for (float y = 0; y <= waveLength; y += 0.1) { // top left pyramid
            int index = (int) mv.map(y, mv.height, 0, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight; // Scale the amplitude based on FFT average
            // Use the x value as the input to the sin function to create a horizontal wave
            float x = mv.sin((mv.height - y) * waveFrequency + mv.frameCount * 0.05f) * amplitude
                    * maxAmplitude;
            x += mv.width / 1.323f; // line up with pyramid full screen

            // Change the color over time based on the amplitude
            int colorValue = (int) mv.map(amplitude, 0, maxAmplitude, 0, 255);
            mv.stroke(mv.color(255 - colorValue, colorValue, 255));

            // Draw the wave by connecting points vertically
            mv.line(x, y, x, y - 0.1f); // Draw points upward for a vertical wave
        }

        // bottom left pyramid
        float bottomWaveAmplitudeScale = 0.1f; // Adjust this to make the bottom waves less reactive

        for (float y = mv.height; y >= mv.height - waveLength; y -= 0.1) {
            int index = (int) mv.map(mv.height - y, 0, mv.height, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = mv.sin(((mv.height - y) * waveFrequency - mv.frameCount * 0.2f)) * amplitude
                    * maxAmplitude;
            x += mv.width / 4.1f;

            int colorValue = (int) mv.map(amplitude, 0, maxAmplitude, 0, 255);
            mv.stroke(mv.color(255 - colorValue, colorValue, 255));

            mv.line(x, y, x, y + 0.1f);
        }

        // bottom right pyramid
        for (float y = mv.height; y >= mv.height - waveLength; y -= 0.1) {
            int index = (int) mv.map(mv.height - y, 0, mv.height, 0, fft.avgSize() - 1);
            float amplitude = fft.getAvg(index) * waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = mv.sin(((mv.height - y) * waveFrequency - mv.frameCount * 0.2f)) * amplitude
                    * maxAmplitude;
            x += mv.width / 1.323f;

            int colorValue = (int) mv.map(amplitude, 0, maxAmplitude, 0, 255);
            mv.stroke(mv.color(255 - colorValue, colorValue, 255));

            mv.line(x, y, x, y + 0.1f);
        }
    }

    // Assuming 'rotationAngle' is a global float variable initialized to 0.0

    public void drawRainbowWave() {
        mv.colorMode(mv.HSB, 360, 100, 100);
        // Recalculate angleStep based on the current fft specSize
        float angleStep = mv.TWO_PI / fft.specSize();

        // Calculate maximum FFT amplitude for normalization
        for (int i = 0; i < fft.specSize(); i++) {
            maxFFTAmplitude = mv.max(maxFFTAmplitude, fft.getBand(i));
        }

        // Ensure there's no divide by zero issue
        if (maxFFTAmplitude == 0) {
            maxFFTAmplitude = 1;
        }

        // Increment the rotation angle, adjust the speed as necessary
        rainbowWaveRotationAngle += 0.01;

        // Start matrix transformation
        mv.pushMatrix();
        // Translate to the center of the screen
        mv.translate(mv.width / 2, mv.height / 2);
        // Rotate the whole wave by the current rotation angle
        if (modes[0]) {
            mv.rotate(rainbowWaveRotationAngle);
        }
        mv.noFill();
        mv.strokeWeight(3);
        mv.beginShape();

        for (int i = 0; i < fft.specSize(); i++) {
            float amplitude = fft.getBand(i);
            // Smooth the transition of amplitude values
            prevAmplitudes[i] = mv.lerp(prevAmplitudes[i], amplitude, smoothingFactor);
            // Normalize and scale the amplitude
            float normalizedAmplitude = mv.map(prevAmplitudes[i], 0, maxFFTAmplitude, 0, maxWaveAmplitude);

            // Calculate wave points
            float x = (baseRadius + normalizedAmplitude) * mv.cos(i * angleStep);
            float y = (baseRadius + normalizedAmplitude) * mv.sin(i * angleStep);

            // Dynamic coloring based on position
            int colorValue = (int) (128 + 128 * mv.sin(i * 0.1f + mv.frameCount * 0.02f));
            mv.stroke(mv.color(255 - colorValue, colorValue, 255));

            mv.vertex(x, y);
        }

        mv.endShape(mv.CLOSE);
        mv.popMatrix(); // Restore matrix state
    }

    public void drawFadingCircleWithTiming() {
        if (circleOpacity < 255) {
            circleOpacity += 5; // Control the speed of the fade-in effect
        }

        // Draw the circle with the current opacity
        int colorValue = (int) (128 + 128 * mv.sin(mv.frameCount * 0.05f));
        mv.noFill(); // Do not fill the circle
        mv.stroke(mv.color(255 - colorValue, colorValue, 255), circleOpacity); // Set the stroke color and
                                                                               // opacity
        mv.strokeWeight(2); // Set the stroke width
        mv.ellipse(mv.width / 2, mv.height / 2, circleMaxRadius * 2, circleMaxRadius * 2); // Draw the
                                                                                           // circle
        // centered
    }

    // Fading circle draw function
    public void drawFadingCircle() {
        if (circleOpacity < 255) {
            circleOpacity += 5; // Control the speed of the fade-in effect
        }
        int colorValue = (int) (128 + 128 * mv.sin(mv.frameCount * 0.05f));
        mv.noFill();
        mv.stroke(mv.color(255 - colorValue, colorValue, 255), circleOpacity);
        mv.strokeWeight(2);
        mv.ellipse(mv.width / 2, mv.height / 2, circleMaxRadius * 2, circleMaxRadius * 2);
    }

    public void keyPressed() {
        if (mv.key == ' ') {
            spinning = !spinning; // Toggle spinning state
            if (spinning) {
                player.play();
                playIntro = true;
                hasStartedPlaying = true;
                isFirstSoundtrackFinished = false; // Reset this flag when the first soundtrack starts
                circleOpacity = 0; // Reset opacity to allow fade-in effect
                // Set the start time for the circle to fade in (e.g., after 10 seconds)
                circleFadeStartTime = mv.millis() + 10000; // 10,000 milliseconds from now
            } else {
                player.pause();
            }
        } else if (mv.key == '1') {
            if (!startDrawingShapes) {
                playSound(sound1);
            }
        } else if (mv.key == '2') {
            if (!startDrawingShapes) {
                playSound(sound2);
            }
        } else if (mv.key == '3') {
            if (!startDrawingShapes) {
                playSound(sound3);
            }
        } else if (mv.key == '4') {
            if (!startDrawingShapes) {
                playSound(sound4);
            }
        } else if (mv.key == mv.ENTER) {
            playIntro = true;
            isFirstSoundtrackFinished = true;
            drawRainbowWave();
            playSound(sound5);
            startFading = true;
            fadeStartTime = mv.millis(); // will eventually trigger startDrawingShapes boolean

        } else if (mv.key == '5') {
            playSound(song);
            startFading = false;
            startDrawingShapes = true; // manually start
        }

        if (startDrawingShapes) { // when we are using audio visualization for shapes, we can pause and resume
            if (mv.key == 'p' || mv.key == 'P') {
                if (song.isPlaying()) {
                    song.pause();
                    movePyramidTopUp = false;
                    movePyramidBottomDown = false;
                } else {
                    song.play();
                }
            }

            if (mv.keyCode == mv.UP) {// controlling cube speeds
                if (bigCubeSpeed > -1) {
                    bigCubeSpeed += 0.1;
                    xRotateDiamond = true;
                }
            } else if (mv.keyCode == mv.DOWN) {
                if (bigCubeSpeed > 0) {
                    bigCubeSpeed -= 0.1;
                    xRotateDiamond = false;
                }
            } else if (mv.keyCode == mv.LEFT) {
                if (smallCubeSpeed > 0) {
                    smallCubeSpeed -= 0.1;
                    yRotateDiamond = false;
                }
            } else if (mv.keyCode == mv.RIGHT) {
                if (smallCubeSpeed > -1) {
                    smallCubeSpeed += 0.1;
                    yRotateDiamond = true;
                }
            }

            if (mv.keyCode == 'f' || mv.keyCode == 'F') {
                fillActivated = !fillActivated;
            }
            if (mv.keyCode == 'e' || mv.keyCode == 'E') {
                if (extremeColour == true) {
                    extremeColour = false;
                } else if (extremeColour == false) {
                    extremeColour = true;
                }
            }
        }

        // You can add more conditions for additional sounds here
    }

    public void mouseClicked() {
        for (int i = 0; i < smallCubePositions.size(); i++) {
            PVector cubePos = smallCubePositions.get(i);
            float screenX = mv.screenX(cubePos.x, cubePos.y, cubePos.z);
            float screenY = mv.screenY(cubePos.x, cubePos.y, cubePos.z);
            float distance = mv.dist(mv.mouseX, mv.mouseY, screenX, screenY);

            if (distance < 20) {
                mv.println("Clicked on cube at " + cubePos);
                if (i == 0) { // Assuming index 0 is the left cube
                    displayDiamond = false;
                } else if (i == 1) { // Assuming index 1 is the right cube
                    displayDiamond = true;
                }
                break;
            }
        }
    }

    public void handleCubeClick(PVector cubePosition) {
        // Handle the event when a cube is clicked
        mv.println("Cube clicked at: " + cubePosition);
        cycleModes();
        cubeClicked = true;
    }

    void cycleModes() {
        modes[currentModeIndex] = false; // Deactivate the current mode
        // currentModeIndex = (currentModeIndex + 1) % NUM_MODES; // Move to the next
        // mode, wrapping around if necessary
        modes[currentModeIndex] = true; // Activate the new mode
    }

    public void updateFading() {
        if (startFading && mv.millis() - fadeStartTime > fadeDuration) {
            startFading = false; // Stop fading after 6 seconds
            startDrawingShapes = true;
            playSound(song);
        }

        if (startFading) {
            // Perform fading logic here
            fadeAmount = mv.map(mv.millis() - fadeStartTime, 0, fadeDuration, 0, 255);
            fadeAmount = mv.constrain(fadeAmount, 0, 255);
        }
    }

    private void playSound(AudioPlayer sound) {
        if (sound == null) {
            mv.println("Error: Attempted to play a null sound.");
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
        float fade = mv.map(time, 0, 2000, 0, 255); // fade in over 2 seconds
        fade = mv.constrain(fade, 0, 255); // Make sure fade doesn't go beyond 255

        // Set the text size for the solid text on top
        mv.textSize(fontSize);

        // Draw the glowing text with fewer layers for a simpler look
        mv.fill(glowColor, fade); // Use fade for alpha
        for (int i = 3; i > 0; i--) { // Only 3 layers of glow for simplicity
            mv.text(text, x, y + i); // Slight offset for the glow layers
        }

        // Draw the solid text on top
        mv.fill(255, fade); // Use fade for alpha
        mv.text(text, x, y); // Draw the text at the original position
    }

    void drawCube(float side, float x, float y, float z, float cubeSpeed) {

        float halfSide = side / 2;
        // papplet. println("cube size:"+side+"cubespeed:"+cubeSpeed); //debugging
        // statement

        // Perform FFT analysis on the current audio playing
        fft.forward(song.mix);

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

        float hue = mv.map(totalAmplitude, 0, 3000, 180, 360); // Ranges from blue to purple to pink mostly
        if (extremeColour) {
            hue = mv.map(totalAmplitude, 0, 200, 0, 360); // Ranges from all colours aggresively
            hue = hue % 360; // Ensure the hue wraps around correctly
        }

        // Rotate based on the average amplitudes
        angleX += mv.map(bassAvg, 0, 10, 0, mv.PI / 200); // Scale these factors as needed
        angleY += mv.map(midAvg, 0, 10, 0, mv.PI / 200);
        angleZ += mv.map(trebleAvg, 0, 10, 0, mv.PI / 200);

        float totalLoudness = 0; // Initialize total loudness

        // Sum all amplitudes to calculate total loudness
        for (int i = 0; i < fft.specSize(); i++) {
            totalLoudness += fft.getBand(i);
        }

        float normalizedLoudness = mv.map(totalLoudness, 0, 200, 1, 10); // Adjust range 0-200 to 1-10,
        normalizedLoudness = mv.constrain(normalizedLoudness, 0, 3); // Ensure stroke weight doesn't get too high
        mv.pushMatrix();
        mv.translate(x, y, z);
        mv.rotateX(angleX * cubeSpeed);
        mv.rotateY(angleY * cubeSpeed);
        mv.rotateZ(angleZ * cubeSpeed);

        if (modes[0]) {
            mv.strokeWeight(normalizedLoudness); // Set the outline weight
            if (song.isPlaying()) {
                mv.strokeWeight(normalizedLoudness); // Set the outline weight
                mv.stroke(hue, 100, 100);
            } else {
                mv.strokeWeight(2); // thin outline
                mv.stroke(255);// white outline
            }
            if (side <= 26f || fillActivated) {// if cubes are very small we want to fill them or if fill activated
                mv.fill(hue, 100, 100);
                mv.strokeWeight(2); // thin outline
                mv.stroke(255);// white outline
            } else {
                mv.noFill(); // Do not fill the shapes
            }
        } else if (modes[1]) {
            mv.fill(hue, 100, 100);
        }

        mv.beginShape(mv.QUADS);
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
        mv.endShape(mv.CLOSE);
        mv.popMatrix();
    }

    void drawMovingSphere(float x, float y, float r) {
        mv.pushMatrix(); // Save the current state of transformations
        mv.translate(x, y); // Use the dynamic `sphereY` for y-position

        if (!startDrawingShapes) {
            angle += 0.01; // Continuously rotate the sphere
        } else if (modes[0] && startDrawingShapes) {
            angle += 0.001;
        }

        if (song.isPlaying() || !playIntro) {

            mv.println("" + song.isPlaying());

            mv.rotateX(angle);

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
            movementSpeed = mv.map(bassAmplitude, 0, 10, 1, 5);
            movementSpeed = mv.constrain(movementSpeed, 1, 5);

            // Setting HSB color mode
            mv.colorMode(mv.HSB, 360, 100, 100);

            // Optional: Adjust color based on treble amplitude
            float hue = mv.map(trebleAmplitude, 0, 2000, 0, 360); // Half range of hue
            float saturation = mv.map(bassAmplitude, 0, 10, 20, 100); // Saturation increases with bass
            float brightness = 100; // Always full brightness for visibility

            float totalAmplitude = 0;

            for (int i = 0; i < fft.specSize(); i++) { // needs to be added for extreme colours to work
                totalAmplitude += fft.getBand(i);
            }

            // Adjust strokepapplet. width dynamically for a pulsing effect
            float strokeWeightValue = mv.map(trebleAmplitude, 0, 10, 0.5f, 15);
            mv.noFill();
            mv.strokeWeight(strokeWeightValue);
            if (modes[0] && startDrawingShapes) {
                if (extremeColour) {
                    hue = mv.map(totalAmplitude, 0, 2000, 0, 360); // Ranges from half colour wheel
                    hue = hue % 360; // Ensure the hue wraps around correctly
                    mv.stroke(hue, 100, 100);
                    r = r / 5; // widen view for more aggresive effect
                } else if (!extremeColour) {
                    hue = mv.map(totalAmplitude, 0, 2000, 0, 80); // Ranges from other half colour wheel
                    hue = hue % 360; // Ensure the hue wraps around correctly
                    mv.stroke(hue, 100, 100);
                    // papplet. println("not extreme colours");
                }
            } else {
                mv.println("----------------------");
                hue = mv.map(totalAmplitude, 0, 2000, 300, 360); // Ranges from half colour wheel
                hue = hue % 360; // Ensure the hue wraps around correctly
                mv.stroke(hue, 100, 100);
            }

            // Draw the sphere with the constant radius
            mv.sphere(r);
        } else {
            // Default color when music is paused
            mv.colorMode(mv.RGB, 255); // Switch back to RGB for consistent color handling
            // papplet. fill(255, 0, 100, 100);
            mv.noFill();
            mv.stroke(255);
            mv.strokeWeight(1);
            mv.sphere(r);
        }

        mv.popMatrix(); // Restore original state of transformations
    }

}
