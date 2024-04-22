package example.screens.IntroVisual;
/*package example;

import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

public class KeyControl {
    private PApplet parent;
    private AudioPlayer player;
    private AudioPlayer song;
    private boolean[] modes;
    private int currentModeIndex;
    private boolean playIntro;
    private boolean startDrawingShapes;
    private boolean startFading;
    private boolean spinning;
    private Cube bigCube;
    private Cube[] cubes;
    private Diamond diamond;
    private Pyramids pyramids;
    private Sphere sphere;
    private long fadeStartTime;
    private int fadeDuration;
    private FFT fft;
    private boolean hasStartedPlaying;
    boolean isFirstSoundtrackFinished;
    float circleOpacity;
    long circleFadeStartTime;
    AudioPlayer sound1; // Player for the interactive sound
    AudioPlayer sound2;
    AudioPlayer sound3;
    AudioPlayer sound4;
    AudioPlayer sound5;
    

    public KeyControl(PApplet parent, AudioPlayer player, AudioPlayer song, boolean[] modes, int currentModeIndex,Cube bigCube, Cube[] cubes, Diamond diamond, Pyramids pyramids, Sphere sphere, FFT fft,boolean spinning,boolean hasStartedPlaying,boolean isFirstSoundtrackFinished,long circleFadeStartTime,AudioPlayer sound1,AudioPlayer sound2,AudioPlayer sound3,AudioPlayer sound4,AudioPlayer sound5) {
        this.parent = parent;
        this.player = player;
        this.song = song;
        this.modes = modes;
        this.currentModeIndex = currentModeIndex;
        this.bigCube = bigCube;
        this.cubes = cubes;
        this.diamond = diamond;
        this.pyramids = pyramids;
        this.sphere = sphere;
        this.fft = fft;
        this.spinning=spinning;
        this.hasStartedPlaying=hasStartedPlaying;
        this.sound1=sound1;
        this.sound2=sound2;
        this.sound3=sound3;
        this.sound4=sound4;
        this.sound5=sound5;
    }




    public void keyPressed() {
        if (parent.key == ' ') {
            this.spinning = this.spinning; // Toggle spinning state
            if(this.spinning){
                this.player.play();
                this.playIntro=true;
                this.diamond.setPlayIntro(this.playIntro);
                this.pyramids.setSpinning(this.spinning);
                this.pyramids.setPlayIntro(this.playIntro); // this will allow pyramids to work in intro
                this.hasStartedPlaying = true;
                this.isFirstSoundtrackFinished = false; // Reset this flag when the first soundtrack starts
                this.circleOpacity = 0; // Reset opacity to allow fade-in effect
                // Set the start time for the circle to fade in (e.g., after 10 seconds)
                this.circleFadeStartTime = parent.millis() + 10000; // 10,000 milliseconds from now
            } else {
                this.player.pause();
            }
        } else if (parent.key == '1') {
            if (!this.startDrawingShapes){
                parent.playSound(this.sound1);
            }
        } else if (parent.key == '2') {
            if (!this.startDrawingShapes){
                playSound(this.sound2);
            }
        }else if(parent.key=='3'){
            if (!this.startDrawingShapes){
                playSound(this.sound3);
            }
        }else if(key=='4'){
            if (!this.startDrawingShapes){
                playSound(this.sound4);
            }
        }else if(parent.key==parent.ENTER){
            this.playIntro=true;
            this.isFirstSoundtrackFinished=true;
            parent.drawRainbowWave();
            //rainbowWave.drawRainbowWave();
            playSound(this.sound5);
            this.startFading = true;
            this.fadeStartTime = parent.millis(); //will eventually trigger startDrawingShapes boolean
            
        }else if(parent.key=='5'){
            playSound(song);
            this.startFading=false;
            this.startDrawingShapes=true; //manually start
        }

        if (this.startDrawingShapes){ // when we are using audio visualization for shapes, we can pause and resume
            if (parent.key == 'p' || parent.key == 'P') {
                if (this.song.isPlaying()) {
                    this.song.pause();
                } else {
                    this.song.play();
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
                //bigCube.setFillActivated(fillActivated);
                //diamond.setFillActivated(fillActivated);
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
            if (keyCode=='m'|| keyCode=='M'){ // middle or main objects on screen control
                fillActivated=!fillActivated;
                bigCube.setFillActivated(fillActivated);
                diamond.setFillActivated(fillActivated);
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
                pyramids.setExtremeColour(extremeColour);
                sphere.setExtremeColour(extremeColour);
                soundWave.setExtremeColour(extremeColour);
            }
        }


        // You can add more conditions for additional sounds here
    }
}

*/