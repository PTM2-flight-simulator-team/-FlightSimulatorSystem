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
    private PlaneData plainData;
    public Model(String dbName, String urLconnection) {
        DbName = dbName;
        URLconnection = urLconnection;
        interpreters = new ArrayList<>();
        this.DB = new DataBase(URLconnection, DbName);
//        interpreter.addObserver(this);
    }
    public void interpret(String code, String id) throws Exception {//execute code
        Interpreter interpreter = new Interpreter(id);
        interpreter.addObserver(this);
        interpreters.add(interpreter);
        interpreter.interpret(code);
    }

    @Override
    public void update(Observable o, Object arg) {//send the commands up to controller
        setChanged();
        notifyObservers(arg);
    }

    public void setFgVarsInInterpreter(PlaneData data, String id){
        Map<String, Double> FgVars = new HashMap<>();
        for(PlaneVar var: data.getAllVars()){
            System.out.println(var.getPath() + "    " + var.getValue());
            FgVars.put(var.getPath(), Double.parseDouble(var.getValue()));
        }
        for (Interpreter i: interpreters){
            if(i.id.equals(id)){
                i.setFGvars(FgVars);
            }
        }
    }
}