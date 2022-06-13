package Model;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.List;

public class DataBase {
    MongoClient client;
    MongoDatabase database;
    static int i =0;

    public DataBase(String connectionURL, String dbName ){
       this.client = MongoClients.create(connectionURL);
       this.database = this.client.getDatabase(dbName);

    }

    public MongoDatabase getDatabase(){
        return this.database;
    }

    public MongoIterable<String> getColList(){
        return this.database.listCollectionNames();
    }
    public void createCollection(String colName){
        this.database.createCollection(colName);
    }
    public void addDocument(String colName, Document doc){
        this.database.getCollection(colName).insertOne(doc);
    }

    public void closeClient(){
        this.client.close();
    }

    public FindIterable<Document> getDoc(String colName, Document doc){
        return this.database.getCollection(colName).find(doc);
    }

    public FindIterable<Document> getDocById(String colName, Integer id){
        return this.database.getCollection(colName).find(new Document().append("_id",id));
    }

    public FindIterable<Document> getDocByName(String colName, String name){
        return this.database.getCollection(colName).find(new Document().append("name",name));
    }

    public Document deleteAndGetDoc(String colName, Document doc){
        return this.database.getCollection(colName).findOneAndDelete(doc);
    }

    public void deleteDocById(String colName, Integer id){
        this.database.getCollection(colName).findOneAndDelete(new Document().append("_id", id));
    }

    public void deleteCol(String colName){
        this.database.getCollection(colName).drop();
    }

    public MongoCollection<Document> getColl(String colName){
        return this.database.getCollection(colName);
    }

    public void savePlainTimeSeries(String plainId,String plainName, List<List<String>> ts){
        Document doc = new Document();
        doc.append("_id",plainId).append("plainName",plainName).append("ts",ts);
        this.addDocument("Flights",doc);
    }

    public FindIterable<Document> getTSbyPlainID(String id){
        return this.database.getCollection("Flights").find(new Document().append("_id",id));
    }

    public FindIterable<Document> getTSbyPlainName(String name){
        return this.database.getCollection("Flights").find(new Document().append("plainName",name));
    }

}
