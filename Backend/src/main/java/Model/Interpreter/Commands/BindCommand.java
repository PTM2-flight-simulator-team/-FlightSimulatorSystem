package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;
import Model.Interpreter.Variable;

import java.util.List;

public class BindCommand extends AbstractCommand{

    public BindCommand(Interpreter interpreter) {
        super(interpreter,1);
    }

    @Override
    public int execute(List<String> args, int index) {
        Variable value = new Variable(args.get(index +1), interpreter.getFGvars().get(args.get(index + 1)));//create a var set bind to fg var and pull the current value from fg
        interpreter.utils.setSymbol(args.get(index-2), value);
        return numOfArgs;
    }

    @Override
    public void validParams() {

    }
}
