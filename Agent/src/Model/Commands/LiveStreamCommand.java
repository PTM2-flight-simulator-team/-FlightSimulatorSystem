package Model.Commands;

import Model.MyModel;

public class LiveStreamCommand implements Command{

    private MyModel model;
    private String command;

    public LiveStreamCommand(MyModel model,String command){
        this.model = model;
        this.command = command;
    }

    @Override
    public void execute() {

    }
}
