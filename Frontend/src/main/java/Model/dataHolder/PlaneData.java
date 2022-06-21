package Model.dataHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaneData {
    public String ID;
    public String PlaneName;
    public String aileron;
    public String elevator;
    public String rudder;
    public String longitude;
    public String latitude;
    public String airSpeed_kt;
    public String vertSpeed;
    public String throttle_0;
    public String throttle_1;
    public String altitude; // height
    public String pitchDeg;
    public String rollDeg;
    public String heading;
    public String turnCoordinator;

    public List<String> getPlaneDataAsList(){
        List<String> planeData = new ArrayList<>();
        planeData.add(this.aileron);
        planeData.add(this.elevator);
        planeData.add(this.rudder);
        planeData.add(this.longitude);
        planeData.add(this.latitude);
        planeData.add(this.airSpeed_kt);
        planeData.add(this.vertSpeed);
        planeData.add(this.throttle_0);
        planeData.add(this.throttle_1);
        planeData.add(this.altitude);
        planeData.add(this.pitchDeg);
        planeData.add(this.rollDeg);
        planeData.add(this.heading);
        planeData.add(this.turnCoordinator);
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = currentTime.format(timeFormatter);
        planeData.add(time);
        return planeData;
    }
}

//{"aileron":"0.000000","elevator":"0.000000","rudder":"0.000000","longitude-deg":"-22.6580185466","latitude-deg":"63.9794995244"
// ,"airspeed-indicator_indicated-speed-kt":"0.000000","vertical-speed":"-0.000000","throttle_0":"0.000000","throttle_1":"0.000000"
// ,"altitude":"10.391782","pitchDeg":"0.000000","rollDeg":"40.000000","heading":"12.891746","turnCoordinator":"69.313248"}