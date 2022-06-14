package com.example.frontend.windowController;

import com.example.frontend.JoyStickViewModel;
import Model.Model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

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

    double jx, jy;
    double mx, my;

    public JoyStickController() {
        jx = 0;
        jy = 0;
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
    }

    public void initViewModel(Model m){
        this.vm = new JoyStickViewModel(m);
        vm.addObserver(this);
    }

//    void init(JoyStickViewModel vm) {
//        this.vm = vm;
//        vm.addObserver(this);
//        vm.throttle.bind(throttle.valueProperty());
//        vm.rudder.bind(rudder.valueProperty());
//        vm.aileron.bind(aileron);
//        vm.elevators.bind(elevators);
//    }

    public void printJoyStick() {
        GraphicsContext gc = joyStick.getGraphicsContext2D();
        // Center canvas
        mx = joyStick.getWidth() / 2;
        my = joyStick.getHeight() / 2;
        gc.clearRect(0,0,joyStick.getWidth(), joyStick.getHeight());
        gc.strokeOval(jx - 50, jy - 50, 100, 100);
        aileron.set((jx-mx)/mx);
        elevators.set((my-jy)/my);
    }

    public void disableJoyStick(){
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
    public void mouseDown(MouseEvent me){
        if(!mouseDisabled){
            if(!mousePushed){
                mousePushed=true;
            }
        }
    }
    @FXML
    public void mouseUp(MouseEvent me){
        if(!mouseDisabled){
            if(mousePushed){
                mousePushed=false;
                jx=mx;
                jy=my;
                printJoyStick();
            }
        }
    }
    @FXML
    public void mouseMove(MouseEvent me){

        if(mousePushed){
           jx=me.getX();
           jy=me.getY();
           printJoyStick();
        }
    }
    public void setValues(double jx, double jy){
        this.jx =jx;
        this.jy =jy;
        printJoyStick();
    }

    @Override
    public void update(Observable o, Object arg) {
        List<Double> list = (List<Double>)arg;
        setValues(list.get(0).doubleValue(),list.get(1).doubleValue());
    }
}
