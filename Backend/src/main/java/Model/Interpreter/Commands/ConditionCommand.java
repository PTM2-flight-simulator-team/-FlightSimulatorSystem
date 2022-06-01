package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class ConditionCommand extends AbstractCommand{

    public ConditionCommand(Interpreter interpreter) {
        super(interpreter,0);//stam
    }

    @Override
    public int execute(List<String> args, int index) {

        interpreter.setDoCommand("condition");
        return 0;
    }

    @Override
    public void validParams() {

    }
}
