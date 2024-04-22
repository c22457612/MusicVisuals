package example;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class Sphere {
    private PApplet parent;
    private FFT fft;
    private AudioPlayer song;
    private boolean playIntro;
    private boolean startDrawingShapes;
    private boolean[] modes;
    private boolean fillActivated;
    private boolean extremeColour;
    private float angle;
    private float r;
    float movementSpeed;

    // Constructor
    public Sphere(PApplet parent, FFT fft, AudioPlayer song, boolean playIntro, boolean startDrawingShapes, boolean[] modes, boolean fillActivated, boolean extremeColour, float sphereRadius,float movementSpeed) {
        this.parent = parent;
        this.fft = fft;
        this.song = song;
        this.playIntro = playIntro;
        this.startDrawingShapes = startDrawingShapes;
        this.modes = modes;
        this.fillActivated = fillActivated;
        this.extremeColour = extremeColour;
        this.r = r;
        this.angle = 0.0f;
        this.movementSpeed=movementSpeed;
    }

    // Draw the moving sphere
    void drawMovingSphere(float x, float y, float r) {
        parent.pushMatrix(); // Save the current state of transformations
        parent.translate(x, y); // Use the dynamic `sphereY` for y-position
        
        if (!this.startDrawingShapes){
            this.angle += 0.01; // Continuously rotate the sphere
        }else if(this.modes[0]&&this.startDrawingShapes){
            this.angle+=0.001;
        }

        if (this.song.isPlaying() || !this.playIntro) {
            
            parent.rotateX(this.angle);
    
            // Analyze the spectrum into bass, mid, and treble
            float bassAmplitude = 0, trebleAmplitude = 0;
            int bassCount = 0, trebleCount = 0;
            
            for (int i = 0; i < this.fft.specSize(); i++) {
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
            this.movementSpeed = PApplet.map(bassAmplitude, 0, 10, 1, 5);
            this.movementSpeed = PApplet.constrain(this.movementSpeed, 1, 5);
    
            // Setting HSB color mode
            parent.colorMode(PConstants.HSB, 360, 100, 100);
    
            // Optional: Adjust color based on treble amplitude
            float hue = PApplet.map(trebleAmplitude, 0, 2000, 0, 360); // Half range of hue
            float saturation = PApplet.map(bassAmplitude, 0, 10, 20, 100); // Saturation increases with bass
            float brightness = 100; // Always full brightness for visibility
    

            float totalAmplitude = 0;

            for (int i = 0; i < fft.specSize(); i++) { // needs to be added for extreme colours to work
                totalAmplitude += fft.getBand(i);
            }

            // Adjust stroke width dynamically for a pulsing effect
            float strokeWeightValue = PApplet.map(trebleAmplitude, 0, 10, 0.5f, 15);
            if (!this.fillActivated){
                parent.fill(255);
            }else{
                parent.noFill(); 
            }
            
            parent.strokeWeight(strokeWeightValue);
            if (this.modes[0]&& this.startDrawingShapes){
                if (this.extremeColour){
                    hue = PApplet.map(totalAmplitude, 0, 2000, 0, 360);  // Ranges from half colour wheel
                    hue = hue % 360;  // Ensure the hue wraps around correctly
                    parent.stroke(hue,100,100);
                    r=r/5; //widen view for more aggresive effect
                }else if (!extremeColour){
                    hue = PApplet.map(totalAmplitude, 0, 2000, 0, 80);  // Ranges from other half colour wheel
                    hue = hue % 360;  // Ensure the hue wraps around correctly
                    parent.stroke(hue,100,100);
                    //println("not extreme colours");
                }
            }else{
                hue = PApplet.map(totalAmplitude, 0, 2000, 300, 360);  // Ranges from half colour wheel
                hue = hue % 360;  // Ensure the hue wraps around correctly
                parent.stroke(hue,100,100);
            }

                
    
            // Draw the sphere with the constant radius
            parent.sphere(r);
        } else {
            // Default color when music is paused
            parent.colorMode(PConstants.RGB, 255); // Switch back to RGB for consistent color handling
            //fill(255, 0, 100, 100);
            parent.noFill();
            parent.stroke(255);
            parent.strokeWeight(1);
            parent.sphere(r);
        }
    
        parent.popMatrix(); // Restore original state of transformations
    }

    // Getter and setter methods
    public void setPlayIntro(boolean playIntro) {
        this.playIntro = playIntro;
    }

    public void setStartDrawingShapes(boolean startDrawingShapes) {
        this.startDrawingShapes = startDrawingShapes;
    }

    public void setModes(boolean[] modes) {
        this.modes = modes;
    }

    public void setFillActivated(boolean fillActivated) {
        this.fillActivated = fillActivated;
    }

    public void setExtremeColour(boolean extremeColour) {
        this.extremeColour = extremeColour;
    }

    public void setRadius(float r) {
        this.r = r;
    }

    // More getters and setters as needed...

}
