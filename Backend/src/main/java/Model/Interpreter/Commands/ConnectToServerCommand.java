package Model.Interpreter.Commands;

import java.util.List;
import  Model.Interpreter.*;

public class ConnectToServerCommand extends AbstractCommand {

    public ConnectToServerCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
        int port =  Integer.parseInt(args.get(index + 2));
        interpreter.setDoCommand("Connect " + args.get(index+1) + " " + port);//send set connection to the Model
        return numOfArgs;
    }
}
