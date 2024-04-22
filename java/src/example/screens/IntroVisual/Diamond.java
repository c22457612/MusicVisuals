package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.analysis.FFT;

public class Diamond {
    PApplet parent; // Reference to the PApplet object for drawing
    FFT fft; // FFT object for audio analysis
    float currentRotationY; // Rotation angle
    boolean extremeColour; // Flag for color mode
    boolean fillActivated; // Flag to determine whether to fill the diamond
    float transparentColour; // Transparency level
    boolean startDrawingShapes;
    boolean xRotateDiamond;
    boolean yRotateDiamond;
    float rotationSpeed=0;
    boolean playIntro;

    // Constructor
    public Diamond(PApplet parent, FFT fft, float currentRotationY, boolean extremeColour, boolean fillActivated, float transparentColour,boolean startDrawingShapes,boolean xRotateDiamond,boolean yRotateDiamond,boolean playIntro) {
        this.parent = parent;
        this.fft = fft;
        this.currentRotationY = currentRotationY;
        this.extremeColour = extremeColour;
        this.fillActivated = fillActivated;
        this.transparentColour = transparentColour;
        this.startDrawingShapes=startDrawingShapes;
        this.xRotateDiamond=xRotateDiamond;
        this.yRotateDiamond=yRotateDiamond;
        this.playIntro=playIntro;
    }


    void drawDiamond() {
        //find out what part of the program we are in
        this.setDrawingShapes(startDrawingShapes);
        this.setExtremeColour(extremeColour);

        this.parent.pushMatrix();
        this.parent.translate(parent.width / 2, parent.height / 2, -400);

        if (rotationSpeed < 0.05) {
            rotationSpeed += 0.0001;
        }

        currentRotationY += rotationSpeed;
        
        //PApplet.println(this.startDrawingShapes);
        if (this.startDrawingShapes){ //in visualizer 
            if (this.xRotateDiamond){
                if (this.extremeColour){
                    this.parent.rotateX(currentRotationY*2);
                }else{
                    parent.rotateX(currentRotationY);
                } 
            }
            if (this.yRotateDiamond){
                if (this.extremeColour){
                    parent.rotateY(this.currentRotationY*2);
                }else{
                    parent.rotateY(this.currentRotationY);
                } 
            }
        }else if (!this.startDrawingShapes){
            this.parent.rotateX(currentRotationY);
            this.parent.rotateY(currentRotationY);
        }
        
        
        
        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }
        
        parent.stroke((parent.frameCount % 255), 255, 255);
        parent.strokeWeight(2);

        if (!this.startDrawingShapes &&this.playIntro){
            transparentColour -= 0.6; 
            this.setTransparentColour(transparentColour);
            parent.fill(340, 100, 100, transparentColour);
        }else if(this.startDrawingShapes){
            if (this.extremeColour){
                if (this.fillActivated){
                    float hue = PApplet.map(totalAmplitude, 0, 2000, 0, 360);  // custom hue for diamond
                    hue=hue%360;
                    parent.fill(hue,100,100);
                }else{
                    parent.noFill();
                }
                
            }else if (this.startDrawingShapes){
                if (this.fillActivated){
                    float hue = PApplet.map(totalAmplitude, 0, 2000, 300, 360);  // custom hue for diamond
                    parent.fill(hue,100,100);
                    parent.stroke(255);//white outline
                }
                
            }
            
        }else{
            parent.fill(340, 100, 100, transparentColour);
        }
        
        float size = 180;
        float mid = size / 2;
        
        // Draw the diamond
        parent.beginShape(PConstants.TRIANGLES);
        parent.vertex(0, -size, 0);// top pyramid 1   
        parent.vertex(-mid, 0, -mid);
        parent.vertex(mid, 0, -mid);

        parent.vertex(0, -size, 0); //top pyramid 2
        parent.vertex(mid, 0, -mid);
        parent.vertex(mid, 0, mid);

        parent.vertex(0, -size, 0); // top pyramid 3
        parent.vertex(mid, 0, mid);
        parent.vertex(-mid, 0, mid);

        parent.vertex(0, -size, 0); //top pyramid 4
        parent.vertex(-mid, 0, mid);
        parent.vertex(-mid, 0, -mid);

        parent.vertex(0, size, 0); //bottom pyramid 1
        parent.vertex(-mid, 0, -mid);
        parent.vertex(mid, 0, -mid);

        parent.vertex(0, size, 0); //bottom pyramid 2
        parent.vertex(mid, 0, -mid);
        parent.vertex(mid, 0, mid);

        parent.vertex(0, size, 0);//bottom pyramid 3
        parent.vertex(mid, 0, mid);
        parent.vertex(-mid, 0, mid);

        parent.vertex(0, size, 0);//bottom pyramid 4
        parent.vertex(-mid, 0, mid);
        parent.vertex(-mid, 0, -mid);

        this.parent.endShape(PConstants.CLOSE);
        
        this.parent.popMatrix();
    }

    public void setDrawingShapes(boolean startDrawingShapes){
        this.startDrawingShapes=startDrawingShapes;
        //PApplet.println(this.startDrawingShapes);
    }

    // Inside your Diamond class
    public void setTransparentColour(float transparentColour) {
        this.transparentColour = transparentColour;
    }

    public void setExtremeColour(boolean extremeColour){
        this.extremeColour=extremeColour;
    }

    public void setxRotateDiamond(boolean xRotateDiamond){
        this.xRotateDiamond=xRotateDiamond;
    }

    public void setyRotateDiamond(boolean yRotateDiamond){
        this.yRotateDiamond=yRotateDiamond;
    }

    public void setPlayIntro(boolean playIntro){
        this.playIntro=playIntro;
    }

    public void setFillActivated(boolean fillActivated){
        this.fillActivated=fillActivated;
    }
    
}
