package Model.Commands;

import Model.MyModel;

/**
 * > This class is a command that prints the output of a command to a PrintStream
 */
public class PrintStreamCommand implements Command{
    private MyModel model;

    public PrintStreamCommand(MyModel model){
        this.model = model;
    }

    /**
     * The execute function is called by the controller when the user write the printstream
     */
    @Override
    public void execute() {
        model.modelNotify("PrintStreamCommand:");
    }
}
