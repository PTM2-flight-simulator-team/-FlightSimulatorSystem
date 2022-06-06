package tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class JoyStick {

    public Canvas joyStick;

    public JoyStick(Canvas js){
        joyStick = js;
    }

    public void printJoyStick() {
        GraphicsContext gc = joyStick.getGraphicsContext2D();
        double mx = joyStick.getWidth() / 2;
        double my = joyStick.getHeight() / 2;
        gc.strokeOval(mx - 50, my - 50, 100, 100);
    }

}
