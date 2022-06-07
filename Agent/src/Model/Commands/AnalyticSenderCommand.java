package Model.Commands;

import Model.MyModel;

public class AnalyticSenderCommand implements Command{
    private MyModel model;

    public AnalyticSenderCommand(MyModel model){
        this.model = model;
    }

    @Override
    public void execute() {
        // send the special data like max speed or regular timeseries data ?
//        model.notifyObservers("PrintStreamCommand:");
        model.modelNotify("AnalyticSenderCommand:");
    }
}
