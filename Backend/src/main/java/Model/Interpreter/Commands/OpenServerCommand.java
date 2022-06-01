package Model.Interpreter.Commands;


import Model.Interpreter.Interpreter;

import java.util.List;

public class OpenServerCommand extends AbstractCommand {

    String[] params = new String[2];
    public OpenServerCommand(Interpreter interpreter) {
        super(interpreter,2);// port
    }

    @Override
    public int execute(List<String> args, int index) {
            int port = Integer.parseInt(args.get(index+1));
            int rate = Integer.parseInt(args.get(index+2));
            interpreter.setDoCommand("OpenServer " + port + " " + rate);
            return numOfArgs;
    }
    @Override
    public void validParams() {

    }
}
