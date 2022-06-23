package Model.Commands;

import Model.MyModel;

/**
 * > This class is a command that sends analytic data to a Bckend
 */
public class AnalyticSenderCommand implements Command{
    private MyModel model;

    public AnalyticSenderCommand(MyModel model){
        this.model = model;
    }

    /**
     * The function is called by the controller when the user clicks the "AnalyticSender" button
     */
    @Override
    public void execute() {
        model.modelNotify("AnalyticSenderCommand:");
    }
}
