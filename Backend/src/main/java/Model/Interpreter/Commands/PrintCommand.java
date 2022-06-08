package Model.Interpreter.Commands;

import java.util.List;
import  Model.Interpreter.*;

public class PrintCommand extends AbstractCommand{

    public PrintCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
        if(Utils.isSymbol(args.get(index + 1))){
            System.out.println(Utils.getSymbol(args.get(index+1)).getValue());
        }else
            System.out.println(args.get(index+1));
        return 1;
    }

    @Override
    public void validParams() {

    }
}
