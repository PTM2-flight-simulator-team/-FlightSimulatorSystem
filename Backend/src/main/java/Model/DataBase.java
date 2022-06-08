package Model;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBase {
    MongoClient client;
    MongoDatabase database;
    MongoIterable<String> list;
    public DataBase(String connectionURL, String dbName ){
       this.client = MongoClients.create(connectionURL);
       this.database = this.client.getDatabase(dbName);
       this.list = this.database.listCollectionNames();
    }
    public void createCollection(String colName){
        this.database.createCollection(colName);
//        Document sampleDoc = new Document("id", 1).append("name", "Yossi Smith");
//        col.insertOne(sampleDoc);

    }
    public List<String> getCollections(){
        List<String> connectionList = new ArrayList<>();
        this.list.forEach(connectionList::add);
        return connectionList;
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

    public FindIterable<Document> getById(String colName, Integer id){
        return this.database.getCollection(colName).find(new Document().append("_id",id));
    }

    public FindIterable<Document> getByName(String colName, String name){
        return this.database.getCollection(colName).find(new Document().append("name",name));
    }

    public Document deleteAndGetDoc(String colName, Document doc){
        return this.database.getCollection(colName).findOneAndDelete(doc);
    }

    public Document deleteById(String colName, Integer id){
        return this.database.getCollection(colName).findOneAndDelete(new Document().append("_id", id));
    }

}
