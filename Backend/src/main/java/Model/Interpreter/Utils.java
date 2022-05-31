package Model.Interpreter;

import Model.Interpreter.Commands.*;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    protected static Map<String, Double> symTable = new HashMap<>();
    protected static Map<String, Command> commands = new HashMap<>();

    public Utils(Interpreter interpreter) {
        commands.put("condition", new ConditionCommand(interpreter));
        commands.put("connect", new ConnectToServerCommand(interpreter));
        commands.put("var", new DefineVarCommand(interpreter));
        commands.put("while", new WhileCommand(interpreter));
        commands.put("openDataServer", new OpenServerCommand(interpreter));
        commands.put("print", new PrintCommand(interpreter));
        commands.put("bind", new BindCommand(interpreter));
        commands.put("sleep", new SleepCommand(interpreter));
        commands.put("=", new AssignCommand(interpreter));
    }

    public double getSymbol(String sym){
        return symTable.get(sym);
    }

    public boolean isSymbol(String sym){
        return symTable.containsKey(sym);
    }

    public void setSymbol(String sym, double value){
        symTable.put(sym, value);
    }

    public  Command getCommand(String command){
        return commands.get(command);
    }

    public boolean isCommand(String command){
        return commands.containsKey(command);
    }

    public  void setCommand(String commandName, Command command){
        commands.put(commandName, command);
    }

}
