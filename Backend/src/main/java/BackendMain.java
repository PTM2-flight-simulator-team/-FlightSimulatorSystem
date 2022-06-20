import Model.DataBase;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BackendMain {
    public static void main(String[] args) {
        System.out.println("test");

//        String url = "";// add url
//        DataBase db = new DataBase(url,"FlightFleet");
//        db.createCollection("samp12");
//        db.addDocument("samp12",new Document().append("_id", 3).append("name","Guy"));
//        db.closeClient();
        DataBase db = new DataBase("mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority","FlightFleet");
        List<List<String>> l = new ArrayList<>();
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        List<String> l3 = new ArrayList<>();
        l1.add("1");
        l1.add("2");
        l1.add("3");
        l2.add("1");
        l2.add("2");
        l2.add("3");
        l3.add("1");
        l3.add("2");
        l3.add("3");
        l.add(l1);
        l.add(l2);
        l.add(l3);

        db.savePlaneTimeSeries("2","shini",l);

    }
}
