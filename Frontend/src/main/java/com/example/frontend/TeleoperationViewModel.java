package com.example.frontend;

import Model.Model;
import Model.dataHolder.TeleoperationsData;

import java.util.Observable;
import java.util.Observer;

public class TeleoperationViewModel extends Observable implements Observer {

     Model m;

    public TeleoperationViewModel(Model m){
        this.m = m;
        this.m.addObserver(this);
    }

    public void sendCode(String planeID,TeleoperationsData data){
        m.SendPostCode(planeID,data);
    }

    public void SendGetPlains() {
        this.m.SendGetAnalyticData();
    }

    public void sendPostShutdown(String planeID){this.m.SendPostShutdown(planeID);};

    public void startService(String pd) {this.m.startGetPlaneData(500,pd);}


    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
