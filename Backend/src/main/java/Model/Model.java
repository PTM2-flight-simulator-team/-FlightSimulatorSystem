package Model;

import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import Model.Interpreter.Interpreter;

import java.util.*;

public class Model extends Observable implements Observer {

    private volatile List<Interpreter> interpreters;
    public DataBase DB;
    private  String DbName;
    private String URLconnection;
    public Model(String dbName, String urLconnection) {
        DbName = dbName;
        URLconnection = urLconnection;
        this.interpreters = new ArrayList<>();
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
            for(int i = 0; i<this.interpreters.size(); i++){//remove the interpreter
                if(this.interpreters.get(i).id == id)
                    this.interpreters.remove(i);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        String interpCommand = (String) arg;
        if(interpCommand.equals("finished")){//if the interpreter done the interpretation remove him
            Interpreter interp = (Interpreter) o;
            for(int i = 0; i<this.interpreters.size(); i++){
                if(this.interpreters.get(i) == interp)
                    this.interpreters.remove(i);
            }
        }else {//send the commands up to controller
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