package Controller;


import CommonClasses.PlaneData;
import Controller.Commands.Command;
import Controller.Commands.OpenServerCommand;
import Controller.ServerConnection.AgentConnections.ClientHandler;
import Controller.ServerConnection.FrontConnection.MyHttpServer;
import Model.Model;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Observer {
   Map<String, Command> mapCommand;
   public static Model model;
   ExecutorService executor;
   public  volatile static Map<String, PlaneData> planeDataMap;//map from plane id to his plane data
   public static volatile HashMap<String, ClientHandler> clientMap;

   public Controller() {
//      System.out.println("Thread id:" + Thread.currentThread().getId());
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
      model.addObserver(this);

   }
   public static PlaneData getPlaneDataById(String pid){
      return planeDataMap.get(pid);
   }

   @Override
   public void update(Observable o, Object arg) {
      if(o instanceof MyHttpServer){// case the data came from the http connection
         List<String> args= (List<String>) arg;
         if(args.get(0).equals("joystick")){// if the data is joystick command
            if(clientMap.containsKey(args.get(1))){
               clientMap.get(args.get(1)).writeToAgent(args.get(2));
            }
         }else if(args.get(0).equals("code")){// if the data is code that need to be interpreted
            try {
               new Thread(()-> {
                  try {
                     model.interpret(args.get(2),args.get(1), planeDataMap.get(args.get(1)));//activate interpreter in separate thread
                  } catch (Exception e) {
                     throw new RuntimeException(e);
                  }
               }).start();
            } catch (Exception e) {
               e.printStackTrace();
            }
         }else if (args.get(0).equals("shutdown")){
            clientMap.get(args.get(1)).writeToAgent("do " + args.get(0));
         }
      }else if(o instanceof Model){// case that the data came from the interpreter ib the model
         List<String> args = (List<String>)arg;
         clientMap.get(args.get(0)).writeToAgent(args.get(1));//write the commands from the interpreter to the agent
      }else if (o instanceof ClientHandler){
         model.setFgVarsInInterpreter((PlaneData) arg);//update the FG in model for pass it to interpreter
         this.planeDataMap.put(((PlaneData) arg).getID(),(PlaneData) arg);
      }else if(o instanceof OpenServerCommand){// case that the data came from the agent server connection
         this.addHandler((Runnable) arg);
         ClientHandler ch = (ClientHandler) arg;
         ch.addObserver(this);
      }
   }

   private void addHandler(Runnable r){
//      System.out.println("add handler, id: " + Thread.currentThread().getId());
//      System.out.println("r class: " + r.getClass());
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
      try {
         return model.DB.GetPlanes();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static List<List<String>> getTimeSeries(String id,int index){
      try {
         return model.DB.getTSbyPlaneID(id, index);
      } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

      }
   }


   private void openServer(){
      this.executor.execute(new OpenServerCommand());
   }

   public void setPlaneDataValue(String id, PlaneData planeData) {
      planeDataMap.put(id, planeData);
   }
}
