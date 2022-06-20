package Model.Interpreter.Commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import  Model.Interpreter.*;
import Model.Interpreter.Errors.ScopeException;
import Model.Interpreter.Expression.ShuntingYardAlgorithm;

public class ConditionCommand extends AbstractCommand{

    public ConditionCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) throws Exception {
        List<String> condition = new LinkedList<>();
        int jumps;
        int i = 1;
        while(!args.get(index+i).equals("{")) {//insert to condition the condition expression
            condition.add(args.get(index+i));
            i++;
        }
        i++;//to skip "{"
        List<String> subarray = new ArrayList<>();
        while(!args.get(index+i).equals("}")){//insert to subarray the commands in loop scope
            subarray.add(args.get(index+i));
            i++;
            if((index+i) == args.size()){
                throw new ScopeException();
            }
        }
        jumps = i;
        System.out.println();
        System.out.println("doing while command:");
        System.out.println();
        if (ShuntingYardAlgorithm.ConditionParser(condition, this.interpreter) == 1){//check the condition status
            interpreter.updateSymTabale();//update the binds vars in symTable to the current value in the flight gear
            for(int j = 0; j<subarray.size(); j++){//doing the commands
                if(interpreter.utils.isCommand(subarray.get(j))){
                    interpreter.utils.getCommand(subarray.get(j)).calculate(subarray, j);
                }
            }
        }
        return jumps;
    }
}
