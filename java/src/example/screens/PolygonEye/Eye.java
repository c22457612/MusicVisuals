package example.screens.PolygonEye;

import example.*;
import processing.core.PApplet;

// Eye Class
public class Eye {

    float cx;
    float cy;
    float eyeSocketWidth;
    float eyeBallWidth;

    float opacity;

    // Amplitude
    float minAmp = 0;
    float maxAmp = 0.4f;
    float ampPer = 0;

    // Eye
    float arcRadius;
    float closedR = 500;
    float oppenedR = eyeSocketWidth / 1.87f;
    float refreshRate = 0;
    private MyVisual mv;

    int extraX = 0;
    int extraY = 0;

    int t = 0;

    // Constructor
    public Eye(MyVisual mv, float cx, float cy, float closedR, float oppenedR, float eyeSocketWidth, float eyeBallWidth,
            float opacity) {
        super();
        this.mv = mv;
        this.cx = cx;
        this.cy = cy;
        this.closedR = closedR;
        this.oppenedR = oppenedR;
        this.eyeSocketWidth = eyeSocketWidth;
        this.eyeBallWidth = eyeBallWidth;
        this.opacity = opacity;
    }

    // Draw eye
    public void render() {

        updateRadius();
        mv.colorMode(mv.RGB);

        // Draw arcs
        mv.noStroke();
        mv.fill(255, 255, 255, opacity);
        drawEyeSocket(cx - eyeSocketWidth / 2, cx + eyeSocketWidth / 2, arcRadius, -1); // Up
        drawEyeSocket(cx - eyeSocketWidth / 2, cx + eyeSocketWidth / 2, arcRadius, 1); // Down

        ampPer = mv.map(mv.getSmoothedAmplitude(), minAmp, maxAmp, 0, 100);
        float red = mv.map(ampPer, 0, 100, 0, 255) * 1.6f;
        mv.strokeWeight(1);
        mv.stroke(red, 0, 255 - red, opacity);
        mv.fill(red, 0, 255 - red, opacity);
        drawEyeBall(cx - eyeBallWidth / 2, cx + eyeBallWidth / 2, arcRadius, -1); // Up
        drawEyeBall(cx - eyeBallWidth / 2, cx + eyeBallWidth / 2, arcRadius, 1); // Down

        // Draw arcs
        mv.strokeWeight(3);
        mv.stroke(200, 50, 50, opacity);
        mv.noFill();
        drawEyeSocket(cx - eyeSocketWidth / 2, cx + eyeSocketWidth / 2, arcRadius, -1); // Up
        drawEyeSocket(cx - eyeSocketWidth / 2, cx + eyeSocketWidth / 2, arcRadius, 1); // Down

    }

    // Update radius
    private void updateRadius() {

        // arcRadius = closedR + oppenedR - mv.map(mv.getSmoothedAmplitude(), minAmp,
        // maxAmp, oppenedR, closedR);

        // if (refreshRate == 0) {
        float amp = mv.getSmoothedAmplitude() * 0.9f;
        if (amp > maxAmp) {
            amp = maxAmp;
        }
        ampPer = mv.map(amp, minAmp, maxAmp, 0, 100);

        if (ampPer < 10) {
            ampPer = mv.map(ampPer, 0, 10, 100, 0);
            arcRadius = mv.map(ampPer, 0, 100, closedR * 0.1f, closedR);

        } else if (ampPer < 20) {
            ampPer = mv.map(ampPer, 10, 20, 100, 0);
            arcRadius = mv.map(ampPer, 0, 100, closedR * 0.05f, closedR * 0.1f);

        } else if (ampPer < 40) {
            ampPer = mv.map(ampPer, 20, 40, 100, 0);
            arcRadius = mv.map(ampPer, 0, 100, closedR * 0.025f, closedR * 0.05f);

        } else {
            ampPer = mv.map(ampPer, 40, 100, 100, 0);
            arcRadius = mv.map(ampPer, 0, 100, oppenedR, closedR * 0.025f);

        }

        // }

        refreshRate++;
        refreshRate %= 3;
    }

    // Eye socket
    private void drawEyeSocket(float a, float b, float r, int direction) {

        // Begin shape
        mv.beginShape();

        // Calculate vertical shift
        float vertShift = Math.abs(cy - (float) (cy + Math.sqrt(Math.pow(r, 2) - Math.pow(a - cx, 2))));
        float arcCy = cy - direction * vertShift;

        for (int x = (int) a; x <= b; x++) {
            // Y of arc
            float y = (float) (arcCy + direction * Math.sqrt(Math.pow(r, 2) - Math.pow(x - cx, 2)));
            mv.vertex(x, y, opacity == 255 ? 10 : -10);

        }
        // End shape
        mv.endShape();

    }

    // Eye ball
    private void drawEyeBall(float a, float b, float r, int direction) {

        // Begin shape
        mv.beginShape();

        float myMouseX = mv.map(mv.mouseX, 0, mv.width, -mv.width / 2, mv.width / 2);
        float myMouseY = mv.map(mv.mouseY, 0, mv.height, -mv.height / 2, mv.height / 2);

        extraX = (int) mv.map(myMouseX, 0, mv.width, 0, 100);
        extraY = (int) (mv.map(myMouseY, 0, mv.height, 0, 100));

        // Calculate vertical shift
        float vertShift = Math
                .abs(cy - (float) (cy + Math.sqrt(Math.pow(r, 2) - Math.pow(cx - eyeSocketWidth / 2 - cx, 2))));
        float arcCy1 = cy - direction * vertShift;
        float arcCy2 = cy - -direction * vertShift;

        for (int x = (int) (a + extraX); x <= b + extraX; x++) {
            // Y of arc
            float y1 = (float) (arcCy1 + direction * Math.sqrt(Math.pow(r, 2) - Math.pow(x - cx, 2)));
            // Y of arc
            float y2 = (float) (arcCy2 + -direction * Math.sqrt(Math.pow(r, 2) - Math.pow(x - cx, 2)));

            // Y of eye ball
            float circY = cy + extraY
                    + direction * (float) Math.sqrt(Math.pow(eyeBallWidth / 2, 2) - Math.pow(x - (cx + extraX), 2));

            if (direction == 1) {
                if (circY < y2) {
                    mv.vertex(x, y2, opacity == 255 ? 10 : -10);
                } else if (circY < y1) {
                    mv.vertex(x, circY, opacity == 255 ? 10 : -10);
                } else {
                    mv.vertex(x, y1, opacity == 255 ? 10 : -10);
                }
            }

            else {
                if (circY > y2) {
                    mv.vertex(x, y2, opacity == 255 ? 10 : -10);
                } else if (circY > y1) {
                    mv.vertex(x, circY, opacity == 255 ? 10 : -10);
                } else {
                    mv.vertex(x, y1, opacity == 255 ? 10 : -10);
                }
            }

        }
        // End shape
        mv.endShape();

    }

}