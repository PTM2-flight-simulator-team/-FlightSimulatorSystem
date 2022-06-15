package Model;

import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import Model.Interpreter.Interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements Observer {
    private Interpreter interpreter;
    public DataBase DB;
    private  String DbName;
    private String URLconnection;
    private PlaneData plainData;
    public Model(String dbName, String urLconnection) {
        DbName = dbName;
        URLconnection = urLconnection;
        this.interpreter = new Interpreter();
        this.DB = new DataBase(URLconnection, DbName);
        interpreter.addObserver(this);
    }
    public void interpret(String filePath) throws Exception {//execute code
        this.interpreter.interpret(filePath);
    }

    @Override
    public void update(Observable o, Object arg) {//send the commands up to controller
        String passToController = interpreter.getDoCommand();
        setChanged();
        notifyObservers(passToController);
    }

    public void setPlainData(PlaneData planeData) {
        this.plainData = planeData;
        this.setFgVarsInInterpreter(planeData);
    }

    public void setFgVarsInInterpreter(PlaneData data){
        Map<String, Double> FgVars = new HashMap<>();
        for(PlaneVar var: data.getAllVars()){
            FgVars.put(var.getPath(), Double.parseDouble(var.getValue()));
        }
        interpreter.setFGvars(FgVars);
    }
}
