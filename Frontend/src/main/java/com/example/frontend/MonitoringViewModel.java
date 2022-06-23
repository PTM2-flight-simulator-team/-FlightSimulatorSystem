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
    int miliseconds = 1000;

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

//        m.startGetPlaneData(miliseconds,"4");
    }
    //{"aileron":"0.000000","elevator":"0.000000","rudder":"0.000000","longitude-deg":"-22.6580185466","latitude-deg":"63.9794995244"
    // ,"airspeed-indicator_indicated-speed-kt":"0.000000","vertical-speed":"-0.000000","throttle_0":"0.000000","throttle_1":"0.000000"
    // ,"altitude":"10.391782","pitchDeg":"0.000000","rollDeg":"40.000000","heading":"12.891746","turnCoordinator":"69.313248"}

    public void startService(String planeID) {
        m.startGetPlaneData(miliseconds,planeID);
    }
    public void buildTimeSeries(PlaneData planeData) {
        this.data.add(planeData.getPlaneDataAsList());
    }

    public void setMiliseconds(){
        this.miliseconds = miliseconds;
    }
    @Override
    public void update(Observable o, Object arg) {
        MyResponse<PlaneData> pd = (MyResponse<PlaneData>) arg;
        if(pd.value instanceof PlaneData){
            this.buildTimeSeries(pd.value);

        }
        setChanged();
        notifyObservers(arg);
    }

    public List<List<String>> getData() {
        return data;
    }

    public void GetAnal(){
        m.SendGetAnalyticData();}
}
