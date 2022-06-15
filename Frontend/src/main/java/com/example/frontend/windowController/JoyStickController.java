package com.example.frontend.windowController;

import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneData;
import com.example.frontend.JoyStickViewModel;
import Model.Model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class JoyStickController implements Initializable, Observer {

    @FXML
    private Canvas joyStick;
    @FXML
    private Slider rudder;
    @FXML
    private Slider throttle;
    boolean mousePushed;
    boolean mouseDisabled;
    JoyStickViewModel vm;
    DoubleProperty aileron, elevators;
    Circle circle;

    double jx, jy;
    double mx, my;
    double prevX, prevY;

    public JoyStickController() {
        circle = new Circle();
        circle.setRadius(90);
        prevX = circle.getCenterX();
        prevY = circle.getCenterY();
        jx = 0;
        jy = 0;
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
    }

    public void initViewModel(Model m) {
        this.vm = new JoyStickViewModel(m);
        vm.addObserver(this);
        vm.throttle.bindBidirectional(throttle.valueProperty());
        vm.rudder.bindBidirectional(rudder.valueProperty());
        vm.aileron.bindBidirectional(aileron);
        vm.elevators.bindBidirectional(elevators);
    }

    public void printJoyStick() {
        GraphicsContext gc = joyStick.getGraphicsContext2D();
        // Center canvas
        mx = joyStick.getWidth() / 2;
        my = joyStick.getHeight() / 2;
        gc.clearRect(0, 0, joyStick.getWidth(), joyStick.getHeight());
        gc.strokeOval(mx - circle.getRadius(), my - circle.getRadius(), circle.getRadius() * 2, circle.getRadius() * 2);
        gc.strokeOval(jx - 35, jy - 35, 70, 70);
        aileron.set((jx - mx) / mx);
        elevators.set((my - jy) / my);
    }

    public void disableJoyStick() {
        mouseDisabled = true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jx = joyStick.getWidth() / 2;
        jy = joyStick.getHeight() / 2;
        printJoyStick();
    }

    //.............joystick buttons.............//
    @FXML
    public void mouseDown(MouseEvent me) {
        if (!mouseDisabled) {
            if (!mousePushed) {
                mousePushed = true;
            }
        }
    }

    @FXML
    public void mouseUp(MouseEvent me) {
        if (!mouseDisabled) {
            if (mousePushed) {
                mousePushed = false;
                jx = mx;
                jy = my;
                printJoyStick();
            }
        }
    }

    private boolean isInCircle(double x, double y) {

        return (Math.pow((x - (circle.getCenterX())-100), 2) + Math.pow((y - (circle.getCenterX())-100), 2)) <= Math.pow(circle.getRadius() - 35, 2);
    }

    @FXML
    public void mouseMove(MouseEvent me) {

        if (mousePushed) {
            jx = me.getX();
            jy = me.getY();

            if (!(isInCircle(jx, jy))) {
                jx = prevX;
                jy = prevY;
            } else {
                System.out.println("in circle");
                prevX = jx;
                prevY = jy;
            }
            //threshold
//            if (me.getX() > 175) {
//                jx = 175;
//            }
//            if (me.getX() < 25) {
//                jx = 25;
//            }
//            if (me.getY() > 175) {
//                jy = 175;
//            }
//            if (me.getY() < 25) {
//                jy = 25;
//            }
            double normalizedX = 2 * ((jx - 25) / (175 - 25)) - 1;
            double normalizedY = -1 * (2 * ((jy - 25) / (175 - 25)) - 1); // invert y axis

            System.out.println("x: " + normalizedX + " y: " + normalizedY);
            printJoyStick();
        }
    }

    public void setValues(double jx, double jy) {
        this.jx = jx;
        this.jy = jy;
        printJoyStick();
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<PlaneData> data = (MyResponse<PlaneData>)arg;
        setValues(aileron.doubleValue(), elevators.doubleValue());
    }
}