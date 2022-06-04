package CommonClasses;

import java.io.Serializable;
import java.util.HashMap;

public class PlainData implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;
    private String plainName;
    private String aileron;
    private String elevator;
    private String rudder;
    private String flaps;
    private String longitude;
    private String latitude;
    private String airSpeed_kt;
    private String vertSpeed;
    private String throttle_0;
    private String throttle_1;
    private String altitude; // height
    private String pitchDeg;
    private String rollDeg; 
    private String heading;
    private String turnCoordinator; // didnt find

    public PlainData(String name,HashMap<String,String> map){
        this.plainName = name;
        this.aileron = map.get("aileron");
        this.elevator = map.get("elevator");
        this.rudder = map.get("rudder");
        this.longitude = map.get("longitude-deg");
        this.latitude = map.get("latitude-deg");
        this.airSpeed_kt = map.get("airspeed-kt");
        this.vertSpeed = map.get("vertical-speed-fps");
        this.throttle_0 = map.get("throttle_0");
        this.throttle_1 = map.get("throttle_1");
        this.altitude = map.get("altitude-ft");
        this.pitchDeg = map.get("pitch-deg");
        this.rollDeg = map.get("roll-deg");
        this.heading = map.get("heading-deg");
        this.turnCoordinator = map.get("side-slip-deg");
    }

    public  void Print(){
        System.out.println(this.aileron);
    }
    
}
