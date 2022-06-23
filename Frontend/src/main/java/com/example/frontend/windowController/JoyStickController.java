package com.example.frontend.windowController;

import Model.dataHolder.JoystickData;
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
import javafx.scene.shape.Rectangle;

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

    double normalizedX, normalizedY;
    String planeID;

    public JoyStickController() {
        circle = new Circle();
        circle.setRadius(95);
        prevX = circle.getCenterX();
        prevY = circle.getCenterY();
        jx = 0;
        jy = 0;
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
    }

    /**
     * > The view model is initialized with a model, and the view model's properties are bound to the view's properties
     */
    public void initViewModel(Model m) {
        this.vm = new JoyStickViewModel(m);
        vm.addObserver(this);
        vm.throttle.bindBidirectional(throttle.valueProperty());
        vm.rudder.bindBidirectional(rudder.valueProperty());
        vm.aileron.bindBidirectional(aileron);
        vm.elevators.bindBidirectional(elevators);
    }


    /**
     * The function prints the joystick on the canvas, and sets the aileron and elevator values to the joystick's position
     */
    public void printJoyStick() {
        GraphicsContext gc = joyStick.getGraphicsContext2D();
        // Center canvas
        mx = joyStick.getWidth() / 2;
        my = joyStick.getHeight() / 2;
        gc.clearRect(0, 0, joyStick.getWidth(), joyStick.getHeight());
        gc.strokeOval(mx - circle.getRadius(), my - circle.getRadius(), circle.getRadius() * 2, circle.getRadius() * 2);
        gc.fillOval(jx - 35, jy - 35, 70, 70);
        aileron.set((jx - mx) / mx);
        elevators.set((my - jy) / my);
    }

    public void disableJoyStick() {
        mouseDisabled = true;
        rudder.setDisable(true);
        throttle.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jx = joyStick.getWidth() / 2;
        jy = joyStick.getHeight() / 2;
        printJoyStick();
    }

    //.............joystick buttons.............//
    @FXML
    public void sliderVer(MouseEvent me) {
        if (!mouseDisabled) {
            throttle.setValue(throttle.getValue());
            sendJoystick(normalizedX, normalizedY);
        }
    }

    @FXML
    public void sliderHor(MouseEvent me) {
        if (!mouseDisabled) {
            rudder.setValue(rudder.getValue());
            sendJoystick(normalizedX, normalizedY);
        }
    }

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

    @FXML
    public void mouseMove(MouseEvent me) {

        if (mousePushed) {
            jx = me.getX();
            jy = me.getY();
        }
        if (jx > 160) {
            jx = 160;
        }
        if (jx < 40) {
            jx = 40;
        }
        if (jy > 160) {
            jy = 160;
        }
        if (jy < 40) {
            jy = 40;
        }

        normalizedX = 2 * ((jx - 40) / (160 - 40)) - 1;
        normalizedY = -1 * (2 * ((jy - 40) / (160 - 40)) - 1); // invert y axis

        sendJoystick(normalizedX, normalizedY);
        printJoyStick();
    }

    private void sendJoystick(double normalizedX, double normalizedY) {
        JoystickData data = new JoystickData();
        data.aileron = String.valueOf(normalizedX);
        data.elevator = String.valueOf(normalizedY);
        data.throttle = String.valueOf(throttle.getValue());
        data.rudder = String.valueOf(rudder.getValue());
        vm.sendJoystickData(planeID, data);
    }

    public void setPlaneID(String pd) {
        this.planeID = pd;
    }

    public void setValues(double jx, double jy) {
        this.jx = jx;
        this.jy = jy;
        printJoyStick();
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<PlaneData> data = (MyResponse<PlaneData>) arg;
        mapAndSetValues(data.value.aileron, data.value.elevator);
    }

    public void mapAndSetValues(String aileron, String  elevator){
        setValues(getMapedJoystickXYminus1to1(aileron), getMapedJoystickXYminus1to1inverted(elevator));

    }
    private double getMapedJoystickXYminus1to1(String val) {
        double d = Double.parseDouble(val);
        double nd = d + 1;
        double percentage = nd / 2 * 100;
        double returnVal = (percentage * (160 - 40) / 100) + 40;
        return returnVal;
    }

    private double getMapedJoystickXYminus1to1inverted(String val) {
        double d = Double.parseDouble(val);
        double nd = d + 1;
        double percentage = 100 - (nd / 2 * 100);
        double returnVal = (percentage * (160 - 40) / 100) + 40;
        return returnVal;
    }

}