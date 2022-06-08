package Model.Interpreter.Commands;

import java.util.List;

public interface Command{

    public int execute(List<String> args, int index);

    public void validParams() throws Exception;


}
