package Controller.ServerConnection.FrontConnection;

import Controller.JsonsFuncs;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.*;

public class CodeHandler extends Observable implements HttpHandler{

    public  void handle(HttpExchange he) throws IOException
    {
        Map<String, Object> param = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        System.out.println(query);
        MyNetworkStatic.parseQuery(query, param);//parse query parameters into map
        List<String> args = new ArrayList<>();
        args.add("code");//args[0] = code
        args.add((String) param.get("plane_id"));//args[1] is the plain id
        Scanner sc = new Scanner(he.getRequestBody());
        StringBuilder code = new StringBuilder();
        while(sc.hasNext())code.append(sc.nextLine());
        JsonObject jsonObject = new JsonParser().parse(code.toString()).getAsJsonObject();
        args.add(JsonsFuncs.codeJsonToString(jsonObject));//args[2] is the code
        setChanged();
        notifyObservers(args);//sending up list of args
    }
}
