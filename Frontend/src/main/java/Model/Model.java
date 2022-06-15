package Model;

import Model.dataHolder.JoystickData;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import Model.dataHolder.MyResponse;
import Model.dataHolder.TeleoperationsData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Model extends Observable implements Observer {
    private List<Double> joyStickData = new ArrayList<>();
    MyHttpHandler myHttpHandler;

    public Model(){
        myHttpHandler = new MyHttpHandler("127.0.0.1","9000");
        myHttpHandler.addObserver(this);
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
        if (o.getClass().equals(MyResponse.class)){
            setChanged();
            notifyObservers(arg);
        }
        setJoyStickData(120,120);
        setChanged();
        notifyObservers(joyStickData);
    }

    //networking related code
    //GET
    public void SendGetPlaneData(String PlaneID){
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncGet("/GET/PlaneData?plane_id="+ PlaneID);
        cf.thenApply((response) -> myHttpHandler.HandleGetPlaneData(response));
    }
    public void SendGetAnalyticData(){
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncGet("/GET/Analytics");
        cf.thenApply((response) -> myHttpHandler.HandleGetAnalytics(response));
    }
    public void SendGetAnalyticData(String PlaneID){
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncGet("/GET/TS?plane_id="+ PlaneID);
        cf.thenApply((response) -> myHttpHandler.HandleGetTS(response));
    }

//    public void SendGetAllPlanes(){
//        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncGet("/GET/Planes");
//        cf.thenApply((response) -> myHttpHandler.HandleGotAllPlanes(response));
//    }

    //POST
    public void SendPostCode(String PlaneID,TeleoperationsData data){
//        json = json.replaceAll("\\\\", "");
//        TeleoperationsData d2 = new Gson().fromJson(json,TeleoperationsData.class);
//        System.out.println("test");
        String json = new Gson().toJson(data);
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncPost("/POST/Code?plane_id="+ PlaneID,json);
        cf.thenApply((response) -> myHttpHandler.HandlePost(response));
    }

    public void SendPostJoystick(String PlaneID, JoystickData data){
        String json = new Gson().toJson(data);
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncPost("/POST/Code?plane_id="+ PlaneID,json);
        cf.thenApply((response) -> myHttpHandler.HandlePost(response));
    }

}
