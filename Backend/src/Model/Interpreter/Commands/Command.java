package Model.Interpreter.Commands;

public interface Command {

    public void execute(String args[]);
    public int getNumOfArgs();
    public void validParams(String args[]);

}
