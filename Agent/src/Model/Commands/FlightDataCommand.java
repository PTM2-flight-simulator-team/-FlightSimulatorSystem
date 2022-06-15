package Model.Commands;

import Model.MyModel;

public class FlightDataCommand implements Command{
    private MyModel model;

    public FlightDataCommand(MyModel model){
        this.model = model;
    }

    @Override
    public void execute() {
        model.modelNotify("FlightDataCommand:");
    }
}
