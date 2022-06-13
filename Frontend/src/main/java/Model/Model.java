package Model;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Model extends Observable implements Observer {
    private List<Double> joyStickData = new ArrayList<>();
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
        //update(this,null);
    }

    public void setJoyStickData(double d1, double d2) {
        this.joyStickData.add(d1);
        this.joyStickData.add(d2);
    }


    @Override
    public void update(Observable o, Object arg) {
        setJoyStickData(120,120);
        setChanged();
        notifyObservers(joyStickData);
    }
}
