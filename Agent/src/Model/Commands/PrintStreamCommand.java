package Model.Commands;

import Model.MyModel;

public class PrintStreamCommand implements Command{
    private MyModel model;

    public PrintStreamCommand(MyModel model){
        this.model = model;
    }

    @Override
    public void execute() {
//        model.notifyObservers("PrintStreamCommand:");
        model.modelNotify("PrintStreamCommand:");
    }
}
