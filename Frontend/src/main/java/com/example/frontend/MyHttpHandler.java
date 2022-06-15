package com.example.frontend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import java.util.HashMap;
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
                .uri(URI.create("http://"+serverIP+":"+serverPort +"/"+ path))
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
                .uri(URI.create("http://"+serverIP+":"+serverPort +"/"+ path))
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
    public Object HandleGetPlaneData(){
        return null;
    }

    public Object HandleGetAnalytics(){
        return null;
    }

    private void Print(Object data) {
        System.out.println(data);

    }
}
