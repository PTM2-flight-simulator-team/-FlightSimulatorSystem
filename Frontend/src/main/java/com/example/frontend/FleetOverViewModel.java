package com.example.frontend;

import Model.Model;


import java.util.Observable;
import java.util.Observer;

public class FleetOverViewModel extends Observable implements Observer {

    Model model;
    int seconds = 60;

    public FleetOverViewModel(Model model)
    {
        this.model = model;
        this.model.addObserver(this);
//        this.model.startGetAnalyticService(seconds);
        this.model.SendGetAnalyticData();
        this.model.SendGetFleetSizeByMonth();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        setChanged();
        notifyObservers(arg);
    }

}
