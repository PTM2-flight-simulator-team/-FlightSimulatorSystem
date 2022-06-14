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

public class CodeHandler {

    public static List<String> handle(Map<String,Object> param, HttpExchange he) throws IOException
    {
        // parse request
        List<String> args = new ArrayList<>();
        args.add("code");//args[0] = code
        args.add((String) param.get("plain_name"));//args[1] is the plain name
        args.add((String) param.get("plain_id"));//args[2] is the plain id
        Scanner sc = new Scanner(he.getRequestBody());
        StringBuilder code = new StringBuilder();
        while(sc.hasNext())code.append(sc.next());
        JsonObject jsonObject = new JsonParser().parse(code.toString()).getAsJsonObject();
        args.add(JsonsFuncs.codeJsonToString(jsonObject));//args[3] is the code
        return args;

    }
}
