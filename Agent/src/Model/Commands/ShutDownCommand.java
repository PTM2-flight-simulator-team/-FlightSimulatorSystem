package Model.Commands;

import Model.MyModel;

/**
 * The ShutDownCommand class implements the Command interface.
 */
public class ShutDownCommand implements Command{

    private MyModel model;

    public ShutDownCommand(MyModel model){
        this.model = model;
    }

    /**
     * The execute function is called when the user presses the shutdown command
     */
    @Override
    public void execute() {
        model.modelNotify("ShutDownCommand:");
    }
}
