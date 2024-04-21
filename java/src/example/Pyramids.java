package example;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;

public class Pyramids {
    // Pyramid properties
    private PApplet parent; // Reference to the PApplet (to use Processing's drawing functions)
    private float x, y, z; // Position of the pyramid
    private float size; // Size of the pyramid
    private float rotation; // Rotation of the pyramid
    private boolean isVisible; // Visibility of the pyramid
    private FFT fft; // Reference to the FFT object for audio analysis
    private float fillAlpha; // Transparency of the pyramid fill
    float rotationSpeed;
    float pyramidRotation;
    float pyramidXPosTop;
    float pyramidXPosBottom;
    float pyramidMoveSpeed;
    float currentRotationY;
    float transparentColour;
    AudioPlayer song;
    float pyramidFillAlpha;
    float pyramidSize;
    boolean playIntro;
    boolean startDrawingShapes;
    boolean[] modes;
    boolean extremeColour;
    boolean pyramidsVisible;
    boolean spinning;
    boolean fillActivated;


    // Constructor
    public Pyramids(PApplet parent, FFT fft,boolean spinning,float rotationSpeed,float pyramidRotation,float pyramidXPosTop,float pyramidXPosBottom,float pyramidMoveSpeed,boolean pyramidsVisible,float currentRotationY,AudioPlayer song,float pyramidFillAlpha,float pyramidSize,boolean playIntro,boolean startDrawingShapes,boolean[] modes,boolean extremeColour,float transparentColour,boolean fillActivated) {
        this.parent = parent;
        this.fft = fft;
        this.spinning=spinning;
        this.rotationSpeed=rotationSpeed;
        this.pyramidRotation=pyramidRotation;
        this.pyramidXPosTop=pyramidXPosTop;
        this.pyramidXPosBottom=pyramidXPosBottom;
        this.pyramidMoveSpeed=pyramidMoveSpeed;
        this.pyramidsVisible=pyramidsVisible;
        this.currentRotationY=currentRotationY;
        this.song=song;
        this.modes=modes;
        this.transparentColour=transparentColour;
        this.pyramidSize=pyramidSize;
    }

