import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class BackendMain {
    public static void main(String[] args) {
        System.out.println("test");

        MongoClient client = MongoClients.create(
                "mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority");

        MongoDatabase db = client.getDatabase("sample_db12");

        MongoCollection<Document> col = db.getCollection("customers");

        Document sampleDoc = new Document("id", 1).append("name", "Yossi Smith");

        col.insertOne(sampleDoc);

        client.close();
    }
}
