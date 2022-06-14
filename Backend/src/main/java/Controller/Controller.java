package Controller;


import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import Controller.Commands.Command;
import Controller.Commands.GetFromDBCommand;
import Controller.Commands.OpenCliCommand;
import Controller.Commands.OpenServerCommand;
import Controller.ServerConnection.FrontConnection.MyHttpServer;
import Model.Model;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Controller implements Observer {
   Map<String, Command> mapCommand;
   public static Model model;
   ExecutorService executor;
   public static volatile Map<String, PlaneData> planeDataMap;//map from plane id to his plane data

   public Controller() {
      System.out.println("Thread id:" + Thread.currentThread().getId());
      this.mapCommand = new HashMap<>();
      this.executor = Executors.newFixedThreadPool(10);
      OpenServerCommand openServerCommand = new OpenServerCommand();
      openServerCommand.addObserver(this);
      MyHttpServer httpServer = new MyHttpServer();
      httpServer.addObserver(this);
      this.executor.execute(httpServer);
      this.executor.execute(openServerCommand);
      planeDataMap = new HashMap<>();
      model = new Model("FlightFleet",
              "mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority");
//      model.addObserver(this);

   }

   @Override
   public void update(Observable o, Object arg) {
      System.out.println("Thread id:" + Thread.currentThread().getId());
      System.out.println("Controller update, object: " + arg);
      this.addHandler((Runnable) arg);
   }

   private void addHandler(Runnable r){
      System.out.println("add handler, id: " + Thread.currentThread().getId());
      System.out.println("r class: " + r.getClass());
      this.executor.execute(r);
   }
//   private void addCommands(){
//      GetFromDBCommand getFromDBCommand = new GetFromDBCommand();
//      getFromDBCommand.addObserver(this);
//      this.mapCommand.put("getFromDBCommand" , getFromDBCommand );
//      OpenCliCommand openCliCommand = new OpenCliCommand();
//      openCliCommand.addObserver(this);
//      this.mapCommand.put("openCliCommand" , openCliCommand );
//
//   }

   public static FindIterable<Document> getAnalytics(){
      return model.DB.GetPlanes();
   }

   public static FindIterable<Document> getTimeSeries(String id){
      return model.DB.getTSbyPlaneID(id);
   }


   private void openServer(){
      this.executor.execute(new OpenServerCommand());
   }

   public void setPlaneDataValue(String id, PlaneData planeData) {
      planeDataMap.put(id, planeData);
   }
}
//Threadpool;