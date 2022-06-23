package com.example.frontend;

import Model.Model;
import Model.dataHolder.AnalyticsData;
import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneData;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MonitoringViewModel extends Observable implements Observer {

    public List<List<String>> data;
    Model m;
    int miliseconds = 5000;

    public MonitoringViewModel(Model m) {
        this.m = m;
        m.addObserver(this);
        this.data = new ArrayList<>();
        List<String> category = new ArrayList<>();
        category.add("Aileron");
        category.add("Elevator");
        category.add("Rudder");
        category.add("Longitude");
        category.add("Latitude");
        category.add("AirSpeed_kt");
        category.add("VertSpeed");
        category.add("Throttle_0");
        category.add("Throttle_1");
        category.add("Altitude");
        category.add("PitchDeg");
        category.add("RollDeg");
        category.add("Heading");
        category.add("TurnCoordinator");
        category.add("Time");
        data.add(category);
    }

    public void startService(String planeID) {
        m.startGetPlaneData(miliseconds, planeID);
    }

    public void buildTimeSeries(PlaneData planeData) {
        this.data.add(planeData.getPlaneDataAsList());
    }

    public void setMiliseconds() {
        this.miliseconds = miliseconds;
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<PlaneData> pd = (MyResponse<PlaneData>) arg;
        if (pd.value instanceof PlaneData) {
            this.buildTimeSeries(pd.value);
        }
        setChanged();
        notifyObservers(arg);
    }

    public List<List<String>> getData() {
        return data;
    }

    public void GetAnal() {
        m.SendGetAnalyticData();
    }
}
