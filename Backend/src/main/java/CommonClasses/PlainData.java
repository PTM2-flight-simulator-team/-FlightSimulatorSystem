package CommonClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlainData implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;

    List<PlainVar> plainVarList = new ArrayList<>();
    private String id;
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

    public PlainVar getAileron() {
        return aileron;
    }

    public PlainVar getElevator() {
        return elevator;
    }

    public PlainVar getRudder() {
        return rudder;
    }

    public PlainVar getFlaps() {
        return flaps;
    }

    public PlainVar getLongitude() {
        return longitude;
    }

    public PlainVar getLatitude() {
        return latitude;
    }

    public PlainVar getAirSpeed_kt() {
        return airSpeed_kt;
    }

    public PlainVar getVertSpeed() {
        return vertSpeed;
    }

    public PlainVar getThrottle_0() {
        return throttle_0;
    }

    public PlainVar getThrottle_1() {
        return throttle_1;
    }

    public PlainVar getAltitude() {
        return altitude;
    }

    public PlainVar getPitchDeg() {
        return pitchDeg;
    }

    public PlainVar getRollDeg() {
        return rollDeg;
    }

    public PlainVar getHeading() {
        return heading;
    }

    public PlainVar getTurnCoordinator() {
        return turnCoordinator;
    }
    public List<PlainVar> getAllVars() {
        return plainVarList;
    }

    public String getId() {
        return id;
    }
    public PlainData() {
    }

    public void Print(){
        System.out.println(
                "aileron:"+this.aileron.value +",elevator:" + this.elevator.value +",rudder:" + this.rudder.value+",longitude-deg:" + this.longitude.value+",latitude-deg:" + this.latitude.value
                        +",airspeed-kt:" + this.airSpeed_kt.value+",vertical-speed-fps:" + this.vertSpeed.value+",throttle_0:" + this.throttle_0.value+",throttle_1:" + this.throttle_1.value
                        +",altitude-ft:" + this.altitude.value +",pitch-deg:" + this.pitchDeg.value+",roll-deg:" + this.rollDeg.value
                        +",heading-deg:" + this.heading.value+",side-slip-deg:" + this.turnCoordinator.value);
    }
}