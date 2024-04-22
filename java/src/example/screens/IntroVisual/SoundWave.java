package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class SoundWave {
    private PApplet parent; // Reference to the PApplet for drawing
    private FFT fft; // FFT object for analyzing the audio
    private float waveHeight; // Height of the wave
    private boolean[] modes;
    private boolean extremeColour;
    private boolean startDrawingShapes;
    private AudioPlayer player;
    private AudioPlayer song;
    private float width;
    private float height;
    private int frameCount;
    

    // Constructor
    public SoundWave(PApplet parent, FFT fft, float waveHeight, boolean[] modes, boolean extremeColour, boolean startDrawingShapes, AudioPlayer player, AudioPlayer song, float width, float height, int frameCount) {
        this.parent = parent;
        this.fft = fft;
        this.waveHeight = waveHeight;
        this.modes = modes;
        this.extremeColour = extremeColour;
        this.startDrawingShapes = startDrawingShapes;
        this.player = player;
        this.song = song;
        this.width = width;
        this.height = height;
        this.frameCount = frameCount;
    }

    public void drawSoundWave(){
        
        parent.noiseDetail(10, 0.3f); // Adjust noise detail for smoother or rougher transitions

        float waveFrequency = 0.7f; // Controls the frequency of the sine wave
        float maxAmplitude = 1.4f; //double the frequency
        float waveLength = this.height/2.75f;// length to properly touch the pyramids
        float thicknessTop=0.1f;
        float thicknessBottom=0.1f;
        

        if (!this.startDrawingShapes){
            this.fft.forward(this.player.mix);
        }else
        {
            this.fft.forward(this.song.mix);
            waveLength=height/2.1f;
        }

        if (this.modes[0] && this.startDrawingShapes){
            parent.strokeWeight(15);
            thicknessTop=1f;
            thicknessBottom=1.5f;
        }

        if (this.extremeColour){
            parent.colorMode(PConstants.HSB, 360, 100, 100);  // Set HSB color mode
            parent.strokeWeight(28); // increase soundwaves naturally
        }
        
        
        //top left pyramid
        for (float y = 0; y <=waveLength ; y+=0.1) { //top left pyramid
            int index = (int) PApplet.map(y, this.height, 0, 0, this.fft.avgSize() - 1);
            float amplitude = this.fft.getAvg(index) * this.waveHeight; // Scale the amplitude based on FFT average
            // Use the x value as the input to the sin function to create a horizontal wave
            float x = PApplet.sin((this.height - y) * waveFrequency + this.frameCount * 0.05f) * amplitude * maxAmplitude;
            x += width/4.1f; // line up with pyramid properly for full screen
        
            // Change the color over time based on the amplitude
            int colorValue = (int) PApplet.map(amplitude, 0, maxAmplitude, 0, 255);
            parent.stroke(parent.color(255 - colorValue, colorValue, 255));
        
            // Draw the wave by connecting points vertically
            parent.line(x, y, x, y - thicknessTop); // Draw points upward for a vertical wave
        }

        //top right pyramid
        for (float y = 0; y <=waveLength ; y+=0.1) { //top left pyramid
            int index = (int) PApplet.map(y, this.height, 0, 0, this.fft.avgSize() - 1);
            float amplitude = this.fft.getAvg(index) * this.waveHeight; // Scale the amplitude based on FFT average
            // Use the x value as the input to the sin function to create a horizontal wave
            float x = PApplet.sin((this.height - y) * waveFrequency + this.frameCount * 0.05f) * amplitude * maxAmplitude;
            x += this.width/1.323f; // line up with pyramid full screen
        
            // Change the color over time based on the amplitude
            int colorValue = (int) PApplet.map(amplitude, 0, maxAmplitude, 0, 255);
            parent.stroke(parent.color(255 - colorValue, colorValue, 255));
        
            // Draw the wave by connecting points vertically
            parent.line(x, y, x, y - thicknessTop); // Draw points upward for a vertical wave
        }

        // bottom left pyramid
        float bottomWaveAmplitudeScale = 0.1f; // Adjust this to make the bottom waves less reactive 

        for (float y = this.height; y >= this.height - waveLength; y -= 0.1) {
            int index = (int) PApplet.map(this.height - y, 0, this.height, 0, this.fft.avgSize() - 1);
            float amplitude = this.fft.getAvg(index) * this.waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = PApplet.sin(((this.height - y) * waveFrequency - this.frameCount * 0.2f)) * amplitude * maxAmplitude; 
            x += width/4.1f;

            int colorValue = (int) PApplet.map(amplitude, 0, maxAmplitude, 0, 255);
            parent.stroke(parent.color(255 - colorValue, colorValue, 255));

            parent.line(x, y, x, y +thicknessBottom);
        }

        //bottom right pyramid
        for (float y = this.height; y >= this.height - waveLength; y -= 0.1) {
            int index = (int) PApplet.map(this.height - y, 0, this.height, 0, this.fft.avgSize() - 1);
            float amplitude = this.fft.getAvg(index) * this.waveHeight * bottomWaveAmplitudeScale; // Apply the scale here
            float x = PApplet.sin(((this.height - y) * waveFrequency - this.frameCount * 0.2f)) * amplitude * maxAmplitude; 
            x += width/1.323f;

            int colorValue = (int) PApplet.map(amplitude, 0, maxAmplitude, 0, 255);
            parent.stroke(parent.color(255 - colorValue, colorValue, 255));

            parent.line(x, y, x, y + thicknessBottom);
        }
    }

    public void setStartDrawingShapes(boolean startDrawingShapes) {
        this.startDrawingShapes = startDrawingShapes;
    }

    public void setExtremeColour(boolean extremeColour) {
        this.extremeColour = extremeColour;
    }
}