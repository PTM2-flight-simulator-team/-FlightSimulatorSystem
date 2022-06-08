package Model;

import Network.NetworkManager;

import java.util.HashMap;

public class AnalyticsHandler {
    private HashMap<String,String> analytics;

    public AnalyticsHandler(){
        analytics = new HashMap<>();
        setFrom("Tel-Aviv");
        setTo("New-York");
    }

    public void compareAnalytics(String FGanalytics){
        String[] data = FGanalytics.split(" ");
        int size = data.length;
        for(int i=0;i<size;i+=2){
            if(!analytics.containsKey(data[i])){
                analytics.put(data[i],data[i+1]);
            }
            else{
                double currentMax = Double.parseDouble(analytics.get(data[i]));
                double value = Double.parseDouble(data[i+1]);
                if(currentMax < value){
                    analytics.put(data[i],String.valueOf(value));
                }
            }
        }
    }

    public void setFrom(String from){
        analytics.put("From",from);
    }

    public void setTo(String to){
        analytics.put("To",to);
    }

    public void setStartTime(String time){
        analytics.put("StartTime",time);
    }

    public void setEndTime(String time){
        analytics.put("EndTime",time);
    }

    public String getFinalAnalytics(){
        StringBuilder analyticsString = new StringBuilder();
        analyticsString.append("From:").append(analytics.get("From")).append(" To:").append(analytics.get("To"));
        analyticsString.append(" startTime:").append(analytics.get("StartTime")).append(" endTime:").append(analytics.get("EndTime"));
        analyticsString.append(" maxAlt:").append(analytics.get("altitude")).append(" maxSpeed:").append(analytics.get("speed"));
//        analytics.forEach((key,value)->{
//            analyticsString.append(key).append(",").append(value);
//        });
        return analyticsString.toString();
    }
}
