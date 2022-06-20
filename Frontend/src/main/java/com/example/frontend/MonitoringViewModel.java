package com.example.frontend;

import Model.Model;
import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneData;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MonitoringViewModel extends Observable implements Observer {

    public List<List<String>> data;
    Model m;
    int miliseconds = 100;

    public MonitoringViewModel(Model m) {
        this.m = m;
        m.addObserver(this);
        this.data = new ArrayList<>();
        List<String> category = new ArrayList<>();
        category.add("aileron");
        category.add("elevator");
        category.add("rudder");
        category.add("longitude-deg");
        category.add("latitude-deg");
        category.add("airspeed-indicator_indicated-speed-kt");
        category.add("vertical-speed");
        category.add("throttle_0");
        category.add("throttle_1");
        category.add("altitude");
        category.add("pitchDeg");
        category.add("rollDeg");
        category.add("heading");
        category.add("turnCoordinator");
        category.add("Time");
        data.add(category);

        m.startGetPlaneData(miliseconds,"4");
    }
    //{"aileron":"0.000000","elevator":"0.000000","rudder":"0.000000","longitude-deg":"-22.6580185466","latitude-deg":"63.9794995244"
    // ,"airspeed-indicator_indicated-speed-kt":"0.000000","vertical-speed":"-0.000000","throttle_0":"0.000000","throttle_1":"0.000000"
    // ,"altitude":"10.391782","pitchDeg":"0.000000","rollDeg":"40.000000","heading":"12.891746","turnCoordinator":"69.313248"}

    public void buildTimeSeries(PlaneData planeData) {
        this.data.add(planeData.getPlaneDataAsList());
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
        if(arg instanceof PlaneData)
            this.buildTimeSeries((PlaneData)arg);
    }
}
