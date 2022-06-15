package Controller;
import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JsonsFuncs {
    public static JSONObject plainDataToJson(PlaneData planeData){
        JSONObject json = new JSONObject();
        for(PlaneVar planeVar: planeData.getAllVars()){
            json.put(planeVar.getNickName(),planeVar.getValue());
        }
        return json;
    }

    public static String JoystickJsonToAgentCommands(JsonObject json){
        List<String> agentCommands = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(String key: json.keySet()){
            String command = "set " + key + " " + json.get(key);
            agentCommands.add(command);
        }
        for(int i = 0; i < agentCommands.size(); i++){
            sb.append(agentCommands.get(i));
            if(i != (agentCommands.size()-1)){
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String codeJsonToString(JsonObject json){
        JsonObject code = json.getAsJsonObject("code");//getting the code json
        StringBuilder sb = new StringBuilder();
        for(String key: code.keySet()){//reading the lines and parse them to string
            sb.append(code.get(key).getAsString() + "\n");
        }
        return sb.toString();//return the code as string
    }

    public static JsonObject getPlainData(String pid) throws IOException {
        JsonObject json = new JsonObject();
        List<PlaneVar> planeData = Controller.planeDataMap.get(pid).getAllVars();//add exception if not find
        for (PlaneVar var: planeData){
            json.addProperty(var.getNickName(), var.getValue());
        }
        return json;
    }

    public static String getTimeSeries(String pid){
        FindIterable<Document> documentsList = Controller.getTimeSeries(pid);
        Document ts = null;
        if(documentsList.first() != null)
            ts = documentsList.first();
        return ts.toString().replaceAll("Document", "");
    }

    public static String getAnalytics(){
        FindIterable<Document> documentsList = Controller.getAnalytics();
        StringBuilder sb = new StringBuilder();
        if(documentsList == null)
            return "document list is null";
        documentsList.forEach((d)->{
            String tmp = "";
            if(d != null)
                tmp = d.toString();
            else
                return;
            String document = tmp;
            sb.append(document);
        });

        return sb.toString().replaceAll("Document", "");
    }
}
