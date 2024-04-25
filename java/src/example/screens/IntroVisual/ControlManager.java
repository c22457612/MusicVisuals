package example.screens.IntroVisual;

import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;

import example.*;

public class ControlManager {
    private MyVisual mv; // Reference to the parent PApplet
    private ArrayList<PVector> smallCubePositions;
    private boolean[] modes;
    private int currentModeIndex;
    private boolean cubeClicked = false;
    boolean displayDiamond;

    public ControlManager(MyVisual _mv, ArrayList<PVector> smallCubePositions, boolean[] modes,
            int currentModeIndex, boolean displayDiamond) {
        this.mv = _mv;
        this.smallCubePositions = smallCubePositions;
        this.modes = modes;
        this.currentModeIndex = currentModeIndex;
        this.displayDiamond = displayDiamond;
    }

    public void mouseClicked() {
        for (int i = 0; i < this.smallCubePositions.size(); i++) {
            PVector cubePos = this.smallCubePositions.get(i);
            float screenX = mv.screenX(cubePos.x, cubePos.y, cubePos.z);
            float screenY = mv.screenY(cubePos.x, cubePos.y, cubePos.z);
            float distance = PApplet.dist(mv.mouseX, mv.mouseY, screenX, screenY);

            if (distance < 20) {
                PApplet.println("Clicked on cube at " + cubePos);
                if (i == 0) { // Assuming index 0 is the left cube
                    this.displayDiamond = false;
                } else if (i == 1) { // Assuming index 1 is the right cube
                    this.displayDiamond = true;
                }
                break;
            }
        }
    }
}
