package Model.Interpreter.Commands;

import Model.Interpreter.Interpreter;
import Model.Interpreter.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;

public class AssignCommand extends AbstractCommand {

    public AssignCommand(Interpreter interpreter) {
        super(interpreter,1);
    }

    @Override
    public int execute(List<String> args, int index) {
        if(args.get(index+1).equals("bind"))
            return 0;
        int i = 1;
        List<String> rightExpression = new ArrayList<>();
        while(!interpreter.utils.isCommand(args.get(index + i)) && !args.get(index + i).equals("\n")){
            String tmp = args.get(index+i);
            String[] toadd = tmp.split("(?<=[-+*/()])|(?=[-+*/()])");
            int size = tmp.length();
            rightExpression.addAll(Arrays.stream(toadd).toList());
            i++;
        }
        for(int e = 0; e<rightExpression.size();e++){
            if(interpreter.utils.isSymbol(rightExpression.get(e))){
                String exp = Double.toString(interpreter.utils.getSymbol(rightExpression.get(e)).getValue());
                rightExpression.set(e, exp);
            }
        }

        return 0;
    }
}
