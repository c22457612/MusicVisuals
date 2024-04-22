package example;

import example.IntroVisual.IntroVisualScreen;
import example.screens.PolygonEye.PolygonEyeScreen;
import ie.tudublin.*;

public class MyVisual extends Visual {
    ScreenIndex sIndex; // Screen index
    Drawable[] screens; // Screens' array

    int NUM_SCREENS = 10;

    // WaveForm wf;
    PolygonEyeScreen v1;
    IntroVisualScreen v2;

    public void settings() {
        size(1800, 900);

        // Use this to make fullscreen
        // fullScreen();

        // Use this to make fullscreen and use P3D for 3D graphics
        // fullScreen(P3D, SPAN);
    }

    public void setup() {
        startMinim();

        colorMode(HSB);

        // Call loadAudio to load an audio file to process
        String path = "C:\\Users\\luisp\\Desktop\\tud\\Year 2\\OOP\\2\\MusicVisuals\\java\\data\\";
        String fileName = "heroplanet.mp3";
        loadAudio(path + fileName);

        // Call this instead to read audio from the microphone

        // startListening();

        // Screen index object
        sIndex = new ScreenIndex(0);

        // Array of screens
        screens = new Drawable[NUM_SCREENS];

        screens[0] = new IntroVisualScreen(this);
        screens[1] = new PolygonEyeScreen(sIndex, this);
    }

    public void keyPressed() {
        if (key == ' ') {
            getAudioPlayer().cue(0);
            getAudioPlayer().play();
        } else if (key == CODED) { // Key is a special key
            if (keyCode == RIGHT) {
                // Code to execute when the right arrow is pressed
                sIndex.value += 1;
                sIndex.value %= NUM_SCREENS;
            } else if (keyCode == LEFT) {
                // Code to execute when the right arrow is pressed
                sIndex.value -= 1;
                sIndex.value %= NUM_SCREENS;
            }
        }

    }

    public void draw() {
        background(0);
        try {
            // Call this if you want to use FFT data
            calculateFFT();
        } catch (VisualException e) {
            e.printStackTrace();
        }
        // Call this is you want to use frequency bands
        calculateFrequencyBands();

        // Call this is you want to get the average amplitude
        calculateAverageAmplitude();
        screens[0].render();

        //screens[sIndex.value].render();
    }
    
}
