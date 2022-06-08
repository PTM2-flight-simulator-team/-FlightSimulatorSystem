package Model.Interpreter.Commands;

import Model.Interpreter.Expression.ShuntingYardAlgorithm;
import Model.Interpreter.Interpreter;
import Model.Interpreter.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WhileCommand extends AbstractCommand{
    public WhileCommand(Interpreter interpreter) {super(interpreter,1);}

    @Override
    public int execute(List<String> args, int index) {
        List<String> condition = new LinkedList<>();
        int jumps;
        int i = 1;
        while(!args.get(index+i).equals("{")) {
            condition.add(args.get(index+i));
            i++;
        }
        i++;//to skip "{"
        List<String> subarray = new ArrayList<>();
        while(!args.get(index+i).equals("}")){
            subarray.add(args.get(index+i));
            i++;
        }
        jumps = i;
        jumps++;
        int check = 1;
        System.out.println();
        System.out.println("doing while command:");
        System.out.println();
        while (ShuntingYardAlgorithm.ConditionParser(condition) == 1){
            for(int j = 0; j<subarray.size(); j++){
                if(Utils.isCommand(subarray.get(j))){
                    Utils.getCommand(subarray.get(j)).calculate(subarray, j);
                }
            }
//            check *= 5;
//            Utils.getSymbol("alt").setValue(check);
        }
        return 0;
    }

    @Override
    public void validParams() {

    }
}
