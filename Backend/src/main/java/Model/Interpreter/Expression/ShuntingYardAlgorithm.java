package Model.Interpreter.Expression;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ShuntingYardAlgorithm {

    public static double calc(List<String> exp){
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

        return CalculateExpTree(queue).calculate(null, 0);
    }

    public static Expression CalculateExpTree(LinkedList<String> queue){
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
}
