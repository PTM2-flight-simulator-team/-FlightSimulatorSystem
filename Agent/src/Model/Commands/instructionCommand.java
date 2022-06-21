package Model.Commands;

import Model.MyModel;

/**
 * This class is a command that will display the instructions for the FG.
 */
public class instructionCommand implements Command{
    private MyModel model;
    private String command;

    public instructionCommand(MyModel model){
        this.model = model;
    }

    /**
     * If the command is not null, then set the command to the converted path
     *
     * @param command the command to be executed
     */
    public void setCommand(String command) { //string != null => path
        String convert = CovertPropertyToPath(command);
        if (CovertPropertyToPath(command) != null) {
            this.command = convert;
        }
        else
            this.command = command;
    }

    /**
     * > If the property contains a slash, it's not a property
     *
     * @param property The property name
     * @return A boolean value.
     */
    public boolean isProperty(String property){
        if(property.contains("/"))
            return false;
        return true;
    }

    /**
     * If the property is a property, then return the path of the property
     *
     * @param property The property to be converted.
     * @return The path of the property.
     */
    public String CovertPropertyToPath(String property){
        if (isProperty(property)){
            String[] data = property.split(" ");
            return model.getProperties().get(data[0]) + " " + data[1];
        }
        return null;
    }

    /**
     * The execute function is called when the user presses the button. It calls the model's modelNotify function, which
     * sends the command to the Backend
     */
    @Override
    public void execute() {
        model.modelNotify("instructionCommand:set "+command);
    }
}
