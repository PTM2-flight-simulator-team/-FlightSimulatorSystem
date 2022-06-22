package com.example.frontend;

import Model.Model;
import Model.dataHolder.AnalyticsData;
import Model.dataHolder.JoystickData;
import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneData;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Observable;
import java.util.Observer;

public class JoyStickViewModel extends Observable implements Observer {
    Model m;

    public DoubleProperty throttle,rudder,aileron,elevators;

    static int counter = 0;
    public JoyStickViewModel(Model m){
        this.m = m;
        this.m.addObserver(this);
        this.throttle = new SimpleDoubleProperty();
        this.rudder = new SimpleDoubleProperty();
        this.aileron = new SimpleDoubleProperty();
        this.elevators = new SimpleDoubleProperty();
//        counter++;
//        System.out.println(counter);
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<PlaneData> data = (MyResponse<PlaneData>)arg;
        if(data.value instanceof PlaneData)
        {
            this.throttle.set(Double.parseDouble(data.value.throttle_0));
            this.rudder.set(Double.parseDouble(data.value.rudder));
            this.aileron.set(Double.parseDouble(data.value.aileron));
            this.elevators.set(Double.parseDouble(data.value.elevator));
            setChanged();
            notifyObservers(arg);

        }
        else
            return;

    }

    public void sendJoystickData(String planeID,JoystickData data) {
        m.SendPostJoystick(planeID,data);
    }

    protected void finalize()
    {
//resources to be close

    }
}
