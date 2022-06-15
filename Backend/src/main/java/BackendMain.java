import Model.DataBase;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.List;

public class BackendMain {
    public static void main(String[] args) {
//        System.out.println("test");
//
//        String url = "mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority";
//        DataBase db = new DataBase(url,"FlightFleet");
//        db.createCollection("samp12");
//        db.addDocument("samp12",new Document().append("_id", 3).append("name","Guy"));
//        db.closeClient();

        DataBase db = new DataBase("mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority","FlightFleet");
        System.out.println(db.getTSbyPlaneID("1").first().get("ts"));
        List<List<String>> l = (List<List<String>>) db.getTSbyPlaneID("1").first().get("ts");

        System.out.println(l.get(0));




    }
}
