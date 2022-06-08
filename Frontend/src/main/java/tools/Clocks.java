package tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;

public class Clocks {
    public Canvas clock;
    public Line line;


    public Clocks(Line line) {
        this.line = line;
        line.setStartX(0);
        line.setStartY(37);
        line.setEndX(0);
        line.setEndY(-14.000030517578125);
    }

    public void rotateAltClock() {
        line.setEndX(51);
        line.setEndY(36);
    }

    public void printClocks() {
        GraphicsContext gc = clock.getGraphicsContext2D();
        double mx = clock.getWidth() / 2;
        double my = clock.getHeight() / 2;
        gc.strokeOval(mx - 50, my - 50, 200, 200);
    }
}
