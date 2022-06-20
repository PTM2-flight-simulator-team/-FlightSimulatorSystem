package CommonClasses;

import javax.swing.text.Document;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaneData implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;

    private String ID;
    private String PlaneName;
    private PlaneVar aileron;
    private PlaneVar elevator;
    private PlaneVar rudder;
    private PlaneVar flaps;
    private PlaneVar longitude;
    private PlaneVar latitude;
    private PlaneVar airSpeed_kt;
    private PlaneVar vertSpeed;
    private PlaneVar throttle_0;
    private PlaneVar throttle_1;
    private PlaneVar altitude; // height
    private PlaneVar pitchDeg;
    private PlaneVar rollDeg;
    private PlaneVar heading;
    private PlaneVar turnCoordinator; // didnt find

    public PlaneData() {
    }

    public String getPlaneName() {
        return PlaneName;
    }

    public PlaneVar getAileron() {
        return aileron;
    }

    public PlaneVar getElevator() {
        return elevator;
    }

    public PlaneVar getRudder() {
        return rudder;
    }

    public PlaneVar getFlaps() {
        return flaps;
    }

    public PlaneVar getLongitude() {
        return longitude;
    }

    public PlaneVar getLatitude() {
        return latitude;
    }

    public PlaneVar getAirSpeed_kt() {
        return airSpeed_kt;
    }

    public PlaneVar getVertSpeed() {
        return vertSpeed;
    }

    public PlaneVar getThrottle_0() {
        return throttle_0;
    }

    public PlaneVar getThrottle_1() {
        return throttle_1;
    }

    public PlaneVar getAltitude() {
        return altitude;
    }

    public PlaneVar getPitchDeg() {
        return pitchDeg;
    }

    public PlaneVar getRollDeg() {
        return rollDeg;
    }

    public PlaneVar getHeading() {
        return heading;
    }

    public PlaneVar getTurnCoordinator() {
        return turnCoordinator;
    }

    public String getId() {
        return ID;
    }

    public void Print(){
        System.out.println(
                "aileron:"+this.aileron.value +",elevator:" + this.elevator.value +",rudder:" + this.rudder.value+",longitude-deg:" + this.longitude.value+",latitude-deg:" + this.latitude.value
                        +",airspeed-kt:" + this.airSpeed_kt.value+",vertical-speed-fps:" + this.vertSpeed.value+",throttle_0:" + this.throttle_0.value+",throttle_1:" + this.throttle_1.value
                        +",altitude-ft:" + this.altitude.value +",pitch-deg:" + this.pitchDeg.value+",roll-deg:" + this.rollDeg.value
                        +",heading-deg:" + this.heading.value+",side-slip-deg:" + this.turnCoordinator.value);
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPlaneName(String planeName) {
        PlaneName = planeName;
    }

    public void setAileron(PlaneVar aileron) {
        this.aileron = aileron;
    }

    public void setElevator(PlaneVar elevator) {
        this.elevator = elevator;
    }

    public void setRudder(PlaneVar rudder) {
        this.rudder = rudder;

    }

    public void setFlaps(PlaneVar flaps) {
        this.flaps = flaps;
    }

    public void setLongitude(PlaneVar longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(PlaneVar latitude) {
        this.latitude = latitude;
    }

    public void setAirSpeed_kt(PlaneVar airSpeed_kt) {
        this.airSpeed_kt = airSpeed_kt;
    }

    public void setVertSpeed(PlaneVar vertSpeed) {
        this.vertSpeed = vertSpeed;
    }

    public void setThrottle_0(PlaneVar throttle_0) {
        this.throttle_0 = throttle_0;
    }

    public void setThrottle_1(PlaneVar throttle_1) {
        this.throttle_1 = throttle_1;
    }

    public void setAltitude(PlaneVar altitude) {
        this.altitude = altitude;
    }

    public void setPitchDeg(PlaneVar pitchDeg) {
        this.pitchDeg = pitchDeg;
    }

    public void setRollDeg(PlaneVar rollDeg) {
        this.rollDeg = rollDeg;
    }

    public void setHeading(PlaneVar heading) {
        this.heading = heading;
    }

    public void setTurnCoordinator(PlaneVar turnCoordinator) {
        this.turnCoordinator = turnCoordinator;
    }
    public String getID() {
        return ID;
    }

    public double getValueByPath(String path){
        List<PlaneVar> list = new ArrayList<>();
        list.add(aileron);
        list.add(elevator);
        list.add(rudder);
        list.add(flaps);
        list.add(longitude);
        list.add(latitude);
        list.add(airSpeed_kt);
        list.add(vertSpeed);
        list.add(throttle_0);
        list.add(throttle_1);
        list.add(altitude); // height
        list.add(pitchDeg);
        list.add(rollDeg);
        list.add(heading);
        list.add(turnCoordinator); // didnt find
        for (PlaneVar var: list){
            if(var != null && var.path.equals(path)){
                Double d = Double.parseDouble(var.getValue());
                return d.doubleValue();
            }
        }
        return Double.MIN_VALUE;
    }

    public PlaneVar getPlaneVarByPath(String path){
        List<PlaneVar> list = new ArrayList<>();
        list.add(aileron);
        list.add(elevator);
        list.add(rudder);
        list.add(flaps);
        list.add(longitude);
        list.add(latitude);
        list.add(airSpeed_kt);
        list.add(vertSpeed);
        list.add(throttle_0);
        list.add(throttle_1);
        list.add(altitude); // height
        list.add(pitchDeg);
        list.add(rollDeg);
        list.add(heading);
        list.add(turnCoordinator); // didnt find
        for (PlaneVar var: list){
            if(var != null && var.path.equals(path)){
                return var;
            }
        }
        return null;
    }

    public List<PlaneVar> getAllVars(){
        List<PlaneVar> list = new ArrayList<>();
        list.add(aileron);
        list.add(elevator);
        list.add(rudder);
        list.add(longitude);
        list.add(latitude);
        list.add(airSpeed_kt);
        list.add(vertSpeed);
        list.add(throttle_0);
        list.add(throttle_1);
        list.add(altitude); // height
        list.add(pitchDeg);
        list.add(rollDeg);
        list.add(heading);
        list.add(turnCoordinator); // didnt find
        System.out.println(list);
        return list;
    }
    
}