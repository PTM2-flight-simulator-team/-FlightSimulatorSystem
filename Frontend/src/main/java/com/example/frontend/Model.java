package com.example.frontend;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements Observer {
    public Model(){
        new Thread("New Thread") {
            public void run(){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                update(null,null);
            }
        }.start();
        update(this,null);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(200);
    }
}
