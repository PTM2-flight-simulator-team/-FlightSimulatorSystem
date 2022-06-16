package Model.Interpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Interpreter extends Observable {

    private Map<String, Double> FGvars;
    private String doCommand;
    Lexer lexer;
    Parser parser;
    public String id;

    public Interpreter(String id) {
        this.id = id;
        doCommand = null;
        this.lexer = new Lexer();
        this.parser = new Parser();
    }

    public void interpret(String code) throws Exception {
        Utils.initialize(this);//initialize Utils commands map
        System.out.println(code + "\n");
        List<String> tokens = lexer.lexer(code);//turn code string to tokens
        parser.parse(tokens);//tokens to commands
        setDoCommand("finished");
        System.out.println("finish");
    }

    public  void setDoCommand(String Command){//pass the commands to the Model
        doCommand = Command;
        List<String> args = new ArrayList<>();
        args.add(id);
        args.add(doCommand);
        setChanged();
        notifyObservers(args);

    }
    public Map<String, Double> getFGvars() {
        return FGvars;
    }//get the FG data

    public void setFGvars(Map<String, Double> FGvars) {
        this.FGvars = FGvars;
    }///set the FG data


}
