package Controller;
import CommonClasses.PlainData;
import CommonClasses.PlainVar;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonsFuncs {
    public static JSONObject plainDataToJson(PlainData plainData){
        JSONObject json = new JSONObject();
        for(PlainVar plainVar: plainData.getAllVars()){
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
        StringBuilder sb = new StringBuilder();
        sb.append(json.get("code"));
        return sb.toString();
    }
}
