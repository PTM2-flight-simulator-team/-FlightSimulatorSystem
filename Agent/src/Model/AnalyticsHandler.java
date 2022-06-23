package Model;

import CommonClasses.PlaneData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * > This class is responsible for handling all analytics related tasks
 */
public class AnalyticsHandler {
    // A hashmap that contains all the analytics data.
    private HashMap<String,String> analytics;
    // An array of arrays that contains all the data of the flight.
    private ArrayList<ArrayList<String>> timeSeries;
    private boolean firstStart = true;
    // Used to calculate the distance between the last location and the current location.
    private PlaneData LastPlaneData = null;
    private static double Nautical_Mile = 0;
    private boolean firstPlaneData = true;

    // This is the constructor of the class.
    public AnalyticsHandler(){
        this.analytics = new HashMap<>();
        this.timeSeries = new ArrayList<ArrayList<String>>();
        ArrayList<String> headers = new ArrayList<String>();
        AddHeaders(headers);
        this.timeSeries.add(headers);
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

    /**
     * This function adds the plain data to the array list, and calculates the distance between the last plane data and the
     * current plane data
     *
     * @param plainData The data that we want to add to the array list.
     */
    public void AddPlainDataToArrayList(PlaneData plainData){
        // This is checking if the last plane data is null, if it is not null, then it will calculate the distance between
        // the last plane data and the current plane data.
        if(LastPlaneData != null){
            double Nautical_Mile_Result = getDistanceFromLatLonInKm(Double.parseDouble(LastPlaneData.getLatitude()), Double.parseDouble(LastPlaneData.getLongitude()),Double.parseDouble(plainData.getLatitude()), Double.parseDouble(plainData.getLongitude()));
            Nautical_Mile += Nautical_Mile_Result;
        }
        // Setting the last plane data to the current plane data.
        LastPlaneData = plainData;
        // This is checking if the first plane data is the first plane data, if it is, then it will set the start location
        // to the current location.
        if(firstStart == true)
        {
            firstStart = false;
            setFrom(plainData.getLongitude(), plainData.getLatitude());
        }
        // This is getting the current time and formatting it to a specific format.
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = currentTime.format(timeFormatter);
        // Converting the plain data to a list of strings.
        ArrayList<String> tsElement = plainData.PlainDataToList();
        // Adding the current time to the array list.
        tsElement.add(time);
        // Adding the current plane data to the array list.
        timeSeries.add(tsElement);
        // Adding the plain data to the array list.
//        timeSeries.add(plainData.PlainDataToList());
        // This function is setting the end location to the current location.
        setTo(plainData.getLongitude(), plainData.getLatitude());
    }

    /**
     * This function returns the timeSeries ArrayList
     *
     * @return The time series of the flight.
     */
    public ArrayList<ArrayList<String>> GetFlight(){
        return timeSeries;
    }

    /**
     * The function takes a PlaneData object and splits the string into an array of strings. Then, it iterates over the
     * array and checks if the key exists in the hashmap. If it does, it checks if the value is greater than the current
     * value. If it is, it replaces the value. If it doesn't, it adds the key and value to the hashmap
     *
     * @param plainData The data that is being sent from the simulator.
     */
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

    /**
     * Sets the start location of the user.
     *
     * @param longitude The longitude of the starting point.
     * @param latitude The latitude of the starting point.
     */
    public void setFrom(String longitude,String latitude){
        analytics.put("StartLongitude",longitude);
        analytics.put("StartLatitude",latitude);
    }

    /**
     * This function is used to set the end location of the user's journey
     *
     * @param longitude The longitude of the destination
     * @param latitude The latitude of the location.
     */
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

    /**
     * This function is used to get the final analytics of the trip
     *
     * @return A string containing the analytics of the trip.
     */
    public String getFinalAnalytics(){
        StringBuilder analyticsString = new StringBuilder();
        analyticsString.append("StartLongitude:").append(analytics.get("StartLongitude")).append(" StartLatitude:").append(analytics.get("StartLatitude"));
        analyticsString.append(" EndLongitude:").append(analytics.get("EndLongitude")).append(" EndLatitude:").append(analytics.get("EndLatitude"));
        analyticsString.append(" startTime:").append(analytics.get("StartTime")).append(" endTime:").append(analytics.get("EndTime"));
        analyticsString.append(" maxAlt:").append(analytics.get("altitude")).append(" maxSpeed:").append(analytics.get("speed"));
        analyticsString.append(" Nautical_Mile:").append(Nautical_Mile);
        MyLogger.LogMessage("Nautical_Mile is: " + Nautical_Mile);
        return analyticsString.toString();
    }

    /**
     * The Haversine formula determines the great-circle distance between two points on a sphere given their longitudes and
     * latitudes
     *
     * @param lat1 Latitude of point 1 (in decimal degrees)
     * @param lon1 longitude of the first point
     * @param lat2 Latitude of the second point
     * @param lon2 longitude of the second point
     * @return The distance in Nautical Miles
     */
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

    /**
     * Convert degrees to radians.
     *
     * @param deg The latitude or longitude of the point, in degrees.
     * @return The distance between two points on the earth.
     */
    public double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}
