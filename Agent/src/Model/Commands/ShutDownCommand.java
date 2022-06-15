package Model.Commands;

import Model.MyModel;

public class ShutDownCommand implements Command{

    private MyModel model;

    public ShutDownCommand(MyModel model){
        this.model = model;
    }

    @Override
    public void execute() {
//        model.notifyObservers("ShutDownCommand:");
        model.modelNotify("ShutDownCommand:");
    }
}
