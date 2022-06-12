package Model.Interpreter.Commands;

import Model.Interpreter.Errors.ScopeException;

import java.util.List;

public interface Command{

    public int execute(List<String> args, int index) throws Exception;


}
