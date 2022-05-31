package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class DefineVarCommand extends AbstractCommand {

    public DefineVarCommand(Interpreter interpreter) {
        super(interpreter,1);//stam
    }

    @Override
    public void execute(List<String> args, int index) {

    }

    @Override
    public void validParams() {

    }
}
