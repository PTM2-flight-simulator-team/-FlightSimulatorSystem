package Model;

import CommonClasses.PlaneData;
import Model.Interpreter.Expression.BinaryExpression;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
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

    public FindIterable<Document> GetPlanes() throws Exception{
        Document doc = this.database.getCollection("AirCrafts").find().first();

        if(doc != null)
            return this.database.getCollection("AirCrafts").find();
        else
            throw new Exception("AirCrafts collection is empty");

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
        Document d = this.database.getCollection("TimeSeries").find(new Document().append("planeID",planeId)).first();
        if(d == null){
            System.out.println("plane does not exits");
            List<List<List<String>>> l = new ArrayList<>();
            l.add(ts);
            Document doc = new Document();
            doc.append("planeID",planeId).append("planeName",planeName).append("tsList",l);
            System.out.println(doc);
            this.addDocument("TimeSeries",doc);
        }
        else{
            System.out.println("plane exists");
            this.addTs(planeId,ts);
        }

    }

    public List<List<String>> getTSbyPlaneID(String id,int index) throws Exception{
//        return this.database.getCollection("TimeSeries").find(new Document().append("planeID",id));
        Document doc = this.database.getCollection("TimeSeries").find(new Document().append("planeID",id)).first();
        if(doc != null){
            List<List<List<String>>> list = (List<List<List<String>>>) doc.get("tsList");
            return list.get(index);
        }else
            throw new Exception("plane does not exists in TimeSeries collection");
    }

    public int getTSIndexesByPlaneID(String id) throws Exception{
//        return this.database.getCollection("TimeSeries").find(new Document().append("planeID",id));
        Document doc = this.database.getCollection("TimeSeries").find(new Document().append("planeID",id)).first();
        if(doc != null){
            List<List<List<String>>> list = (List<List<List<String>>>) doc.get("tsList");
            return list.size();
        }else
            throw new Exception("plane does not exists in TimeSeries collection");
    }

    public void saveNewPlaneAnalytics(String id, String name, Month month, Double miles, Boolean active, HashMap<String ,String> planeData){
        LocalDate currentdate = LocalDate.now();
        HashMap<String,Double> metrics = new HashMap<>();
        Integer month1 = currentdate.getMonth().getValue();
        metrics.put(Month.JANUARY.toString(),0.0);
        metrics.put(Month.FEBRUARY.toString(),0.0);
        metrics.put(Month.MARCH.toString(),0.0);
        metrics.put(Month.APRIL.toString(),0.0);
        metrics.put(Month.MAY.toString(),0.0);
        metrics.put(Month.JUNE.toString(),0.0);
        metrics.put(Month.JULY.toString(),0.0);
        metrics.put(Month.AUGUST.toString(),0.0);
        metrics.put(Month.SEPTEMBER.toString(),0.0);
        metrics.put(Month.OCTOBER.toString(),0.0);
        metrics.put(Month.NOVEMBER.toString(),0.0);
        metrics.put(Month.DECEMBER.toString(),0.0);
        metrics.put(month.toString(),metrics.get(month.toString())+miles);
//        String gson = new Gson().toJson(planeData);
        Document d = new Document().append("_id",id).append("name", name).append("miles",metrics).append("active",active).append("planeData" ,planeData).append("createdMonth", month1);
        this.addDocument("AirCrafts",d);
    }


    public void updateMilesById(String id, Double mile,Month month){
        Month currentMonth = LocalDate.now().getMonth();
        FindIterable<Document> docs = this.getDocById("AirCrafts",id);
        HashMap<String,Double> hashMap = new HashMap<>();
//        int numberOfFlights = (Integer) Objects.requireNonNull(docs.first()).get("NumberOfFlights");
        docs.forEach((d)->{
            Document doc = (Document) d.get("miles");
            doc.forEach((key,value)->
                hashMap.put(key, (Double) value));

        });
        if(hashMap.containsKey(currentMonth.toString()))
            hashMap.put(month.toString(),hashMap.get(currentMonth.toString())+mile);
        else
            hashMap.put(currentMonth.toString(),mile);
        BasicDBObject query = new BasicDBObject();
        query.put("_id",id ); // (1)

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("miles", hashMap);
//
//        BasicDBObject newDoc2 = new BasicDBObject();
//        newDoc2.put("NumberOfFlights",numberOfFlights+1);



        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

//        BasicDBObject update2 = new BasicDBObject();
//        update2.put("$set", newDoc2);

        database.getCollection("AirCrafts").updateOne(query, updateObject);
      //  database.getCollection("AirCrafts").updateOne(query,update2);

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


    public void addTs(String id, List<List<String>> ts){
        BasicDBObject query = new BasicDBObject();
        query.put("planeID",id);
//         Document doc =  this.getDocById("TimeSeries",id).first();
        Document doc = this.database.getCollection("TimeSeries").find(new Document().append("planeID",id)).first();
        if(doc != null){
            System.out.println("doc != null");
            List<List<List<String>>> list = (List<List<List<String>>>) doc.get("tsList");
            list.add(ts);
            BasicDBObject newDoc = new BasicDBObject();
            newDoc.put("tsList",list);

            BasicDBObject update = new BasicDBObject();
            update.put("$set",newDoc);
            database.getCollection("TimeSeries").updateOne(query,update);
        }


    }
    public HashMap<Integer,Integer> getActivePlaneByMonth(){
        LocalDate currentdate = LocalDate.now();
        Integer month1 = currentdate.getMonth().getValue();
        HashMap<Integer,Integer> hashMap = new HashMap<>();
        int[] array = new int[12];
        FindIterable<Document> it = this.database.getCollection("AirCrafts").find();
        List<Integer> l = new ArrayList<>();
        it.forEach((d)->{
            if(d.get("createdMonth") != null){
                int x = (Integer) d.get("createdMonth");
                array[x]++;
            }


        });
        int sum =0;
        for(int i=1; i<13;i++){
            if(i > month1+1)
                hashMap.put(i,0);

            else {

                hashMap.put(i-1, sum);
                sum += array[i];
            }
        }
        return hashMap;
    }


    }


