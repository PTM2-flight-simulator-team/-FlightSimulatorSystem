package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public abstract class AbstractCommand implements Command {

    final int numOfArgs;
    Interpreter interpreter;
    public AbstractCommand(Interpreter interpreter, int numOfArgs) {
        this.interpreter = interpreter;
        this.numOfArgs = numOfArgs;
    }

    @Override
    public int execute(List<String> args, int index) {
        return -1;
    }

    @Override
    public void validParams() throws Exception {

    }

}
