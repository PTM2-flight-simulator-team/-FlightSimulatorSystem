package Model.Interpreter.Commands;

import java.util.List;

public interface Command{

    public void execute(List<String> args, int index);
    public int getNumOfArgs();
    public void validParams() throws Exception;


}
