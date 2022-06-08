package Model.Interpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Interpreter extends Observable {

    private Map<String, Double> FGvars;
    String code;
    private String doCommand;
    Lexer lexer;
    Parser parser;

    public Interpreter() {
        doCommand = null;
    }

    public void interpret(){
        Utils.start(this);
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
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

        System.out.println(code + "\n");
        List<String> tokens = lexer.lexer(code);
        parser.parse(tokens);
        System.out.println("finish");
    }

    public String getDoCommand() {
        return doCommand;
    }

    public  void setDoCommand(String Command){
        doCommand = Command;
        System.out.println(doCommand);
        setChanged();
        notifyObservers();

    }
    public Map<String, Double> getFGvars() {
        return FGvars;
    }

    public void setFGvars(Map<String, Double> FGvars) {
        this.FGvars = FGvars;
    }

//    public static void main(String[] args)
//    {
//        Map<String,Double> FGvars = new HashMap<>();
//        FGvars.put("/controls/flight/speedbreak", 0.0);
//        FGvars.put("/controls/engines/engine/throttle", 0.0);
//        FGvars.put("/instrumentation/heading-indicator/offset-deg", 0.0);
//        FGvars.put("/instrumentation/airspeed-indicator/indicated-speed-kt", 0.0);
//        FGvars.put("/instrumentation/attitude-indicator/indicated-roll-deg", 0.0);
//        FGvars.put("/controls/flight/rudder", 0.0);
//        FGvars.put("/controls/flight/aileron", 0.0);
//        FGvars.put("/instrumentation/attitude-indicator/internal-pitch-deg", 0.0);
//        FGvars.put("/controls/flight/elevator", 0.0);
//        FGvars.put("/instrumentation/altimeter/indicated-altitude-ft", 0.0);
//
//        Interpreter interpreter = new Interpreter();
//        interpreter.setFGvars(FGvars);
//        Utils.start(interpreter);
//        Lexer lexer = new Lexer();
//        Parser parser = new Parser();
//        String code = "";
//        StringBuilder sb = new StringBuilder("");
//        try {
//            BufferedReader bf = new BufferedReader(new FileReader("C:\\Users\\shaha\\OneDrive\\Documents\\GitHub\\FlightSimulatorSystem\\Backend\\src\\main\\java\\Model\\Interpreter\\Commands\\code.txt"));
//            String line ="";
//            while((line = bf.readLine()) != null){
//                sb.append(line + "\n");
//            }
//            sb.replace(sb.length()-1, sb.length(), "");
//            code = sb.toString();
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
////        ConditionCommand conditionCommand = new ConditionCommand(interpreter);
////        conditionCommand.execute();
////        System.out.println(interpreter.getDoCommand());
////        AssignCommand assignCommand = new AssignCommand(interpreter);
////        assignCommand.execute();
////        System.out.println(interpreter.getDoCommand());
//
//        System.out.println(code + "\n");
//        List<String> tokens = lexer.lexer(code);
//        parser.parse(tokens);
//        System.out.println("finish");
//    }


}
