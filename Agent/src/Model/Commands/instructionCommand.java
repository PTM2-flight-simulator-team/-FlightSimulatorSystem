package Model.Commands;

import Model.MyModel;

public class instructionCommand implements Command{
    private MyModel model;
    private String command;

    public instructionCommand(MyModel model){
        this.model = model;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void execute() {
//        model.notifyObservers("instructionCommand:"+command);
        model.modelNotify("instructionCommand:set "+command);
    }
}
