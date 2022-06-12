package Model.Interpreter.Expression;

import java.util.List;

public class ConditionExpression extends BinaryExpression{

    String operator;
    public ConditionExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double calculate(List<String> args, int index) throws Exception {//case to each operator
        switch (operator){
            case "<":{
                if(left.calculate(null,0) < right.calculate(null, 0))
                    return 1;
                else
                    return 0;
            }
            case "<=":{
                if(left.calculate(null,0) <= right.calculate(null, 0))
                    return 1;
                else
                    return 0;
            }
            case ">":{
                if(left.calculate(null,0) > right.calculate(null, 0))
                    return 1;
                else
                    return 0;
            }
            case ">=":{
                if(left.calculate(null,0) >= right.calculate(null, 0))
                    return 1;
                else
                    return 0;
            }
            case "==":{
                if(left.calculate(null,0) == right.calculate(null, 0))
                    return 1;
                else
                    return 0;
            }
        }
        return -10;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
