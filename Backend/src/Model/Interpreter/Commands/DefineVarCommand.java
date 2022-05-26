package Model.Interpreter.Commands;

public class DefineVarCommand extends AbstractCommand {

    public DefineVarCommand() {
        super(0, null);//stam
    }

    @Override
    public void execute(String[] args) {

    }

    @Override
    public int getNumOfArgs() {
        return 0;
    }

    @Override
    public void validParams(String[] args) {

    }
}
