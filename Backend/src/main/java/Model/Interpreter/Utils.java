package Model.Interpreter;

import Model.Interpreter.Commands.*;
import Model.Interpreter.Expression.ExpressionCommand;
import Model.Interpreter.Interpreter;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, Variable> symTable = new HashMap<>();
    public static Map<String, ExpressionCommand> commands = new HashMap<>();
    Interpreter interpreter;

    public static void initialize(Interpreter interpreter) {//initialize commands
        commands.put("condition", new ExpressionCommand(new ConditionCommand(interpreter)));
        commands.put("connect", new ExpressionCommand(new ConnectToServerCommand(interpreter)));
        commands.put("var", new ExpressionCommand(new DefineVarCommand(interpreter)));
        commands.put("while", new ExpressionCommand(new WhileCommand(interpreter)));
        commands.put("openDataServer", new ExpressionCommand(new OpenServerCommand(interpreter)));
        commands.put("print", new ExpressionCommand(new PrintCommand(interpreter)));
        commands.put("bind", new ExpressionCommand(new BindCommand(interpreter)));
        commands.put("sleep", new ExpressionCommand(new SleepCommand(interpreter)));
        commands.put("=", new ExpressionCommand(new AssignCommand(interpreter)));
        commands.put("if", new ExpressionCommand(new ConditionCommand(interpreter)));

    }

    public static Map<String, Variable> getSymTable() {
        return symTable;
    }

    public static Map<String, ExpressionCommand> getCommands() {
        return commands;
    }

    public static Variable getSymbol(String sym){
        return symTable.get(sym);
    }

    public static boolean isSymbol(String sym){
        return symTable.containsKey(sym);
    }

    public static void setSymbol(String sym, Variable value){
        symTable.put(sym, value);
    }

    public static ExpressionCommand getCommand(String command){
        return commands.get(command);
    }

    public static boolean isCommand(String command){
        return commands.containsKey(command);
    }

}
