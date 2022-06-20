package Model.Interpreter.Commands;

import java.util.List;
import  Model.Interpreter.*;
import Model.Interpreter.Errors.VarIsAlreadyDefined;

public class DefineVarCommand extends AbstractCommand {

    public DefineVarCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) throws VarIsAlreadyDefined {
        if(!interpreter.utils.isSymbol(args.get(index+1))){
            interpreter.utils.setSymbol(args.get(index+1), null);//define the var at the Utils symTable
        }else {
            throw new VarIsAlreadyDefined(args.get(index+1));
        }
        return numOfArgs;
    }
}
