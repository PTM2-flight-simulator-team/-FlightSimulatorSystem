package Controller;

import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;

public class MainController {

    public static void main(String[] args){
//        OpenServerCommand sever = new OpenServerCommand();
//        sever.execute();
        PlaneData planeData = new PlaneData();
        planeData.setAileron(new PlaneVar("///", "ail","1"));
        planeData.setAltitude(new PlaneVar("///", "ail1","1"));
        planeData.setAirSpeed_kt(new PlaneVar("///", "ail2","1"));
        planeData.setElevator(new PlaneVar("///", "ail3","1"));
        planeData.setFlaps(new PlaneVar("///", "ail4","1"));
        planeData.setHeading(new PlaneVar("///", "ail5","1"));
        planeData.setLatitude(new PlaneVar("///", "ail6","1"));
        planeData.setLongitude(new PlaneVar("///", "ail7","1"));
        planeData.setPitchDeg(new PlaneVar("///", "ail8","1"));
        planeData.setRollDeg(new PlaneVar("///", "ail9","1"));
        planeData.setThrottle_0(new PlaneVar("///", "ail10","1"));
        planeData.setID("123");
        planeData.setRudder(new PlaneVar("///", "ail11","1"));
        Controller c = new Controller();
        c.setPlaneDataValue(planeData.getId(), planeData);
//        System.out.println("Thread id:" + Thread.currentThread().getId());
//        System.out.println("main thread died");


    }


}