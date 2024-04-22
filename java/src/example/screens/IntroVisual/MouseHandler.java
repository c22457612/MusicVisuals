package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;

public class MouseHandler {
    PApplet parent;
    ArrayList<PVector> cubePositions;
    boolean[] modes;
    boolean displayDiamond;
    
    public MouseHandler(PApplet parent, ArrayList<PVector> cubePositions, boolean[] modes) {
        this.parent = parent;
        this.cubePositions = cubePositions;
        this.modes = modes;
    }

    public void mouseClicked() {
        for (int i = 0; i < cubePositions.size(); i++) {
            PVector cubePos = cubePositions.get(i);
            float screenX = parent.screenX(cubePos.x, cubePos.y, cubePos.z);
            float screenY = parent.screenY(cubePos.x, cubePos.y, cubePos.z);
            float distance = PApplet.dist(parent.mouseX, parent.mouseY, screenX, screenY);
            
            if (distance < 20) {
                PApplet.println("Clicked on cube at " + cubePos);
                if (i == 0) {  // Assuming index 0 is the left cube
                    displayDiamond = false;
                } else if (i == 1) {  // Assuming index 1 is the right cube
                    displayDiamond = true;
                }
                break;
            }
        }
    }

    void cycleModes() {
        int currentModeIndex = 0;
        for (int i = 0; i < modes.length; i++) {
            if (modes[i]) {
                currentModeIndex = i;
                break;
            }
        }

        modes[currentModeIndex] = false;
        currentModeIndex = (currentModeIndex + 1) % modes.length;
        modes[currentModeIndex] = true;
    }

    public boolean isDisplayDiamond() {
        return displayDiamond;
    }

    public void setDisplayDiamond(boolean displayDiamond) {
        this.displayDiamond = displayDiamond;
    }
}
