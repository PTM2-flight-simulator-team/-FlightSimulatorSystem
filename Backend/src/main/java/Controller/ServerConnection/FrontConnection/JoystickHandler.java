package Controller.ServerConnection.FrontConnection;

import Controller.JsonsFuncs;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.conversions.Bson;
import org.json.HTTP;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class JoystickHandler extends Observable implements HttpHandler{
    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, Object> param = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        System.out.println(query);
        MyNetworkStatic.parseQuery(query, param);//parse query parameters into map
        List<String> args = new ArrayList<>();
        args.add("joystick");//args[0] = joystick
        args.add((String) param.get("plane_id"));//args[1] is the plane id
        Scanner sc = new Scanner(he.getRequestBody());
        StringBuilder joystick = new StringBuilder();
        while(sc.hasNext())joystick.append(sc.next());
        JsonObject jsonObject = new JsonParser().parse(joystick.toString()).getAsJsonObject();
        args.add(JsonsFuncs.JoystickJsonToAgentCommands(jsonObject).replaceAll("\"", ""));//args[2] is the joystick commands
        System.out.println(JsonsFuncs.JoystickJsonToAgentCommands(jsonObject));
        setChanged();
        notifyObservers(args);//sending up list of args
    }
}

