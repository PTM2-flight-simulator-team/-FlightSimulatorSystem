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
import java.time.LocalDate;
import java.time.Month;
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
        for (int i = 0; i<planeData.size(); i++){
            if(planeData.get(i)!= null){
                json.addProperty(planeData.get(i).getNickName(), planeData.get(i).getValue());
            }
        }
        return json;
    }

    public static String getTimeSeries(String pid, int index){ return new Gson().toJson(Controller.getTimeSeries(pid,index));}

    public static String getAnalytics(){
        FindIterable<Document> documentsList = Controller.getAnalytics();
        List<Document> docList = new ArrayList<>();
        Map<String, PlaneData> map = Controller.getActiveData();
        Month month = LocalDate.now().getMonth();
        if(documentsList == null && !Controller.getActiveData().keySet().isEmpty()){
                  for(String key: map.keySet()){
                      HashMap<String, String> tmp = new HashMap<>();
                      for(PlaneVar var: map.get(key).getAllVars()){
                          tmp.put(var.getNickName(), var.getValue());
                      }
                      Controller.model.DB.saveNewPlaneAnalytics(map.get(key).getID(), map.get(key).getPlaneName()
                              , month, 0.0, true, tmp);
            }
        } else if(documentsList == null && Controller.getActiveData().keySet().isEmpty())
            return "there not planes";
        HashSet<String> idsSet = new HashSet<>();
        documentsList = Controller.getAnalytics();
        documentsList.forEach((d)->{
            d.remove("createdMonth");
            if(d != null) {
                if(Controller.getActiveData().containsKey(d.get("_id"))){
                    String id = (String) d.get("_id");
                    idsSet.add(id);
                    PlaneData planeData = Controller.getPlaneDataByPid(id);
                    HashMap<String,String> data = new HashMap<>();
                    data.put("ID", planeData.getID());
                    data.put("PlaneName", planeData.getPlaneName());
                    for(PlaneVar var: planeData.getAllVars()){
                        data.put(var.getNickName(), var.getValue());
                    }
                    d.replace("planeData", data);
                    d.replace("active", "true");
                }
                docList.add(d);
            }
            else
                return;
        });
        HashMap<String,Double> metrics = new HashMap<>();
        metrics.put(Month.JANUARY.toString(),0.0);
        metrics.put(Month.FEBRUARY.toString(),0.0);
        metrics.put(Month.MARCH.toString(),0.0);
        metrics.put(Month.APRIL.toString(),0.0);
        metrics.put(Month.MAY.toString(),0.0);
        metrics.put(Month.JUNE.toString(),0.0);
        metrics.put(Month.JULY.toString(),0.0);
        metrics.put(Month.AUGUST.toString(),0.0);
        metrics.put(Month.SEPTEMBER.toString(),0.0);
        metrics.put(Month.OCTOBER.toString(),0.0);
        metrics.put(Month.NOVEMBER.toString(),0.0);
        metrics.put(Month.DECEMBER.toString(),0.0);
        for(String key: map.keySet()){
            if(!Controller.model.DB.doesPlaneExists(map.get(key).getID())){
                HashMap<String, String> tmp = new HashMap<>();
                for(PlaneVar var: map.get(key).getAllVars()){
                    tmp.put(var.getNickName(), var.getValue());
                }
                Document d = new Document().append("_id",map.get(key).getID()).append("name", map.get(key).getPlaneName()).append("miles",metrics)
                        .append("active",true).append("planeData" ,tmp).append("createdMonth", month);
                d.remove("createdMonth");
                docList.add(d);
            }
        }
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
            String last = String.valueOf(numOfTimeSeries-1);
            ret += last;
            return ret;
        }

    }

}
