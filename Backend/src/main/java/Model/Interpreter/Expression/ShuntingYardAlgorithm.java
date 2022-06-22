package Model.Interpreter.Expression;

import Model.Interpreter.Interpreter;
import Model.Interpreter.Utils;

import java.util.*;

public class ShuntingYardAlgorithm {

    public static double calc(List<String> exp) throws Exception {//creating calculate tree
        LinkedList<String> queue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        int size = exp.size();

        for(int i = 0; i<size; i++){
            switch (exp.get(i)) {
                case "+":
                case "-": {
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        queue.addLast(stack.pop());
                    }
                    stack.push(exp.get(i));
                    break;
                }
                case "(":
                case "/":
                case "*": {
                    stack.push(exp.get(i));
                    break;
                }
                case ")": {
                    while (!stack.isEmpty() && !stack.peek().equals("(")) queue.addLast(stack.pop());
                    stack.pop();
                    break;
                }
                default: {
                    queue.addLast(exp.get(i));
                    break;
                }
            }
        }
        while (!stack.isEmpty()){
            queue.addLast(stack.pop());
        }

        return CalculateExpTree(queue).calculate(null, 0);//calculate the result and return it
    }

    public static Expression CalculateExpTree(LinkedList<String> queue){//calculate the tree
        Expression ret;
        if(queue.isEmpty())
            return new Number(0);
        String currentExp = queue.pollLast();

        switch (currentExp){
            case "+":{
                Expression right = CalculateExpTree(queue);
                Expression left = CalculateExpTree(queue);
                ret = new Plus(left, right);
                break;
            }
            case "-":{
                Expression right = CalculateExpTree(queue);
                Expression left = CalculateExpTree(queue);
                ret = new Minus(left, right);
                break;
            }
            case "*":{
                Expression right = CalculateExpTree(queue);
                Expression left = CalculateExpTree(queue);
                ret = new Mul(left, right);
                break;
            }
            case "/":{
                Expression right = CalculateExpTree(queue);
                Expression left = CalculateExpTree(queue);
                ret = new Div(left, right);
                break;
            }
            default:{
                ret = new Number(Double.parseDouble(currentExp));
                break;
            }
        }

        return ret;
    }
    public static double ConditionParser(List<String> conditionExp, Interpreter interpreter) throws Exception {//creating the condition tree
        LinkedList<String> queue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        int size = conditionExp.size();
        String tmp ="";
        List<String> condition = new ArrayList<>();
        HashSet<String> operators = new HashSet<>();
        operators.add(">");
        operators.add("<");
        operators.add(">=");
        operators.add("<=");
        operators.add("==");
        for(int i = 0; i<size; i++){
            if (!operators.contains(conditionExp.get(i))){
                List<String> exp = new ArrayList<>();
                if(conditionExp.get(i).contains(" ")){
                    exp = Arrays.asList(conditionExp.get(i).split(" "));
                    for(int j = 0; j< exp.size(); j++){
                        if(interpreter.utils.isSymbol(exp.get(j)))
                            exp.set(j, String.valueOf(interpreter.utils.getSymbol(exp.get(j)).getValue()));
                    }
                }else{
                    if(interpreter.utils.isSymbol(conditionExp.get(i)))
                        exp.add(String.valueOf(interpreter.utils.getSymbol(conditionExp.get(i)).getValue()));
                    else
                        exp.add(conditionExp.get(i));
                }
//                while (i<size && !operators.contains(conditionExp.get(i))){
//                    if(Utils.isSymbol(conditionExp.get(i))) {
//                        exp.add(String.valueOf(Utils.getSymbol(conditionExp.get(i)).getValue()));
//                    }else
//                        exp.add(conditionExp.get(i));
//                    i++;
//                }
                condition.add(String.valueOf(ShuntingYardAlgorithm.calc(exp)));
            }
            else
                condition.add(conditionExp.get(i));
//            if(Utils.isSymbol(conditionExp.get(i))) {
//                tmp = String.valueOf(Utils.getSymbol(conditionExp.get(i)).getValue());
//            }else
//                tmp = conditionExp.get(i);
//            condition.add(tmp);
        }
//        System.out.println(condition);
        size = condition.size();
        for(int i = 0; i<size; i++){
            switch (condition.get(i)) {
                case ">":
                case ">=":
                case "<":
                case "<=":
                case "==": {
                    stack.push(condition.get(i));
                    break;
                }
                case "&&":
                case "||":{
                    while (!stack.isEmpty() && operators.contains(stack.peek())) {
                        queue.addLast(stack.pop());
                    }
                    stack.push(condition.get(i));
                    break;
                }
                default: {
                    queue.addLast(condition.get(i));
                    break;
                }
            }
        }
        while (!stack.isEmpty()){
            queue.addLast(stack.pop());
        }
        Expression check = CheckCondition(queue);
        return check.calculate(null,0);
    }
    public static Expression CheckCondition(LinkedList<String> queue){//check the condition returning 0 case that false else 1
        Expression ret;
        if(queue.isEmpty())
            return new Number(0);
        String currentExp = queue.pollLast();

        switch (currentExp){
            case ">":
            case ">=":
            case "<":
            case "<=":
            case "==": {
                Expression right = CheckCondition(queue);
                Expression left = CheckCondition(queue);
                ConditionExpression tmp = new ConditionExpression(left, right);
                tmp.setOperator(currentExp);
                ret = tmp;
                break;
            }
            case "&&":{
                Expression right = CheckCondition(queue);
                Expression left = CheckCondition(queue);
                ret = new And(left, right);
                break;
            }
            case "||":{
                Expression right = CheckCondition(queue);
                Expression left = CheckCondition(queue);
                ret = new Or(left, right);
                break;
            }
            default:{
                ret = new Number(Double.parseDouble(currentExp));
                break;
            }
        }

        return ret;

    }
}
