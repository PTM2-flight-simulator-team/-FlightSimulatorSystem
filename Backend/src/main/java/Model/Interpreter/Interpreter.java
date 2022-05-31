package Model.Interpreter;

import Model.Interpreter.Commands.AssignCommand;
import Model.Interpreter.Commands.ConditionCommand;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class Interpreter extends Observable {

    private Map<String, Double> FGvars;
    String code;
    String doCommand;
    Lexer lexer;
    Parser parser;
    public Utils utils;

    public Interpreter(Map<String,Double> Fgvars) {
        doCommand = null;
        this.FGvars = Fgvars;
        this.utils = new Utils(this);
    }

    public void interpret(){}

    public String getDoCommand() {
        return doCommand;
    }

    public void setDoCommand(String Command){
        doCommand = Command;
        setChanged();
        notifyObservers();
    }
    public Map<String, Double> getFGvars() {
        return FGvars;
    }

    public void setFGvars(Map<String, Double> FGvars) {
        this.FGvars = FGvars;
    }

    public static void main(String[] args)
    {
        Interpreter interpreter = new Interpreter(new HashMap<>());
        Utils utils = new Utils(interpreter);
        Lexer lexer = new Lexer();
        Parser parser = new Parser(utils);
        String code = "";
        StringBuilder sb = new StringBuilder("");
        try {
            BufferedReader bf = new BufferedReader(new FileReader("C:\\Users\\shaha\\OneDrive\\Documents\\GitHub\\FlightSimulatorSystem\\Backend\\src\\main\\java\\Model\\Interpreter\\Commands\\code.txt"));
            String line ="";
            while((line = bf.readLine()) != null){
                sb.append(line + "\n");
            }
            sb.replace(sb.length()-1, sb.length(), "");
            code = sb.toString();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        ConditionCommand conditionCommand = new ConditionCommand(interpreter);
//        conditionCommand.execute();
//        System.out.println(interpreter.getDoCommand());
//        AssignCommand assignCommand = new AssignCommand(interpreter);
//        assignCommand.execute();
//        System.out.println(interpreter.getDoCommand());

        System.out.println(code + "\n");
        List<String> tokens = lexer.lexer(code);
        AssignCommand assignCommand = new AssignCommand(interpreter);
        assignCommand.execute(tokens, 88);
//        parser.parse(tokens);
        System.out.println("finish");
    }


}
