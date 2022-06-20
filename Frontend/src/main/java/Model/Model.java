package Model;

import Model.dataHolder.*;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Model extends Observable implements Observer {
    private List<Double> joyStickData = new ArrayList<>();
    MyHttpHandler myHttpHandler;

    public Model(){
        myHttpHandler = new MyHttpHandler("127.0.0.1","9000");
        myHttpHandler.addObserver(this);
        PlaneData planeData = new PlaneData();
        planeData.throttle_0 = "0.0";
        planeData.rudder = "0.0";
        planeData.aileron = "0.0";
        planeData.elevator = "0.0";
        MyResponse<PlaneData> response = new MyResponse<>(planeData, ResonseType.PlaneData);
//        SendGetAnalyticData();
//        HashMap<String,String> code = new HashMap<>();
//        code.put("1", "var throttle = bind \"/controls/engines/current-engine/throttle\"");
//        code.put("2", "var heading = bind \"/instrumentation/heading-indicator/offset-deg\"");
//        code.put("3", "var airspeed = bind \"/instrumentation/airspeed-indicator/indicated-speed-kt\"");
//        TeleoperationsData tele = new TeleoperationsData();
//        tele.code = code;
//        SendPostCode("4", tele);
//        SendGetAnalyticData();
        SendGetTSData("1995", "1");
        new Thread("New Thread") {
            public void run(){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                update(null,response);
            }
        }.start();
    }

    public void setJoyStickData(double d1, double d2) {
        this.joyStickData.add(d1);
        this.joyStickData.add(d2);
    }


    @Override
    public void update(Observable o, Object arg) {
//        if (o.getClass().equals(Model.class)){
            setChanged();
            notifyObservers(arg);
            return;
//        }

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
    public void SendGetTSData(String PlaneID, String flightId){
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncGet("/GET/TS?plane_id="+ PlaneID+ "&flightId=" + flightId);
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
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(data);
        System.out.println(json);
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncPost("/POST/Code?plane_id="+ PlaneID,json);
        cf.thenApply((response) -> myHttpHandler.HandlePost(response));
    }

    public void SendPostJoystick(String PlaneID, JoystickData data){
        String json = new Gson().toJson(data);
        System.out.println(json);
        CompletableFuture<HttpResponse<String>> cf = myHttpHandler.SendAsyncPost("/POST/Joystick?plane_id="+ PlaneID ,json);
        cf.thenApply((response) -> myHttpHandler.HandlePost(response));
    }

}
