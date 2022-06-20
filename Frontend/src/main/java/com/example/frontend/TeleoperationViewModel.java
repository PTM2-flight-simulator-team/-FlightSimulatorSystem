package com.example.frontend;

import Model.Model;
import Model.dataHolder.TeleoperationsData;

public class TeleoperationViewModel {

     Model m;

    public TeleoperationViewModel(Model m){
        this.m = m;
    }

    public void sendCode(String planeID,TeleoperationsData data){
        m.SendPostCode(planeID,data);
    }
}
