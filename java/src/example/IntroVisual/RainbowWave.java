package example.IntroVisual;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.analysis.FFT;

public class RainbowWave {
    private PApplet parent;
    private FFT fft;
    private float baseRadius;
    private float maxWaveAmplitude;
    private float smoothingFactor;
    private float[] prevAmplitudes;
    private float rainbowWaveRotationAngle;
    private int specSize;
    float maxFFTAmplitude;
    boolean[] modes;

    public RainbowWave(PApplet parent, FFT fft, float baseRadius, float maxWaveAmplitude, float smoothingFactor,float maxFFTAmplitude,boolean [] modes,float [] prevAmplitudes) {
        this.parent = parent;
        this.fft = fft;
        this.baseRadius = baseRadius;
        this.maxWaveAmplitude = maxWaveAmplitude;
        this.smoothingFactor = smoothingFactor;
        this.rainbowWaveRotationAngle = 0.0f;
        this.specSize = fft.specSize();
        this.prevAmplitudes = prevAmplitudes;
        this.maxFFTAmplitude=maxFFTAmplitude;
        this.modes=modes;
    }

    public void drawRainbowWave() {
        parent.colorMode(PConstants.HSB, 360, 100, 100);
        // Recalculate angleStep based on the current fft specSize
        float angleStep = PConstants.TWO_PI / this.fft.specSize();
    
        // Calculate maximum FFT amplitude for normalization
        for (int i = 0; i < fft.specSize(); i++) {
            this.maxFFTAmplitude = PApplet.max(this.maxFFTAmplitude, this.fft.getBand(i));
        }
    
        // Ensure there's no divide by zero issue
        if (this.maxFFTAmplitude == 0) {
            this.maxFFTAmplitude = 1;
        }
    
        // Increment the rotation angle, adjust the speed as necessary
        this.rainbowWaveRotationAngle += 0.01;
    
        // Start matrix transformation
        parent.pushMatrix();
        // Translate to the center of the screen
        parent.translate(parent.width/2, parent.height/2);
        // Rotate the whole wave by the current rotation angle
        /*if (this.modes[0]){
            parent.rotate(rainbowWaveRotationAngle);
        }*/
        parent.noFill();
        parent.strokeWeight(3);
        parent.beginShape();
        
        for (int i = 0; i < this.fft.specSize(); i++) {
            float amplitude = this.fft.getBand(i);
            // Smooth the transition of amplitude values
            this.prevAmplitudes[i] = PApplet.lerp(this.prevAmplitudes[i], amplitude, this.smoothingFactor);
            // Normalize and scale the amplitude
            float normalizedAmplitude = PApplet.map(this.prevAmplitudes[i], 0, this.maxFFTAmplitude, 0, this.maxWaveAmplitude);
    
            // Calculate wave points
            float x = (this.baseRadius + normalizedAmplitude) * PApplet.cos(i * angleStep);
            float y = (this.baseRadius + normalizedAmplitude) * PApplet.sin(i * angleStep);
    
            // Dynamic coloring based on position
            int colorValue = (int) (128 + 128 * PApplet.sin(i * 0.1f + parent.frameCount * 0.02f));
            parent.stroke(parent.color(255 - colorValue, colorValue, 255));
    
            parent.vertex(x, y);
        }
    
        parent.endShape(PConstants.CLOSE);
        parent.popMatrix(); // Restore matrix state
    }

    public void setPrevAmplitudes(float[] prevAmplitudes) {
        if (prevAmplitudes.length == this.prevAmplitudes.length) {
            System.arraycopy(prevAmplitudes, 0, this.prevAmplitudes, 0, prevAmplitudes.length);
        }
    }

}
