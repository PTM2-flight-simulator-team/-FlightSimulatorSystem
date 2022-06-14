package Controller.ServerConnection.FrontConnection;

import Controller.JsonsFuncs;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class GetAnalyticsHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> param = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        System.out.println(query);
        MyNetworkStatic.parseQuery(query, param);//parse query parameters into map
        String response = JsonsFuncs.getAnalytics();
//        String response = document.substring(8, document.length()-1);
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
