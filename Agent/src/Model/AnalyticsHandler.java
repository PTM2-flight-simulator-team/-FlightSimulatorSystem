package Model;

import CommonClasses.PlaneData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class AnalyticsHandler {
    private HashMap<String,String> analytics;
    private ArrayList<ArrayList<String>> timeSeries;
    private boolean firstStart = true;
    private PlaneData LastPlainData = null;
    private static double Nautical_Mile = 0;
    private boolean firstPlaneData = true;

    public AnalyticsHandler(){
        analytics = new HashMap<>();
        timeSeries = new ArrayList<ArrayList<String>>();

        ArrayList<String> headers = new ArrayList<String>();
        AddHeaders(headers);

        timeSeries.add(headers);
    }

    private void AddHeaders(ArrayList<String> headers) {
        headers.add("Aileron");
        headers.add("Elevator");
        headers.add("Rudder");
        headers.add("Longitude");
        headers.add("Latitude");
        headers.add("AirSpeed_kt");
        headers.add("VertSpeed");
        headers.add("Throttle_0");
        headers.add("Throttle_1");
        headers.add("Altitude");
        headers.add("PitchDeg");
        headers.add("RollDeg");
        headers.add("Heading");
        headers.add("TurnCoordinator");
        headers.add("Time");
    }

    public void AddPlainDataToArrayList(PlaneData plainData){
        // lat2/lon2 is the new plaindata and lat1/lon1 is old plaingdata
//        if(firstPlaneData == true)
//            firstStart = false;
        if(LastPlainData != null){
//            System.out.println(LastPlainData.getLatitude());
//            System.out.println(plainData.getLatitude());
            double Nautical_Mile_Result = getDistanceFromLatLonInKm(Double.parseDouble(LastPlainData.getLatitude()), Double.parseDouble(LastPlainData.getLongitude()),Double.parseDouble(plainData.getLatitude()), Double.parseDouble(plainData.getLongitude()));
            Nautical_Mile += Nautical_Mile_Result;
        }
        LastPlainData = plainData;
        if(firstStart == true)
        {
            firstStart = false;
            setFrom(plainData.getLongitude(), plainData.getLatitude());
        }

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = currentTime.format(timeFormatter);
        ArrayList<String> tsElement = plainData.PlainDataToList();
        tsElement.add(time);
        timeSeries.add(tsElement);


        timeSeries.add(plainData.PlainDataToList());

        setTo(plainData.getLongitude(), plainData.getLatitude());
    }
    public ArrayList<ArrayList<String>> GetFlight(){
        return timeSeries;
    }
    public void InsertAnalytics(PlaneData plainData){
        String FGanalytics = "altitude "+ plainData.getAltitude() + " speed " + plainData.getAirSpeed_kt(); // add all the data you want to compare
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

    public void setFrom(String longitude,String latitude){
        analytics.put("StartLongitude",longitude);
        analytics.put("StartLatitude",latitude);
    }

    public void setTo(String longitude,String latitude){
        if(analytics.containsKey("EndLongitude") && analytics.containsKey("EndLatitude"))
        {
            analytics.replace("EndLongitude",longitude);
            analytics.replace("EndLatitude",latitude);
            return;
        }
        else {
            analytics.put("EndLongitude", longitude);
            analytics.put("EndLatitude", latitude);
        }
    }

    public void setStartTime(String time){
        analytics.put("StartTime",time);
    }

    public void setEndTime(String time){
        analytics.put("EndTime",time);
    }

    public String getFinalAnalytics(){
        StringBuilder analyticsString = new StringBuilder();
        analyticsString.append("StartLongitude:").append(analytics.get("StartLongitude")).append(" StartLatitude:").append(analytics.get("StartLatitude"));
        analyticsString.append(" EndLongitude:").append(analytics.get("EndLongitude")).append(" EndLatitude:").append(analytics.get("EndLatitude"));
        analyticsString.append(" startTime:").append(analytics.get("StartTime")).append(" endTime:").append(analytics.get("EndTime"));
        analyticsString.append(" maxAlt:").append(analytics.get("altitude")).append(" maxSpeed:").append(analytics.get("speed"));
        analyticsString.append(" Nautical_Mile:").append(Nautical_Mile);
        System.out.println("Nautical_Mile is: " + Nautical_Mile);
        return analyticsString.toString();
    }

    public double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c * 0.54; // Distance in Nautical_Mile
        return d;
    }

    public double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}
