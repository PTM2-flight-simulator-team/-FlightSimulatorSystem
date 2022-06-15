package Controller.ServerConnection.FrontConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class GetHandler extends Observable implements HttpHandler {

    public GetHandler() {

    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = he.getRequestURI();
        String query = requestedUri.getRawQuery();
        MyNetworkStatic.parseQuery(query, parameters);
        // send response
        //  setChanged();
        //  notifyObservers();
        String response = "t";
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
