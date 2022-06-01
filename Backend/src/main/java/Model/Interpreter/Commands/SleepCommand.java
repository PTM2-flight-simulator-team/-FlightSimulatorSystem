package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class SleepCommand extends AbstractCommand{
    public SleepCommand(Interpreter interpreter) {
        super(interpreter,1);
    }

    @Override
    public int execute(List<String> args, int index) {
        return 0;
    }

    @Override
    public void validParams() {

    }
}
