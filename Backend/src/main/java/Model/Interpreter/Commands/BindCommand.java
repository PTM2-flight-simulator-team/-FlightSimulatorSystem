package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;
import  Model.Interpreter.*;

import java.util.List;

public class BindCommand extends AbstractCommand{

    public BindCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
        String tmp = args.get(index +1);
        String bindto = tmp.substring(1,tmp.length()-1);
//        String[] valName = bindto.split("/");
//        bindto = valName[valName.length-1];
        Variable value = new Variable(bindto, interpreter.getFGvars().get(bindto));//create a var set bind to fg var and pull the current value from fg
        Utils.setSymbol(args.get(index-2), value);
        return numOfArgs;
    }
}
