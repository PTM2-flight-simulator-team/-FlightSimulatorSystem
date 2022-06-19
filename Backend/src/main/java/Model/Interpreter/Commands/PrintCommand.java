package Model.Interpreter.Commands;

import java.util.List;
import  Model.Interpreter.*;

public class PrintCommand extends AbstractCommand{

    public PrintCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
        if(interpreter.utils.isSymbol(args.get(index + 1))){
            System.out.println(interpreter.utils.getSymbol(args.get(index+1)).getValue());//print var value
        }else{
            String arg = args.get(index+1);
            String toPrint = "";
            for(int i = 0; i<arg.length(); i++){//replace '"' for print
                if(arg.charAt(i) != '"'){
                    toPrint += arg.charAt(i);
                }
            }
            System.out.println(toPrint);
        }
        return 1;
    }
}
