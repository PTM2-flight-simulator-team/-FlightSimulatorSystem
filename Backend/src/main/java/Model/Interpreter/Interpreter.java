package Model.Interpreter;

import CommonClasses.PlaneData;
import Model.Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Interpreter extends Observable {
    public volatile PlaneData FGvars;
    public volatile boolean stop = false;
    private String doCommand;
    public Utils utils;
    Lexer lexer;
    public Parser parser;
    public String id;

    public Interpreter(String id, PlaneData fg) {
        this.id = id;
        doCommand = null;
        FGvars = fg;
        this.lexer = new Lexer();
        this.parser = new Parser();
        this.utils = new Utils();
    }

    public void interpret(String code) throws Exception {
        utils.initialize(this);//initialize Utils commands map
        List<String> tokens = lexer.lexer(code);//turn code string to tokens
        parser.parse(tokens, this);//tokens to commands
        System.out.println("interpreter " + id + " is finished");
        setDoCommand("finished");
    }

    public  void setDoCommand(String Command){//pass the commands to the Model
        doCommand = Command;
        List<String> args = new ArrayList<>();
        args.add(id);
        args.add(doCommand);
        setChanged();
        notifyObservers(args);

    }
    public void setFGvars(PlaneData fGvars) {
        this.FGvars= fGvars;
    }///set the FG data


    public PlaneData getFGvars() {
        return this.FGvars;
    }

    public void updateSymTabale() {
        for (String key : this.utils.symTable.keySet()) {//iterate over the symTable and update all the bind vars
            if (this.utils.symTable.get(key) != null && this.utils.symTable.get(key).getBindTo() != null) {
                this.utils.symTable.put(key, new Variable(this.utils.symTable.get(key).getBindTo(), this.FGvars.getValueByPath(this.utils.symTable.get(key).getBindTo())));
            }
        }
    }
}
