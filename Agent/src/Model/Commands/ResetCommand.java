package Model.Commands;

import Model.MyModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ResetCommand implements Command{

    private MyModel model;

    public ResetCommand(MyModel model){
        this.model = model;
    }

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
