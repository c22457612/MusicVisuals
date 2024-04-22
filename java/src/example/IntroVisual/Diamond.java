package example.IntroVisual;

import processing.core.PApplet;
import processing.core.PConstants;
import ddf.minim.analysis.FFT;

public class Diamond {
    IntroVisualScreen parent; // Reference to the PApplet object for drawing
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
    public Diamond(IntroVisualScreen introVisualScreen, FFT fft, float currentRotationY, boolean extremeColour, boolean fillActivated, float transparentColour,boolean startDrawingShapes,boolean xRotateDiamond,boolean yRotateDiamond,boolean playIntro) {
        this.parent = introVisualScreen;
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

        this.parent.mv.pushMatrix();
        this.parent.mv.translate(parent.mv.width / 2, parent.mv.height / 2, -400);

        if (rotationSpeed < 0.05) {
            rotationSpeed += 0.0001;
        }

        currentRotationY += rotationSpeed;
        
        //PApplet.println(this.startDrawingShapes);
        if (this.startDrawingShapes){ //in visualizer 
            if (this.xRotateDiamond){
                if (this.extremeColour){
                    this.parent.mv.rotateX(currentRotationY*2);
                }else{
                    parent.mv.rotateX(currentRotationY);
                } 
            }
            if (this.yRotateDiamond){
                if (this.extremeColour){
                    parent.mv.rotateY(this.currentRotationY*2);
                }else{
                    parent.mv.rotateY(this.currentRotationY);
                } 
            }
        }else if (!this.startDrawingShapes){
            this.parent.mv.rotateX(currentRotationY);
            this.parent.mv.rotateY(currentRotationY);
        }
        
        
        
        float totalAmplitude = 0;

        for (int i = 0; i < fft.specSize(); i++) {
            totalAmplitude += fft.getBand(i);
        }
        
        parent.mv.stroke((parent.mv.frameCount % 255), 255, 255);
        parent.mv.strokeWeight(2);

        if (!this.startDrawingShapes &&this.playIntro){
            transparentColour -= 0.6; 
            this.setTransparentColour(transparentColour);
            parent.mv.fill(340, 100, 100, transparentColour);
        }else if(this.startDrawingShapes){
            if (this.extremeColour){
                if (this.fillActivated){
                    float hue = PApplet.map(totalAmplitude, 0, 2000, 0, 360);  // custom hue for diamond
                    hue=hue%360;
                    parent.mv.fill(hue,100,100);
                }else{
                    parent.mv.noFill();
                }
                
            }else if (this.startDrawingShapes){
                if (this.fillActivated){
                    float hue = PApplet.map(totalAmplitude, 0, 2000, 300, 360);  // custom hue for diamond
                    parent.mv.fill(hue,100,100);
                    parent.mv.stroke(255);//white outline
                }
                
            }
            
        }else{
            parent.mv.fill(340, 100, 100, transparentColour);
        }
        
        float size = 180;
        float mid = size / 2;
        
        // Draw the diamond
        parent.mv.beginShape(PConstants.TRIANGLES);
        parent.mv.vertex(0, -size, 0);// top pyramid 1   
        parent.mv.vertex(-mid, 0, -mid);
        parent.mv.vertex(mid, 0, -mid);

        parent.mv.vertex(0, -size, 0); //top pyramid 2
        parent.mv.vertex(mid, 0, -mid);
        parent.mv.vertex(mid, 0, mid);

        parent.mv.vertex(0, -size, 0); // top pyramid 3
        parent.mv.vertex(mid, 0, mid);
        parent.mv.vertex(-mid, 0, mid);

        parent.mv.vertex(0, -size, 0); //top pyramid 4
        parent.mv.vertex(-mid, 0, mid);
        parent.mv.vertex(-mid, 0, -mid);

        parent.mv.vertex(0, size, 0); //bottom pyramid 1
        parent.mv.vertex(-mid, 0, -mid);
        parent.mv.vertex(mid, 0, -mid);

        parent.mv.vertex(0, size, 0); //bottom pyramid 2
        parent.mv.vertex(mid, 0, -mid);
        parent.mv.vertex(mid, 0, mid);

        parent.mv.vertex(0, size, 0);//bottom pyramid 3
        parent.mv.vertex(mid, 0, mid);
        parent.mv.vertex(-mid, 0, mid);

        parent.mv.vertex(0, size, 0);//bottom pyramid 4
        parent.mv.vertex(-mid, 0, mid);
        parent.mv.vertex(-mid, 0, -mid);

        this.parent.mv.endShape(PConstants.CLOSE);
        
        this.parent.mv.popMatrix();
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
