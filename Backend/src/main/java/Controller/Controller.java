package Controller;


import CommonClasses.PlainData;
import Controller.Commands.Command;
import Controller.Commands.GetFromDBCommand;
import Controller.Commands.OpenCliCommand;
import Controller.Commands.OpenServerCommand;
import Model.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Observer {
   Map<String, Command> mapCommand;
   Model model;
   ExecutorService executor;
   public static volatile Map<String, PlainData> plainDataMap;

   public Controller() {
      System.out.println("Thread id:" + Thread.currentThread().getId());
      this.mapCommand = new HashMap<>();
      this.executor = Executors.newFixedThreadPool(10);
     OpenServerCommand openServerCommand = new OpenServerCommand();
     openServerCommand.addObserver(this);
     this.executor.execute(openServerCommand);
     plainDataMap = new HashMap<>();
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
//      OpenServerCommand openServer = new OpenServerCommand();
//      //openServer.addObserver(this);
////      GetFromDBCommand getFromDBCommand = new GetFromDBCommand();
////      getFromDBCommand.addObserver(this);
////      OpenCliCommand openCliCommand = new OpenCliCommand();
////      openCliCommand.addObserver(this);
//
//   }
   private void openServer(){
      this.executor.execute(new OpenServerCommand());
   }


}
//Threadpool;
