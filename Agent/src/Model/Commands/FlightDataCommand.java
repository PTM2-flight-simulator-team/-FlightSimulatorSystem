package Model.Commands;

import Model.MyModel;

/**
 * > The `FlightDataCommand` class implements the `Command` interface
 */
public class FlightDataCommand implements Command{
    private MyModel model;

    public FlightDataCommand(MyModel model){
        this.model = model;
    }

    /**
     * The execute function is called when the user clicks the FlightDataCommand button
     */
    @Override
    public void execute() {
        model.modelNotify("FlightDataCommand:");
    }
}
