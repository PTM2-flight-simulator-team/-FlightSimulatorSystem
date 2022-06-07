package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class ConnectToServerCommand extends AbstractCommand {

    public ConnectToServerCommand(Interpreter interpreter) {
        super(interpreter,2);//ip port
    }

    @Override
    public int execute(List<String> args, int index) {
        int port =  Integer.parseInt(args.get(index + 2));
        interpreter.setDoCommand("Connect " + args.get(index+1) + " " + port);
        return numOfArgs;
    }

    @Override
    public void validParams() {

    }
}
