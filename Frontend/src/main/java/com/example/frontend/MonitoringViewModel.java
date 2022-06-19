package com.example.frontend;

import Model.Model;
import Model.dataHolder.MyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MonitoringViewModel extends Observable implements Observer {

    public List<List<String>> data;
    Model m;

    public MonitoringViewModel(Model m) {
        this.m = m;
        m.addObserver(this);
        this.data = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {

        MyResponse<List<List<String>>> data = (MyResponse<List<List<String>>>)arg;
        this.data = data.value;
        setChanged();
        notifyObservers(arg);
    }
}
