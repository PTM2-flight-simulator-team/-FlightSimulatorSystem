package Controller;


import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import Controller.Commands.Command;
import Controller.Commands.GetFromDBCommand;
import Controller.Commands.OpenCliCommand;
import Controller.Commands.OpenServerCommand;
import Controller.ServerConnection.AgentConnections.ClientHandler;
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
   public static volatile HashMap<String, ClientHandler> clientMap;

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
      clientMap = new HashMap<>();
      model = new Model("FlightFleet",
              "mongodb+srv://fleetManagement:r7uRtk!ytxGbVrR@flightfleet.aerzo.mongodb.net/?retryWrites=true&w=majority");
//      model.addObserver(this);

   }

   @Override
   public void update(Observable o, Object arg) {
      System.out.println("Thread id:" + Thread.currentThread().getId());
      System.out.println("Controller update, object: " + arg);
      System.out.println(o.getClass() + " in controller update");
      if(o instanceof MyHttpServer){// case the data came from the http connection
         List<String> args= (List<String>) arg;
         if(args.get(0).equals("joystick")){// if the data is joystick command
            if(clientMap.containsKey(args.get(1))){
               clientMap.get(args.get(1)).writeToAgent(args.get(2));
            }
         }else if(args.get(0).equals("code")){// if the data is code that need to be interpreted
            try {
               clientMap.get(args.get(1)).activeInterpreter = true;
               model.interpret(args.get(2),args.get(1));
            } catch (Exception e) {
               e.printStackTrace();
            }
         }else if(args.get(0).equals("shutdown")){
            clientMap.get(args.get(1)).writeToAgent("do " + args.get(0));
         }
      }else if(o instanceof Model){// case that the data came from the interpreter ib the model
         List<String> args = (List<String>)arg;
         if(args.get(1).equals("finished")){
            clientMap.get(args.get(0)).activeInterpreter = false;
         }else
            clientMap.get(args.get(0)).writeToAgent(args.get(1));
      }else if (o instanceof ClientHandler){
         ClientHandler client = (ClientHandler) o;
         if(client.activeInterpreter){
               model.setFgVarsInInterpreter((PlaneData) arg, client.getID());
         }
      }else if(o instanceof OpenServerCommand){// case that the data came from the agent server connection
         this.addHandler((Runnable) arg);
      }
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