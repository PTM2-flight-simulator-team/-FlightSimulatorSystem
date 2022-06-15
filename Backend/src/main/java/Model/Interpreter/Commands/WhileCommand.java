package Model.Interpreter.Commands;

import Model.Interpreter.Errors.ScopeException;
import Model.Interpreter.Expression.ShuntingYardAlgorithm;
import Model.Interpreter.Interpreter;
import Model.Interpreter.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WhileCommand extends AbstractCommand{
    public WhileCommand(Interpreter interpreter) {super(interpreter,1);}

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
        while (ShuntingYardAlgorithm.ConditionParser(condition) == 1){//check the condition status
            for(int j = 0; j<subarray.size(); j++){//doing the commands
                if(Utils.isCommand(subarray.get(j))){
                    Utils.getCommand(subarray.get(j)).calculate(subarray, j);
                }
            }
        }
        return jumps;
    }
}
