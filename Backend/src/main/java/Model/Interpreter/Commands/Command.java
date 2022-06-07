package Model.Interpreter.Commands;

import java.util.List;

public interface Command{

    public int execute(List<String> args, int index);
    public int getNumOfArgs();
    public void validParams() throws Exception;


}
