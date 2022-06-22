package com.example.frontend;

import Model.Model;
import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneData;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Observable;
import java.util.Observer;

public class ClocksViewModel extends Observable implements Observer {
    Model m;

    public DoubleProperty compassDegree,speed,verticalSpeed;

    public ClocksViewModel(Model m){
        this.m = m;
        m.addObserver(this);
        compassDegree = new SimpleDoubleProperty();
        speed = new SimpleDoubleProperty();
        verticalSpeed = new SimpleDoubleProperty();
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<PlaneData> data = (MyResponse<PlaneData>)arg;
        this.compassDegree.set(Double.parseDouble(data.value.heading));
        this.speed.set(Double.parseDouble(data.value.airSpeed_kt));
        this.verticalSpeed.set(Double.parseDouble(data.value.vertSpeed));
        setChanged();
        notifyObservers(arg);
    }
}
