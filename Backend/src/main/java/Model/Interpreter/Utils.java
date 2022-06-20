package Model.Interpreter;

import CommonClasses.PlaneVar;
import Model.Interpreter.Commands.*;
import Model.Interpreter.Expression.ExpressionCommand;
import Model.Interpreter.Interpreter;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public boolean stop = false;
    public volatile Map<String, Variable> symTable = new HashMap<>();
    public Map<String, ExpressionCommand> commands = new HashMap<>();
    public void initialize(Interpreter interpreter) {//initialize commands
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

    public Map<String, Variable> getSymTable() {
        return symTable;
    }

    public Map<String, ExpressionCommand> getCommands() {
        return commands;
    }

    public Variable getSymbol(String sym){
        return symTable.get(sym);
    }

    public boolean isSymbol(String sym){
        return symTable.containsKey(sym);
    }

    public void setSymbol(String sym, Variable value){
        symTable.put(sym, value);
    }

    public ExpressionCommand getCommand(String command){
        return commands.get(command);
    }

    public boolean isCommand(String command){
        return commands.containsKey(command);
    }

}
