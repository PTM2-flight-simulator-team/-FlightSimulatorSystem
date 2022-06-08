package Model.Interpreter.Commands;

import java.util.List;
import  Model.Interpreter.*;

public class ConditionCommand extends AbstractCommand{

    public ConditionCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {

        interpreter.setDoCommand("condition");
        return 0;
    }

    @Override
    public void validParams() {

    }
}
