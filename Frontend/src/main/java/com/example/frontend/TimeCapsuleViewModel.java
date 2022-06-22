package com.example.frontend;

import Model.Model;

import java.util.Observable;
import java.util.Observer;

public class TimeCapsuleViewModel extends Observable implements Observer {
    Model m;
    public  TimeCapsuleViewModel(Model m)
    {
        this.m = m;
        m.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void sendGetAnalytic(){
        m.SendGetAnalyticData();
    }

    public void sendGetFlightIDS(String pid){
        m.SendGetTSIndexesByPlaneID(pid);
    }

    public void sendGetTS(String planeID, String flightNum){
        m.SendGetTSData(planeID,flightNum);
    }
}
