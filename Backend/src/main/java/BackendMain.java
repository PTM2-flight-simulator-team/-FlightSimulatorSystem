import Model.DataBase;
import com.mongodb.client.*;
import org.bson.Document;

public class BackendMain {
    public static void main(String[] args) {
        System.out.println("test");

        String url = "mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority";
        DataBase db = new DataBase(url,"FlightFleet");
//        db.createCollection("samp12");
       // db.addDocument("samp12",new Document().append("_id", 3).append("name","Guy"));

       // FindIterable<Document> docs =  db.getDoc("samp12",new Document().append("_id","1"));

        //Document doc = db.deleteAndGetDoc("samp12",new Document().append("_id","1"));

//       Document doc = db.deleteById("samp12",3);

        FindIterable<Document> doc = db.getByName("samp12","Guy");

        for(Document d : doc){
            System.out.println(d.get("_id"));
        }

//        System.out.println(doc.get("name"));
        db.closeClient();

//        MongoClient client = MongoClients.create(
//                "mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority");
//
//        MongoDatabase db = client.getDatabase("sample_db12");
//
//        MongoCollection<Document> col = db.getCollection("customers");
//
//        Document sampleDoc = new Document("id", 1).append("name", "Yossi Smith");
//
//        col.insertOne(sampleDoc);
//
//        client.close();
    }
}
