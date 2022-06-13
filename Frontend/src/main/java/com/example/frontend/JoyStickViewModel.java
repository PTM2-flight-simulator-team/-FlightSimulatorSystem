package com.example.frontend;

import Model.Model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Observable;
import java.util.Observer;

public class JoyStickViewModel extends Observable implements Observer {
    Model m;

    public DoubleProperty throttle,rudder,aileron,elevators;

    public JoyStickViewModel(Model m){
        this.m = m;
        m.addObserver(this);
        this.throttle = new SimpleDoubleProperty();
        this.rudder = new SimpleDoubleProperty();
        this.aileron = new SimpleDoubleProperty();
        this.elevators = new SimpleDoubleProperty();

    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
