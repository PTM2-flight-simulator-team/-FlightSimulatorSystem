package Model.Interpreter.Commands;
import Model.Interpreter.*;

import java.util.List;
import  Model.Interpreter.*;

public class SleepCommand extends AbstractCommand{
    public SleepCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
        try {
            Thread.sleep(Integer.parseInt(args.get(index+1)));
        }catch (InterruptedException e) {}
        return 1;
    }
}
