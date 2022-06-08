package Controller;


import Controller.Commands.Command;
import Model.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
   Map<String, Command> mapCommand;
   Model model;

   public Controller() {
      this.mapCommand = new HashMap<>();
//      model.addObserver(this);
   }

   @Override
   public void update(Observable o, Object arg) {

   }
}
//Threadpool;
