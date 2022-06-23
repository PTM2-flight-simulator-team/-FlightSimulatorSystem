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

    /**
     * It sends a request to the server to get the analytic data.
     */
    public void sendGetAnalytic(){
        m.SendGetAnalyticData();
    }

    /**
     * Send a request to the server to get all the flight IDs for a given plane ID.
     * */
    public void sendGetFlightIDS(String pid){
        m.SendGetTSIndexesByPlaneID(pid);
    }

    /**
     * This function sends a request to the server to get the timestamp of the last update of the plane with the given
     * planeID and flightNum
     *
     * @param planeID The plane's ID
     * @param flightNum The flight number of the plane.
     */
    public void sendGetTS(String planeID, String flightNum){
        m.SendGetTSData(planeID,flightNum);
    }
}
