package Controller;
import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
            if(planeVar != null)
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
        Controller.getPlaneDataByPid(pid).Print();
        List<PlaneVar> planeData = Controller.getPlaneDataByPid(pid).getAllVars();//add exception if not find;
        System.out.println(planeData);
        System.out.println("size: " + planeData.size());
        for (int i = 0; i<planeData.size(); i++){
            if(planeData.get(i)!= null){
                json.addProperty(planeData.get(i).getNickName(), planeData.get(i).getValue());
                System.out.println("key: " + planeData.get(i).getNickName() + " value: " + planeData.get(i).getValue());
            }
        }
        System.out.println(json.toString());
        return json;
    }

    public static String getTimeSeries(String pid, int index){
        List<List<String>> list = Controller.getTimeSeries(pid,index);
        return new Gson().toJson(Controller.getTimeSeries(pid,index));
    }

    public static String getAnalytics(){
        FindIterable<Document> documentsList = Controller.getAnalytics();
        List<Document> docList = new ArrayList<>();
        if(documentsList == null)
            return "document list is null";
        documentsList.forEach((d)->{
//            String tmp = "";
            if(d != null) {
                docList.add(d);
            }
            else
                return;
        });
        Document retDoc = new Document();
        retDoc.append("analyticList", docList);
        return retDoc.toJson();
    }
}
