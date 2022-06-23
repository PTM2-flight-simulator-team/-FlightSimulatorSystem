package Controller.ServerConnection.FrontConnection;

import Controller.JsonsFuncs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class GetPlaneDataHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> param = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        MyNetworkStatic.parseQuery(query, param);//parse query parameters into map
        JsonObject planeData = JsonsFuncs.getPlaneData((String) param.get("plane_id"));
        String response = new Gson().toJson(planeData).toString();
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
