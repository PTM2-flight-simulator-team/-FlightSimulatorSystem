package Model.Interpreter.Commands;

public class AbstractCommand implements Command{

    final int numOfArgs;
    final String typeofArgs;

    public AbstractCommand(int numOfArgs, String type) {
        this.numOfArgs = numOfArgs;
        this.typeofArgs = type;
    }

    @Override
    public void execute(String[] args) {

    }

    @Override
    public int getNumOfArgs() {
        return 0;
    }

    @Override
    public void validParams(String[] args) {

    }
}
