package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class SleepCommand extends AbstractCommand{
    public SleepCommand(Interpreter interpreter) {
        super(interpreter,1);
    }

    @Override
    public void execute(List<String> args, int index) {

    }

    @Override
    public void validParams() {

    }
}
