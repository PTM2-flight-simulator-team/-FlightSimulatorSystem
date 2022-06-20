package Model.Interpreter.Commands;

import Model.Interpreter.Errors.ScopeException;
import Model.Interpreter.Expression.ShuntingYardAlgorithm;
import Model.Interpreter.Interpreter;
import Model.Interpreter.Utils;
import Model.Interpreter.Variable;
import Model.Model;

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
            if(args.get(index+i).contains("(")){
                String tmp = args.get(index+i).substring(1);
                if(args.get(index + i).contains("(")){
                    args.set(index+i, tmp);
                }
                StringBuilder sb = new StringBuilder();
                while (!args.get(index+i).equals(")")) {
                    if(args.get(index + i).contains(")")){
                        tmp = args.get(index+i).substring(0,args.get(index + i).length()-1);
                        args.set(index+i, tmp);
                        sb.append(args.get(index+i));
                        break;
                    }
                    sb.append(args.get(index+i));
                    sb.append(" ");
                    i++;
                }
                condition.add(sb.toString());
            }else
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
        System.out.println("doing while command:");
        while (ShuntingYardAlgorithm.ConditionParser(condition, this.interpreter) == 1 && !interpreter.parser.stop){//check the condition status
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
