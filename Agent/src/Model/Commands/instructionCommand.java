package Model.Commands;

import Model.MyModel;

public class instructionCommand implements Command{
    private MyModel model;
    private String command;

    public instructionCommand(MyModel model){
        this.model = model;
    }

    public void setCommand(String command) { //string != null => path
        String convert = CovertPropertyToPath(command);
        if (CovertPropertyToPath(command) != null) {
            this.command = command;
        }
        else
            this.command = convert;
    }

    public boolean isProperty(String property){
        // aileron -> /controls/dmn,ads.mdas
        if(property.contains("/"))
            return false;
        return true;
    }

    public String CovertPropertyToPath(String property){
        if (isProperty(property)){
            return model.getProperties().get(property);
        }
        return null;
    }

    @Override
    public void execute() {
//        model.notifyObservers("instructionCommand:"+command);
        model.modelNotify("instructionCommand:set "+command);
    }
}
