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

    public String getPlainName() {
        return plainName;
    }

    public String getAileron() {
        return aileron;
    }

    public String getElevator() {
        return elevator;
    }

    public String getRudder() {
        return rudder;
    }

    public String getFlaps() {
        return flaps;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAirSpeed_kt() {
        return airSpeed_kt;
    }

    public String getVertSpeed() {
        return vertSpeed;
    }

    public String getThrottle_0() {
        return throttle_0;
    }

    public String getThrottle_1() {
        return throttle_1;
    }

    public String getAltitude() {
        return altitude;
    }

    public String getPitchDeg() {
        return pitchDeg;
    }

    public String getRollDeg() {
        return rollDeg;
    }

    public String getHeading() {
        return heading;
    }

    public String getTurnCoordinator() {
        return turnCoordinator;
    }

    public PlainData(String name, HashMap<String,String> map){
        this.plainName = name;
        this.aileron = map.get("aileron");
        this.elevator = map.get("elevator");
        this.rudder = map.get("rudder");
        this.longitude = map.get("longitude-deg");
        this.latitude = map.get("latitude-deg");
        this.airSpeed_kt = map.get("airspeed-indicator_indicated-speed-kt");
        this.vertSpeed = map.get("vertical-speed-fps");
        this.throttle_0 = map.get("throttle_0");
        this.throttle_1 = map.get("throttle_1");
        this.altitude = map.get("altimeter_indicated-altitude-ft");
        this.pitchDeg = map.get("attitude-indicator_internal-pitch-deg");
        this.rollDeg = map.get("attitude-indicator_indicated-roll-deg");
        this.heading = map.get("indicated-heading-deg");
        //indicated-heading-deg,/instrumentation/heading-indicator/indicated-heading-deg
        // heading might be wrong need the offset
        this.turnCoordinator = map.get("side-slip-deg");
    }

    public String PlainDataToString(){
        return "aileron"+this.aileron +",elevator" + this.elevator +",rudder" + this.rudder+",longitude-deg" + this.longitude+",latitude-deg" + this.latitude
                +",airspeed-kt" + this.airSpeed_kt+",vertical-speed-fps" + this.vertSpeed+",throttle_0" + this.throttle_0+",throttle_1" + this.throttle_1
                +",altitude-ft" + this.altitude +",pitch-deg" + this.pitchDeg+",roll-deg" + this.rollDeg
                +",heading-deg" + this.heading+",side-slip-deg" + this.turnCoordinator;
    }
    public void Print(){
        System.out.println(
                "aileron:"+this.aileron +",elevator:" + this.elevator +",rudder:" + this.rudder+",longitude-deg:" + this.longitude+",latitude-deg:" + this.latitude
                        +",airspeed-kt:" + this.airSpeed_kt+",vertical-speed-fps:" + this.vertSpeed+",throttle_0:" + this.throttle_0+",throttle_1:" + this.throttle_1
                        +",altitude-ft:" + this.altitude +",pitch-deg:" + this.pitchDeg+",roll-deg:" + this.rollDeg
                        +",heading-deg:" + this.heading+",side-slip-deg:" + this.turnCoordinator);
    }
    
}
