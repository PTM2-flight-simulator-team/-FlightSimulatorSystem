package Model.Interpreter;

import Model.Interpreter.Commands.AbstractCommand;
import Model.Interpreter.Commands.Command;

import java.util.HashMap;
import java.util.Map;

public class Util {

    protected static Map<String, Double> symTable = new HashMap<>();
    protected static Map<String, Command> commands = new HashMap<>();

    public static double getSymbol(String sym){
        return new Double(1).doubleValue();//stam
    }

    public static boolean isSymbol(String sym){

        return false; //stam
    }

    public static void setSymbol(String sym, double value){

    }

    public static Command getCommand(String command){
        return new AbstractCommand(0, "null");//stam
    }

    public static boolean isCommand(String command){

        return false; //stam
    }

    public static void setCommand(String commandname, Command command){

    }

}
