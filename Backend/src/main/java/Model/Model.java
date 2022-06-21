package Model;

import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import Model.Interpreter.Interpreter;

import java.util.*;

public class Model extends Observable implements Observer {

    private static List<Interpreter> interpreters;
    public DataBase DB;
    private  String DbName;
    private String URLconnection;
    public Model(String dbName, String urLconnection) {
        DbName = dbName;
        URLconnection = urLconnection;
        interpreters = new ArrayList<>();
        this.DB = new DataBase(URLconnection, DbName);
    }
    public void interpret(String code, String id, PlaneData data) throws Exception {//execute code
        Interpreter interpreter = new Interpreter(id, data);
        interpreter.addObserver(this);
        interpreters.add(interpreter);
        try {
            interpreter.interpret(code);
        }catch (Exception e){
            System.out.println("unexcepted exception");
            for (int i = 0; i<interpreters.size(); i++){
                if(interpreters.get(i).id.equals(id)){
                    interpreters.remove(i);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {//send the commands up to controller
        List<String> fromInterpreter = (List<String>) arg;
        if(fromInterpreter.get(1).equals("finished")){
            for (int i = 0; i<interpreters.size(); i++){
                if(interpreters.get(i).id.equals(fromInterpreter.get(0))){
                    interpreters.remove(i);
                }
            }
        }else{
            setChanged();
            notifyObservers(arg);
        }
    }

    public void setFgVarsInInterpreter(PlaneData data){
        for (Interpreter i: interpreters){
            if(i.id.equals(data.getID())){
                i.setFGvars(data);
            }
        }
    }
}