package Controller;
import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.json.JSONObject;

import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import java.io.IOException;
import java.util.*;


public class JsonsFuncs {

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

    public static JsonObject getPlaneData(String pid) throws IOException {
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

    public static String getTimeSeries(String pid, int index){ return new Gson().toJson(Controller.getTimeSeries(pid,index));}

    public static String getAnalytics(){
        FindIterable<Document> documentsList = Controller.getAnalytics();
        List<Document> docList = new ArrayList<>();
        if(documentsList == null)
            return "document list is null";
        documentsList.forEach((d)->{
            d.remove("createdMonth");
            if(d != null) {
                if((boolean)d.get("active") == true){
                    String id = (String) d.get("_id");
                    PlaneData planeData = Controller.getPlaneDataByPid(id);
                    HashMap<String,String> data = new HashMap<>();
                    data.put("ID", planeData.getID());
                    data.put("PlaneName", planeData.getPlaneName());
                    for(PlaneVar var: planeData.getAllVars()){
                        data.put(var.getNickName(), var.getValue());
                    }
                    d.replace("planeData", data);
                }
                docList.add(d);
            }
            else
                return;
        });
        Document retDoc = new Document();
        retDoc.append("analyticList", docList);
        return retDoc.toJson();
    }

    public static String fleetSize(){
        return new Gson().toJson(Controller.getFleetSize());
    }

    public static String getPlaneFlightsIndexes(String pid){
        int numOfTimeSeries = Controller.getNumOfTimeSeries(pid);
        if(numOfTimeSeries == 0)
            return "none";
        else{
            String ret = "0-";
            String last = String.valueOf(numOfTimeSeries);
            ret += last;
            return ret;
        }

    }

}
