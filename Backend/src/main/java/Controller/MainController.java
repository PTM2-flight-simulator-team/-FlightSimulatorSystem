package Controller;

import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;

public class MainController {

    public static void main(String[] args){
        Controller c = new Controller();
//        c.setPlaneDataValue(planeData.getId(), planeData);
        System.out.println("Thread id:" + Thread.currentThread().getId());
        System.out.println("main thread died");


    }


}