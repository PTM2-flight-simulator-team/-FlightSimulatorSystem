package CommonClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PlainData implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;
    private String ID;
    private String plainName;
    private PlainVar aileron;
    private PlainVar elevator;
    private PlainVar rudder;
    private PlainVar flaps;
    private PlainVar longitude;
    private PlainVar latitude;
    private PlainVar airSpeed_kt;
    private PlainVar vertSpeed;
    private PlainVar throttle_0;
    private PlainVar throttle_1;
    private PlainVar altitude; // height
    private PlainVar pitchDeg;
    private PlainVar rollDeg;
    private PlainVar heading;
    private PlainVar turnCoordinator; // didnt find

//    public String getPlainName() {
//        return plainName;
//    }

    public String getAileron() {
        return aileron.value;
    }

    public String getElevator() {
        return elevator.value;
    }

    public String getRudder() {
        return rudder.value;
    }

    public String getFlaps() {
        return flaps.value;
    }

    public String getLongitude() {
        return longitude.value;
    }

    public String getLatitude() {
        return latitude.value;
    }

    public String getAirSpeed_kt() { return airSpeed_kt.value;}

    public String getVertSpeed() {
        return vertSpeed.value;
    }

    public String getThrottle_0() {
        return throttle_0.value;
    }

    public String getThrottle_1() {
        return throttle_1.value;
    }

    public String getAltitude() {
        return altitude.value;
    }

    public String getPitchDeg() {
        return pitchDeg.value;
    }

    public String getRollDeg() {
        return rollDeg.value;
    }

    public String getHeading() {
        return heading.value;
    }

    public String getTurnCoordinator() {
        return turnCoordinator.value;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPlainName(String plainName) {
        this.plainName = plainName;
    }

    public PlainData(HashMap<String,String> map){
//        this.plainName = name;
        this.aileron = new PlainVar("/controls/flight/aileron[0]","aileron",map.get("aileron")) ;
        this.elevator = new PlainVar("/controls/flight/elevator","elevator",map.get("elevator"));
        this.rudder = new PlainVar("/controls/flight/rudder","rudder",map.get("rudder"));
        this.longitude = new PlainVar("/position/longitude-deg","longitude-deg",map.get("longitude-deg"));
        this.latitude = new PlainVar("/position/latitude-deg","latitude-deg",map.get("latitude-deg"));
        this.airSpeed_kt =new PlainVar("/instrumentation/airspeed-indicator/indicated-speed-kt","airspeed-indicator_indicated-speed-kt",map.get("airspeed-indicator_indicated-speed-kt"));
        this.vertSpeed = new PlainVar("/velocities/vertical-speed-fps","vertical-speed",map.get("vertical-speed-fps"));
        this.throttle_0 = new PlainVar("/controls/engines/current-engine/throttle","throttle_0",map.get("throttle_0"));
        this.throttle_1 = new PlainVar("/controls/engines/engine[1]/throttle","throttle_1",map.get("throttle_1"));
        this.altitude = new PlainVar("/instrumentation/altimeter/indicated-altitude-ft","altitude",map.get("altimeter_indicated-altitude-ft"));
        this.pitchDeg = new PlainVar("/instrumentation/attitude-indicator/internal-pitch-deg","pitchDeg",map.get("attitude-indicator_internal-pitch-deg"));
        this.rollDeg = new PlainVar("instrumentation/attitude-indicator/indicated-roll-deg","rollDeg",map.get("attitude-indicator_indicated-roll-deg"));
        this.heading = new PlainVar("/instrumentation/heading-indicator/offset-deg","heading",map.get("indicated-heading-deg"));
        //indicated-heading-deg,/instrumentation/heading-indicator/indicated-heading-deg
        // heading might be wrong need the offset
        this.turnCoordinator = new PlainVar("/orientation/side-slip-deg","turnCoordinator",map.get("side-slip-deg"));
    }

    public ArrayList<String> PlainDataToList(){
        ArrayList<String> data = new ArrayList<>();
        data.add(this.aileron.value);
        data.add(this.elevator.value);
        data.add(this.rudder.value);
        data.add(this.longitude.value);
        data.add(this.latitude.value);
        data.add(this.airSpeed_kt.value);
        data.add(this.vertSpeed.value);

        data.add(this.throttle_0.value);
        data.add(this.throttle_1.value);
        data.add(this.altitude.value);
        data.add(this.pitchDeg.value);
        data.add(this.rollDeg.value);
        data.add(this.heading.value);
        data.add(this.turnCoordinator.value);
        return data;
    }
    public void Print(){
        System.out.println(
                "aileron:"+this.aileron.value +",elevator:" + this.elevator.value +",rudder:" + this.rudder.value+",longitude-deg:" + this.longitude.value+",latitude-deg:" + this.latitude.value
                        +",airspeed-kt:" + this.airSpeed_kt.value+",vertical-speed-fps:" + this.vertSpeed.value+",throttle_0:" + this.throttle_0.value+",throttle_1:" + this.throttle_1.value
                        +",altitude-ft:" + this.altitude.value +",pitch-deg:" + this.pitchDeg.value+",roll-deg:" + this.rollDeg.value
                        +",heading-deg:" + this.heading.value+",side-slip-deg:" + this.turnCoordinator.value);
    }

}