    // Method to draw the pyramid
    public void drawPyramids() {

        if (this.spinning){
            //PApplet.println("in spinning");
            //println(transparentColour);
            if (this.rotationSpeed < 0.05) {
                this.rotationSpeed += 0.0001;
            }
            this.pyramidRotation += this.rotationSpeed; // Keep rotating the pyramids continuously
            if (PApplet.abs(pyramidXPosTop) < parent.width / 3) {
                this.pyramidXPosTop -= this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            if (PApplet.abs(pyramidXPosBottom) < parent.width / 3) {
                this.pyramidXPosBottom += this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            this.currentRotationY += this.rotationSpeed;
            if (this.transparentColour > 0) {
                this.transparentColour -= 0.5;
            }
        }

        if (this.song.isPlaying()){ //rotate
            if (this.rotationSpeed < 0.05) {
                this.rotationSpeed += 0.0001;
            }
            this.pyramidRotation += this.rotationSpeed; // Keep rotating the pyramids continuously
            if (PApplet.abs(pyramidXPosTop) < parent.width / 2) {
                this.pyramidXPosTop -= this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            if (PApplet.abs(pyramidXPosBottom) < parent.width / 2) {
                this.pyramidXPosBottom += this.pyramidMoveSpeed;
            } else {
                this.pyramidsVisible = true;
            }
            this.currentRotationY += this.rotationSpeed;
            if (this.transparentColour > 0) {
                this.transparentColour -= 1;
            }
            else{// else stay
                this.pyramidsVisible=true;
            }
            this.pyramidFillAlpha += 2; // Increase alpha gradually
            this.pyramidFillAlpha = PApplet.constrain(pyramidFillAlpha, 0, 255); // Limit alpha to max 255
            
        }

        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }

        // Top pyramid and its additional bottom pyramid
        parent.pushMatrix();
        parent.translate(parent.width / 2 + this.pyramidXPosTop, parent.height / 2, -200);
        parent.rotateY(pyramidRotation);
    
        // Draw the top pyramid (inverted)
        parent.pushMatrix();
        parent.translate(0, -this.pyramidSize, 0);
        parent.rotateX(PConstants.PI);
        if (this.playIntro &&!this.startDrawingShapes){ //intro
            parent.fill(totalAmplitude, 50, totalAmplitude, this.transparentColour);
            //println(transparentColour);
            PApplet.println("in intro"); //debugging statement
            PApplet.println(totalAmplitude);
        }
        if (this.modes[0] &&this.startDrawingShapes)// colour scheme for mode 0
        {
            PApplet.println("in visualizer");
            if (this.extremeColour){
                float hue = PApplet.map(totalAmplitude, 0, 2000, 40, 180);  // Ranges from all colours aggresively
                hue = hue % 360;  // Ensure the hue wraps around correctly
                parent.fill(hue,100,100);
                //println("extreme colours");
            }else if (!this.extremeColour){
                float hue = PApplet.map(totalAmplitude, 0, 2000, 240, 360);  // Ranges from purple to pink aggresively
                hue = hue % 360;  // Ensure the hue wraps around correctly
                parent.fill(hue,100,100);
                //println("not extreme colours");
            }
            
        }else if(this.modes[1]){
            //println(totalAmplitude); //debugging statement
            if (totalAmplitude>1500){
                parent.fill(totalAmplitude/2, 50, totalAmplitude/4, pyramidFillAlpha);
                parent.stroke(0);
            }else if(totalAmplitude>1800){
                parent.fill(totalAmplitude/6, 50, totalAmplitude/6, pyramidFillAlpha);
                parent.stroke(0);
            }else{ 
                parent.fill(totalAmplitude/3, 50, totalAmplitude/3, pyramidFillAlpha);
                parent.stroke(0);
            }
            
        }

        
        this.drawPyramid(this.pyramidSize);
        parent.popMatrix();
    
        // Draw the additional bottom pyramid (upright) underneath the top pyramid
        if (PApplet.abs(this.pyramidXPosTop) >= parent.width / 3) {
            parent.translate(0, this.pyramidSize, 0);
            this.drawPyramid(this.pyramidSize);
        }
        parent.popMatrix();
    
        // Bottom pyramid and its additional top pyramid
        parent.pushMatrix();
        parent.translate(parent.width / 2 + this.pyramidXPosBottom, parent.height / 2, -200);
        parent.rotateY(this.pyramidRotation);
    
        // Draw the bottom pyramid (upright)
        parent.pushMatrix();
        parent.translate(0, this.pyramidSize, 0);
        this.drawPyramid(this.pyramidSize);
        parent.popMatrix();
    
        // Draw the additional top pyramid (inverted) above the bottom pyramid
        if (PApplet.abs(this.pyramidXPosBottom) >= parent.width / 3) {
            parent.translate(0, -this.pyramidSize, 0);
            parent.rotateX(PConstants.PI);
            this.drawPyramid(this.pyramidSize);
        }
        parent.popMatrix();
    }

    public void drawPyramid(float size) {
        PApplet.println("in draw pyramid method");
        parent.beginShape(PConstants.TRIANGLES);
        //PApplet.println(size);
        if (this.pyramidsVisible) {
            parent.strokeWeight(2); // Outlines visible
        } else {
            parent.strokeWeight(0); // Outlines invisible
        }
        this.parent.vertex(-size / 2, -size / 2, -size / 2);
        this.parent.vertex(size / 2, -size / 2, -size / 2);
        this.parent.vertex(0, size / 2, 0);

        this.parent.vertex(size / 2, -size / 2, -size / 2);
        this.parent.vertex(size / 2, -size / 2, size / 2);
        this.parent.vertex(0, size / 2, 0);

        this.parent.vertex(size / 2, -size / 2, size / 2);
        this.parent.vertex(-size / 2, -size / 2, size / 2);
        this.parent.vertex(0, size / 2, 0);

        this.parent.vertex(-size / 2, -size / 2, size / 2);
        this.parent.vertex(-size / 2, -size / 2, -size / 2);
        this.parent.vertex(0, size / 2, 0);
        this.parent.endShape(PConstants.CLOSE);
    }

    public void setPlayIntro(boolean playIntro){
        this.playIntro=playIntro;
    }

    public void setSpinning(boolean spinning){
        this.spinning=spinning;
    }

    public void setDrawingShapes(boolean startDrawingShapes){
        this.startDrawingShapes=startDrawingShapes;
        //PApplet.println(this.startDrawingShapes);
    }

    public void setPyramidsVisible(boolean pyramidsVisible){
        this.pyramidsVisible=pyramidsVisible;
    }

    public void setExtremeColour(boolean extremeColour){
        this.extremeColour=extremeColour;
    }

    public void setfillActivated(boolean fillActivated){
        this.fillActivated=fillActivated;
    }
}
