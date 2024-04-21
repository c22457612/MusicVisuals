package example.screens.PolygonEye;

import example.*;

// Eye Class
public class Eye extends Drawable {

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

    // Constructor
    public Eye(
            MyVisual mv,
            float cx,
            float cy,
            float closedR,
            float oppenedR,
            float eyeSocketWidth,
            float eyeBallWidth, float opacity) {
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
    public void drawEye() {

        updateRadius();
        mv.colorMode(mv.RGB);
        // Draw arcs
        mv.stroke(255, 255, 255, opacity);
        mv.fill(255, 255, 255, opacity);
        drawEyeSocket(cx - eyeSocketWidth / 2, cx + eyeSocketWidth / 2, arcRadius, -1); // Up
        drawEyeSocket(cx - eyeSocketWidth / 2, cx + eyeSocketWidth / 2, arcRadius, 1); // Down

        ampPer = mv.map(mv.getSmoothedAmplitude(), minAmp, maxAmp, 0, 100);
        float red = mv.map(ampPer, 0, 100, 0, 255);
        mv.stroke(red, 0, 255 - red, opacity);
        mv.fill(red, 0, 255 - red, opacity);
        drawEyeBall(cx - eyeBallWidth / 2, cx + eyeBallWidth / 2, arcRadius, -1); // Up
        drawEyeBall(cx - eyeBallWidth / 2, cx + eyeBallWidth / 2, arcRadius, 1); // Down

    }

    // Update radius
    private void updateRadius() {

        // arcRadius = closedR + oppenedR - mv.map(mv.getSmoothedAmplitude(), minAmp,
        // maxAmp, oppenedR, closedR);

        // if (refreshRate == 0) {
        float amp = mv.getSmoothedAmplitude();
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
            mv.vertex(x, y);

        }
        // End shape
        mv.endShape();

    }

    // Eye ball
    private void drawEyeBall(float a, float b, float r, int direction) {

        // Begin shape
        mv.beginShape();

        // Calculate vertical shift
        float vertShift = Math
                .abs(cy - (float) (cy + Math.sqrt(Math.pow(r, 2) - Math.pow(cx - eyeSocketWidth / 2 - cx, 2))));
        float arcCy = cy - direction * vertShift;

        for (int x = (int) a; x <= b; x++) {
            // Y of arc
            float y = (float) (arcCy + direction * Math.sqrt(Math.pow(r, 2) - Math.pow(x - cx, 2)));

            // Y of eye ball
            float circY = cy + direction * (float) Math.sqrt(Math.pow(eyeBallWidth / 2, 2) - Math.pow(x - cx, 2));

            if (direction == 1) {
                if (circY < y) {
                    mv.vertex(x, circY);
                } else {
                    mv.vertex(x, y);
                }
            }

            else {
                if (circY > y) {
                    mv.vertex(x, circY);
                } else {
                    mv.vertex(x, y);
                }
            }

        }
        // End shape
        mv.endShape();

    }

}
