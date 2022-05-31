package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;

import java.util.List;

public class AssignCommand extends AbstractCommand {

    public AssignCommand(Interpreter interpreter) {
        super(interpreter,2);
    }

    @Override
    public void execute(List<String> args, int index) {
        interpreter.setDoCommand("Assign");
    }
}
