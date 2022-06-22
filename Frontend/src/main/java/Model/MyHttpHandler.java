package Model;

import Model.dataHolder.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MyHttpHandler extends Observable {

    public String serverIP;
    public String serverPort;
    public MyHttpHandler(String serverIP,String serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    private final HttpClient myHttpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    // HttpClient: An HttpClient can be used to send requests and retrieve their responses. An HttpClient is created through a builder.

    // Duration: A time-based amount of time, such as '5 seconds'.

    // Send async get request and delegates the response up
    public  CompletableFuture<HttpResponse<String>> SendAsyncGet(String path) {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://"+serverIP+":"+serverPort + path))
                .header("accept","application/json")
                .build();

        CompletableFuture<HttpResponse<String>> asyncResponse = null;

        // sendAsync(): Sends the given request asynchronously using this client with the given response body handler.
        //Equivalent to: sendAsync(request, responseBodyHandler, null).
        asyncResponse = myHttpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return  asyncResponse;
//        asyncResponse.thenApply(response -> HandleResponse(response));

    }

    public CompletableFuture<HttpResponse<String>> SendAsyncPost(String path,String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(json))
                .uri(URI.create("http://"+serverIP+":"+serverPort + path))
                .header("accept","application/json")
                .build();

        CompletableFuture<HttpResponse<String>> asyncResponse = null;

        // sendAsync(): Sends the given request asynchronously using this client with the given response body handler.
        //Equivalent to: sendAsync(request, responseBodyHandler, null).
        asyncResponse = myHttpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return asyncResponse;
//        asyncResponse.thenApply(response -> HandleResponse(response));

    }


    private Object HandleGeneralResponse(HttpResponse<String> response) {
        System.out.println("Handleing response");
        Print(response.body());
        Print(response.statusCode());
        setChanged();
        notifyObservers();
        return response;
    }
    public Object HandleGetPlaneData(HttpResponse<String> response){
//        System.out.println(response.body());
        PlaneData data = new Gson().fromJson(response.body(),PlaneData.class);
        MyResponse<PlaneData> res = new MyResponse<>(data, ResonseType.PlaneData);
        setChanged();
        notifyObservers(res);
        return null;
    }

    public Object HandleGetAnalytics(HttpResponse<String> response){
        AnalyticsData data = new Gson().fromJson(response.body(), AnalyticsData.class);
        MyResponse<AnalyticsData> res = new MyResponse<>(data, ResonseType.Analytic);
        setChanged();
        notifyObservers(res);
        return null;
    }
    public Object HandleGetTS(HttpResponse<String> response){
        System.out.println(response.body());
        List<List<String>> ts = new Gson().fromJson(response.body(),new TypeToken<List<List<String>>>(){}.getType());
        MyResponse<List<List<String>>> res = new MyResponse<>(ts, ResonseType.TS);
        setChanged();
        notifyObservers(res);

        return null;
    }

    public Object HandleGetTSIndexes(HttpResponse<String> response){
        System.out.println(response.body());
        String tsIndexes = response.body();
        MyResponse<String> res = new MyResponse<>(tsIndexes, ResonseType.TS);
        setChanged();
        notifyObservers(res);

        return null;
    }

    public Object HandleGetFleetSize(HttpResponse<String> response){
        HashMap<Integer,Integer> fleetSize = new Gson().fromJson(response.body(),new TypeToken<HashMap<Integer,Integer>>(){}.getType());
        MyResponse<HashMap<Integer,Integer>> res = new MyResponse<>(fleetSize, ResonseType.Analytic);
        setChanged();
        notifyObservers(res);
        return null;
    }
//    public Object HandleGetAllPlanes(HttpResponse<String> response){
//        List<PlaneData> data = new Gson().fromJson(response.body(),new TypeToken<List<PlaneData>>(){}.getType());
//        MyResponse<List<PlaneData>> res = new MyResponse<>(data,ResonseType.AllPlanes);
//        return null;
//    }
    public Object HandlePost(HttpResponse<String> response){
        return null;
    }

    private void Print(Object data) {
        System.out.println(data);

    }
}
