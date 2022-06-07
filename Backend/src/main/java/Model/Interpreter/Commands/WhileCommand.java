package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class WhileCommand extends AbstractCommand{
    public WhileCommand(Interpreter interpreter) {
        super(interpreter,0);
    }

    @Override
    public int execute(List<String> args, int index) {
        return 0;
    }

    @Override
    public void validParams() {

    }
}
