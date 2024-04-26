package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import java.util.ArrayList;
import ddf.minim.*;
import ddf.minim.analysis.FFT;

import example.*;
import example.screens.PolygonEye.PolygonEyeScreen;

public class IntroVisualScreen {
    MyVisual mv;

    AudioPlayer player;
    FFT fft;
    int bands;
    float[] bandWidth;
    float[] smoothedBands;
    float waveHeight = 5; // mv. Height of the soundwave

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
    Pyramids pyramids;
    Sphere sphere;
    SoundWave soundWave;
    RainbowWave rainbowWave;
    ControlManager controlManager;

    FadingCircle fadingCircle;
    float cx;
    float cy;

    PolygonEyeScreen myPolygonEye;

    public IntroVisualScreen(MyVisual _mv) {
        this.mv = _mv;

        targetRotationY = mv.PI / 37;
        pyramidCenterY = mv.height / 21.6f;
        centerX = mv.width * 0.026041f;
        centerY = mv.height * 0.046296f;
        colors = new int[] { mv.color(255, 0, 0), mv.color(0, 255, 0), mv.color(0, 0, 255), mv.color(255, 255, 0),
                mv.color(0, 255, 255), mv.color(255, 0, 255) };
        // offset = mv.height / 6.48f;
        offset = 190;
        sphereY = mv.height / 21.6f;
        sphereOffset = mv.height / 7.55999f;
        cx = mv.width / 38.4f;
        cy = mv.height / 21.6f;

        mv.println("------------------------------");
        mv.println("Screen width: " + mv.width / 2);
        mv.println("Screen height: " + mv.height / 2);
        mv.println("targetRotationY: " + targetRotationY);
        mv.println("pyramidCenterY: " + pyramidCenterY);
        mv.println("centerX: " + centerX);
        mv.println("centerY: " + centerY);
        mv.println("offset: " + offset);
        mv.println("sphereY: " + sphereY);
        mv.println("sphereOffset: " + sphereOffset);
        mv.println("cx: " + cx);
        mv.println("cy: " + cy);

        player = mv.minim.loadFile("introSound.mp3", 512);
        sound1 = mv.minim.loadFile("8bitSound1.mp3", 512);// interactive sounds
        sound2 = mv.minim.loadFile("8bitSound2.mp3", 512);
        sound3 = mv.minim.loadFile("8bitSound3.mp3", 512);
        sound4 = mv.minim.loadFile("8bitSound4.mp3", 512);
        sound5 = mv.minim.loadFile("8bitSound5.mp3", 512);// continue to audio visualizer maybe constraint so
                                                          // can
        // only be pressed once
        song = mv.minim.loadFile("pushup.mp3", 512);

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

        // PolygonEye
        myPolygonEye = new PolygonEyeScreen(_mv);

        // big cube initialization
        bigCube = new Cube(mv, bigCubeSize, mv.width / 2, mv.height / 2, 0, bigCubeSpeed, fft, song, extremeColour,
                modes,
                fillActivated);
        smallCubeAbove = new Cube(mv, smallCubeSize, mv.width / 2, mv.height / 2 - offset, 0, smallCubeSpeed, fft,
                song,
                extremeColour, modes, fillActivated);
        smallCubeAboveLeft = new Cube(mv, smallCubeSize, mv.width / 2.6f, mv.height / 2 - offset, 0, smallCubeSpeed,
                fft,
                song, extremeColour, modes, fillActivated);
        smallCubeAboveRight = new Cube(mv, smallCubeSize, mv.width / 1.65f, mv.height / 2 - offset, 0, smallCubeSpeed,
                fft,
                song, extremeColour, modes, fillActivated);
        smallCubeMiddleLeft = new Cube(mv, smallCubeSize, mv.width / 2.6f, mv.height / 2, 0, smallCubeSpeed, fft,
                song,
                extremeColour, modes, fillActivated);
        smallCubeMiddleRight = new Cube(mv, smallCubeSize, mv.width / 1.65f, mv.height / 2, 0, smallCubeSpeed, fft,
                song,
                extremeColour, modes, fillActivated);
        smallCubeBottomLeft = new Cube(mv, smallCubeSize, mv.width / 2.6f, mv.height / 2 + offset, 0, smallCubeSpeed,
                fft,
                song, extremeColour, modes, fillActivated);
        smallCubeBottom = new Cube(mv, smallCubeSize, mv.width / 2, mv.height / 2 + offset, 0, smallCubeSpeed, fft,
                song,
                extremeColour, modes, fillActivated);
        smallCubeBottomRight = new Cube(mv, smallCubeSize, mv.width / 1.65f, mv.height / 2 + offset, 0,
                smallCubeSpeed,
                fft,
                song, extremeColour, modes, fillActivated);
        smallCubeFurtherAbove = new Cube(mv, smallCubeSize, mv.width / 2, mv.height / 2 - offset * 1.75f, 0,
                smallCubeSpeed,
                fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherAboveLeft = new Cube(mv, smallCubeSize, mv.width / 2.6f, mv.height / 2 - offset * 1.75f, 0,
                smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherAboveRight = new Cube(mv, smallCubeSize, mv.width / 1.65f, mv.height / 2 - offset * 1.75f, 0,
                smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherBottom = new Cube(mv, smallCubeSize, mv.width / 2, mv.height / 2 + offset * 1.75f, 0,
                smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherBottomLeft = new Cube(mv, smallCubeSize, mv.width / 2.6f, mv.height / 2 + offset * 1.75f, 0,
                smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        smallCubeFurtherBottomRight = new Cube(mv, smallCubeSize, mv.width / 1.65f, mv.height / 2 + offset * 1.75f, 0,
                smallCubeSpeed, fft, song, extremeColour, modes, fillActivated);
        verySmallCubeLeft = new Cube(mv, verySmallCubeSize, mv.width / 1.14f, mv.height / 2, 0, smallCubeSpeed, fft,
                song,
                extremeColour, modes, fillActivated);
        verySmallCubeRight = new Cube(mv, verySmallCubeSize, mv.width / 8.4f, mv.height / 2, 0, smallCubeSpeed, fft,
                song,
                extremeColour, modes, fillActivated);
        diamond = new Diamond(mv, fft, currentRotationY, extremeColour, fillActivated, transparentColour,
                startDrawingShapes, xRotateDiamond, yRotateDiamond, playIntro);
        pyramids = new Pyramids(mv, fft, spinning, rotationSpeed, pyramidRotation, pyramidXPosTop, pyramidXPosBottom,
                pyramidMoveSpeed, pyramidsVisible, currentRotationY, song, pyramidFillAlpha, pyramidSize, playIntro,
                startDrawingShapes, modes, extremeColour, transparentColour, fillActivated);
        sphere = new Sphere(mv, fft, song, playIntro, startDrawingShapes, modes, fillActivated, extremeColour,
                sphereRadius, movementSpeed);
        soundWave = new SoundWave(mv, fft, waveHeight, modes, extremeColour, startDrawingShapes, player, song,
                mv.width,
                mv.height, mv.frameCount);
        rainbowWave = new RainbowWave(mv, fft, baseRadius, maxWaveAmplitude, smoothingFactor, maxFFTAmplitude, modes,
                prevAmplitudes);
        controlManager = new ControlManager(mv, smallCubePositions, modes, currentModeIndex, displayDiamond);
        fadingCircle = new FadingCircle(mv, circleOpacity, circleMaxRadius);

        // polygon =new Polygon(this,);
    }

    public void render() {
        mv.background(0); // Set background to black
        if (playIntro) {

            currentRotationY %= mv.TWO_PI;
            if (!startDrawingShapes) { // logic for intro
                // mv.println("not started drawing shapes");
                // drawDiamond();
                diamond.render();
                // drawPyramids();
                pyramids.drawPyramids();
                soundWave.render();
                // drawSoundWave();
                sphere.render(mv.width / 2, mv.height / 2, sphereRadius);

                if (soundToVisualize != null) {
                    fft.forward(soundToVisualize.mix);
                    drawRainbowWave();
                    // rainbowWave.drawRainbowWave();
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
                    // mv.println("in block 1");
                    // drawFadingCircleWithTiming();
                    // fadingCircle.getCircleOpacity(circleOpacity);
                    fadingCircle.drawFadingCircleWithTiming();
                }

                // Draw the fading circle if visible
                if (circleVisible && !startFading) {
                    drawFadingCircleWithTiming();
                    mv.println("in block 2");
                    // drawFadingCircleWithTiming();
                    fadingCircle.drawFadingCircleWithTiming();
                }

                if (spinning && player.position() > 9000) { // Check if 8 seconds have passed
                    // Draw the neon text at the bottom center of the screen
                    drawNeonTextWithFade("Press Enter:", mv.width / 2, mv.height - fontSize, mv.color(0, 255, 255),
                            player.position() - 9000);
                }
            } else if (startDrawingShapes) { // logic for other shapes
                if (modes[0]) {
                    mv.colorMode(mv.HSB, 360, 100, 100); // Set HSB color mode
                    mv.pushMatrix(); // Save the current transformation matrix state
                    // drawMovingSphere(mv.width /2,mv. height/2, sphereRadius);
                    sphere.setStartDrawingShapes(startDrawingShapes);
                    sphere.render(mv.width / 2, mv.height / 2, sphereRadius);
                    if (displayDiamond) {
                        diamond.setDrawingShapes(startDrawingShapes);
                        diamond.setExtremeColour(extremeColour);
                        diamond.render();
                    } else {
                        bigCube.render();
                    }
                    smallCubeAbove.render();
                    smallCubeAboveLeft.render();
                    smallCubeAboveRight.render();
                    smallCubeMiddleLeft.render();
                    smallCubeMiddleRight.render();
                    smallCubeBottomLeft.render();
                    smallCubeBottom.render();
                    smallCubeBottomRight.render();
                    smallCubeFurtherAbove.render();
                    smallCubeFurtherAboveLeft.render();
                    smallCubeFurtherAboveRight.render();
                    smallCubeFurtherBottom.render();
                    smallCubeFurtherBottomLeft.render();
                    smallCubeFurtherBottomRight.render();
                    pyramids.setDrawingShapes(startDrawingShapes);
                    pyramids.drawPyramids();

                    mv.colorMode(mv.RGB, 255, 255, 255); // Switch back to RGB color mode for drawing other elements
                    mv.popMatrix();
                    // drawSoundWave();
                    soundWave.setStartDrawingShapes(startDrawingShapes);
                    soundWave.render();
                    verySmallCubeLeft.render();
                    verySmallCubeRight.render();
                    if (!song.isPlaying()) { // paused logic
                        if (displayDiamond) {
                            diamond.render();
                        } else {
                            bigCube.render();
                            // drawCube(bigCubeSize,width/2,height/2,0,bigCubeSpeed);
                        }
                        soundWave.render();
                        sphere.setSongStatus();
                        sphere.render(mv.width / 2, mv.height / 2, sphereRadius);
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
                // mv.println("fade amount"+fadeAmount); //debugging statement

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
            diamond.render();
            mv.noFill();
            // drawMovingSphere(mv.width /2,mv. height/2, sphereRadius);
            sphere.render(mv.width / 2, mv.height / 2, sphereRadius);
        }

    }

    public void stop() {
        player.close();
        mv.minim.stop();
        mv.stop();
    }

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
        mv.stroke(mv.color(255 - colorValue, colorValue, 255), circleOpacity); // Set the stroke color and opacity
        mv.strokeWeight(2); // Set the strokemv. width
        mv.ellipse(mv.width / 2, mv.height / 2, circleMaxRadius * 2, circleMaxRadius * 2); // Draw the circle centered
    }

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
        if (mv.key == 'd' || mv.key == 'D' || mv.key == 'a' || mv.key == 'A') {
            mv.currScreen++;
            mv.currScreen %= mv.NUM_SCREENS;
        } else {
            if (mv.key == ' ') {
                spinning = !spinning; // Toggle spinning state
                if (spinning) {
                    player.play();
                    playIntro = true;
                    diamond.setPlayIntro(playIntro);
                    pyramids.setSpinning(spinning);
                    pyramids.setPlayIntro(playIntro); // this will allow pyramids to work in intro
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
                // rainbowWave.drawRainbowWave();
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
                        bigCube.setCubeSpeedUp();
                        xRotateDiamond = true;
                        diamond.setxRotateDiamond(xRotateDiamond);
                    }
                } else if (mv.keyCode == mv.DOWN) {
                    if (bigCubeSpeed > 0) {
                        bigCube.setCubeSpeedDown();
                        xRotateDiamond = false;
                        diamond.setxRotateDiamond(xRotateDiamond);
                    }
                } else if (mv.keyCode == mv.LEFT) {
                    if (smallCubeSpeed > 0) {
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
                        yRotateDiamond = false;
                        diamond.setyRotateDiamond(yRotateDiamond);
                    }
                } else if (mv.keyCode == mv.RIGHT) {
                    if (smallCubeSpeed > -1) {
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
                        yRotateDiamond = true;
                        diamond.setyRotateDiamond(yRotateDiamond);
                    }
                }
                if (mv.keyCode == 'f' || mv.keyCode == 'F') {
                    fillActivated = !fillActivated;
                    // bigCube.setFillActivated(fillActivated);
                    // diamond.setFillActivated(fillActivated);
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
                    pyramids.setfillActivated(fillActivated);
                    sphere.setFillActivated(fillActivated);
                }
                if (mv.keyCode == 'm' || mv.keyCode == 'M') { // middle or main objects on screen control
                    fillActivated = !fillActivated;
                    bigCube.setFillActivated(fillActivated);
                    diamond.setFillActivated(fillActivated);
                }
                if (mv.keyCode == 'e' || mv.keyCode == 'E') {
                    extremeColour = !extremeColour;
                    mv.println("extremeColour toggled to: " + extremeColour);
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
                    pyramids.setExtremeColour(extremeColour);
                    sphere.setExtremeColour(extremeColour);
                    soundWave.setExtremeColour(extremeColour);
                }
            }
        }
        // You can add more conditions for additional sounds here
    }

    public void mouseClicked() {
        controlManager.mouseClicked();
        displayDiamond = controlManager.displayDiamond; // Update the main display flag after interaction
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

    public void getTransparentColour(float transparentColour) {
        diamond.transparentColour = transparentColour;
    }

}