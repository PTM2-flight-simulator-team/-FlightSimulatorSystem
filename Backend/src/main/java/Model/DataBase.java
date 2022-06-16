package Model;

import CommonClasses.PlaneData;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public FindIterable<Document> GetPlanes(){
        return this.database.getCollection("AirCrafts").find();
    }

    public FindIterable<Document> getDocById(String colName, String id){
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

    public void savePlaneTimeSeries(String planeId,String planeName, List<List<String>> ts){
        //System.out.println("planeId:" + planeId + "ts: " + ts);
        Document doc = new Document();
        doc.append("plainID",planeId).append("PlaneName",planeName).append("ts",ts);
        System.out.println(doc);
        this.addDocument("TimeSeries",doc);
    }

    public FindIterable<Document> getTSbyPlaneID(String id){
        return this.database.getCollection("TimeSeries").find(new Document().append("_id",id));
    }




    public FindIterable<Document> getTSbyPlaneName(String name){
        return this.database.getCollection("TimeSeries").find(new Document().append("planeName",name));
    }
    public void saveNewPlaneAnalytics(String id, String name, Month month, Double miles, Boolean active, PlaneData planeData){
        HashMap<String,Double> hashMap = new HashMap<>();
       hashMap.put(Month.JANUARY.toString(),0.0);
       hashMap.put(Month.FEBRUARY.toString(),0.0);
       hashMap.put(Month.MARCH.toString(),0.0);
       hashMap.put(Month.APRIL.toString(),0.0);
       hashMap.put(Month.MAY.toString(),0.0);
       hashMap.put(Month.JUNE.toString(),0.0);
       hashMap.put(Month.JULY.toString(),0.0);
       hashMap.put(Month.AUGUST.toString(),0.0);
       hashMap.put(Month.SEPTEMBER.toString(),0.0);
       hashMap.put(Month.OCTOBER.toString(),0.0);
       hashMap.put(Month.NOVEMBER.toString(),0.0);
       hashMap.put(Month.DECEMBER.toString(),0.0);

       hashMap.put(month.toString(),hashMap.get(month.toString())+miles);

        Document d = new Document().append("_id",id).append("Name", name).append("miles",hashMap).append("active",active).append("planeData" ,planeData);
        this.addDocument("AirCrafts",d);
    }


    public void updateMilesById(String id, Double mile,Month month){
        Month currentMonth = LocalDate.now().getMonth();
        FindIterable<Document> docs = this.getDocById("AirCrafts",id);
        HashMap<String,Double> hashMap = new HashMap<>();
        docs.forEach((d)->{
            Document doc = (Document) d.get("miles");
            doc.forEach((key,value)->{
                hashMap.put(key, (Double) value);
            });
        });
        if(hashMap.containsKey(currentMonth.toString()))
            hashMap.put(month.toString(),hashMap.get(currentMonth.toString())+mile);
        else
            hashMap.put(currentMonth.toString(),mile);
        BasicDBObject query = new BasicDBObject();
        query.put("_id",id ); // (1)

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("miles", hashMap); // (2)

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument); // (3)

        database.getCollection("AirCrafts").updateOne(query, updateObject); // (4)

    }
    public void changePlaneState(String id, Boolean state){
        BasicDBObject query = new BasicDBObject();
        query.put("_id",id);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("active",state);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set",newDocument);
        database.getCollection("AirCrafts").updateOne(query,updateObject);

    }

    public void changePlaneData(String id, PlaneData planeData){
        BasicDBObject query = new BasicDBObject();
        query.put("_id",id);

        BasicDBObject newDoc = new BasicDBObject();
        newDoc.put("planeData",planeData);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set",newDoc);
        database.getCollection("AirCrafts").updateOne(query,updateObject);
    }
    
    public boolean doesPlaneExists(String id){
        FindIterable<Document> d = this.getDocById("AirCrafts", id);
        AtomicBoolean b = new AtomicBoolean(false);
        d.forEach((d1)->{
            if(d1 != null)
                b.set(true);
        });
        return b.get();
    }
    

}
