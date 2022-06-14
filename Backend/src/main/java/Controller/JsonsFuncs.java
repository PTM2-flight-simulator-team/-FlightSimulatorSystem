package Controller;
import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JsonsFuncs {
    public static JSONObject plainDataToJson(PlaneData plainData){
        JSONObject json = new JSONObject();
        for(PlaneVar plainVar: plainData.getAllVars()){
            json.put(plainVar.getNickName(),plainVar.getValue());
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
}
