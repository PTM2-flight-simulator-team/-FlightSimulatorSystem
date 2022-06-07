package Model.Interpreter.Commands;

import java.util.List;
import  Model.Interpreter.*;

public class DefineVarCommand extends AbstractCommand {

    public DefineVarCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
        if(!Utils.isSymbol(args.get(index+1))){
            Utils.setSymbol(args.get(index+1), null);
        }
        return numOfArgs;
    }

    @Override
    public void validParams() {

    }
}
