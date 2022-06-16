import Model.DataBase;
import com.mongodb.client.*;
import org.bson.Document;

public class BackendMain {
    public static void main(String[] args) {
        System.out.println("test");

        String url = "";// add url
        DataBase db = new DataBase(url,"FlightFleet");
        db.createCollection("samp12");
        db.addDocument("samp12",new Document().append("_id", 3).append("name","Guy"));
        db.closeClient();
    }
}
