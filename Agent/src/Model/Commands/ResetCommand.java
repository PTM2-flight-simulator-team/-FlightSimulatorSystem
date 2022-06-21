package Model.Commands;

import Model.MyModel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * > This class implements the Command interface and is used to reset the FG
 */
public class ResetCommand implements Command{

    private MyModel model;

    public ResetCommand(MyModel model){
        this.model = model;
    }

    /**
     * It reads a file, parses the data, and then executes the commands
     */
    @Override
    public void execute() {
        try {
            Scanner in = new Scanner(new FileReader("Agent/src/Model/Commands/resetFile.txt"));
            String line;
            while(in.hasNextLine())
            {
                line = in.nextLine();
                String[] data = line.split(",");
                String property = data[0];
                double value = Double.parseDouble(data[1]);
                instructionCommand ic = new instructionCommand(this.model);
                ic.setCommand(this.model.getProperties().get(property) + " " + value);
                ic.execute();
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
