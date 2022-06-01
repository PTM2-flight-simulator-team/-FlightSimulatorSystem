package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class DefineVarCommand extends AbstractCommand {

    public DefineVarCommand(Interpreter interpreter) {
        super(interpreter,1);//stam
    }

    @Override
    public int execute(List<String> args, int index) {
        if(!interpreter.utils.isSymbol(args.get(index+1))){
            interpreter.utils.setSymbol(args.get(index+1), null);
        }
        return numOfArgs;
    }

    @Override
    public void validParams() {

    }
}
