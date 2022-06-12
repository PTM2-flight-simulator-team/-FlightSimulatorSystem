package Model.Interpreter.Commands;

import Model.Interpreter.Errors.NullVar;
import Model.Interpreter.Expression.ShuntingYardAlgorithm;
import Model.Interpreter.Interpreter;
import Model.Interpreter.Utils;
import Model.Interpreter.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssignCommand extends AbstractCommand {

    public AssignCommand(Interpreter interpreter) {super(interpreter,1);
    }

    @Override
    public int execute(List<String> args, int index) throws Exception {
        if(args.get(index+1).equals("bind"))
            return 0;
        int i = 1;
        List<String> rightExpression = new ArrayList<>();
        while(!Utils.isCommand(args.get(index + i)) && !args.get(index + i).equals("\n")){//insert all the expression after the '=' to rightExpression
            String tmp = args.get(index+i);
            String[] toadd = tmp.split("(?<=[-+*/()])|(?=[-+*/()])");
            int size = tmp.length();
            rightExpression.addAll(Arrays.stream(toadd).toList());
            i++;
        }
        for(int e = 0; e<rightExpression.size();e++){//if one of the expression in rightExpression in var replace him with his value
            if(Utils.isSymbol(rightExpression.get(e))){
                if(Utils.getSymbol(rightExpression.get(e)) == null){
                    throw new NullVar(rightExpression.get(e));
                }
                String exp = Double.toString(Utils.getSymbol(rightExpression.get(e)).getValue());
                rightExpression.set(e, exp);
            }
        }
        if(!Utils.isSymbol(args.get(index-1))){//case: assign first time local var
            Utils.setSymbol(args.get(index-1), new Variable(ShuntingYardAlgorithm.calc(rightExpression)));
        }else {
            if(Utils.getSymbol(args.get(index-1)).getBindTo() == null){//case: change value of local var
                Utils.getSymbol(args.get(index-1)).setValue(ShuntingYardAlgorithm.calc(rightExpression));
            }else {//case: change FG var values
                Utils.getSymbol(args.get(index-1)).setValue(ShuntingYardAlgorithm.calc(rightExpression));
                interpreter.setDoCommand("set" + " " + Utils.getSymbol(args.get(index-1)).getBindTo() + " " +
                        Utils.getSymbol(args.get(index-1)).getValue());//change the value on the FlightGear
            }
        }
        i -= 1;//i moving forward one extra time
        return i;// returning the num of jumps in args list
    }
}
