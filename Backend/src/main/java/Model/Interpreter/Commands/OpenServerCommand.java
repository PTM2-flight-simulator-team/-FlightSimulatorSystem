package Model.Interpreter.Commands;


import java.util.List;
import  Model.Interpreter.*;

public class OpenServerCommand extends AbstractCommand {
    public OpenServerCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
            int port = Integer.parseInt(args.get(index+1));
            int rate = Integer.parseInt(args.get(index+2));
            interpreter.setDoCommand("OpenServer " + port + " " + rate);//send set open server to the Model
            return numOfArgs;
    }
}
