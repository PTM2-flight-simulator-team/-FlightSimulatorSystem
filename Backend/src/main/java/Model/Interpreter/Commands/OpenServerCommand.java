package Model.Interpreter.Commands;


import Model.Interpreter.Interpreter;

import java.util.List;

public class OpenServerCommand extends AbstractCommand {

    public OpenServerCommand(Interpreter interpreter) {
        super(interpreter,2);// port
    }

    @Override
    public void execute(List<String> args, int index) {

    }
    @Override
    public void validParams() {

    }
}
