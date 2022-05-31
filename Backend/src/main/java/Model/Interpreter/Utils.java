package Model.Interpreter;

import Model.Interpreter.Commands.*;
import Model.Interpreter.Expression.ExpressionCommand;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    protected static Map<String, Variable> symTable = new HashMap<>();
    protected static Map<String, ExpressionCommand> commands = new HashMap<>();

    public Utils(Interpreter interpreter) {
        commands.put("condition", new ExpressionCommand(new ConditionCommand(interpreter)));
        commands.put("connect", new ExpressionCommand(new ConnectToServerCommand(interpreter)));
        commands.put("var", new ExpressionCommand(new DefineVarCommand(interpreter)));
        commands.put("while", new ExpressionCommand(new WhileCommand(interpreter)));
        commands.put("openDataServer", new ExpressionCommand(new OpenServerCommand(interpreter)));
        commands.put("print", new ExpressionCommand(new PrintCommand(interpreter)));
        commands.put("bind", new ExpressionCommand(new BindCommand(interpreter)));
        commands.put("sleep", new ExpressionCommand(new SleepCommand(interpreter)));

//        symTable.put("h0", new Variable(null, 100));
//        symTable.put("heading", new Variable("/instrumentation/heading-indicator/offset-deg", 1000));
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

    public  ExpressionCommand getCommand(String command){
        return commands.get(command);
    }

    public boolean isCommand(String command){
        return commands.containsKey(command);
    }

    public  void setCommand(String commandName, ExpressionCommand command){
        commands.put(commandName, command);
    }

}
