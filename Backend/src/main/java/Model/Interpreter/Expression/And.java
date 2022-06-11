package Model.Interpreter.Expression;

import java.util.List;

public class And extends BinaryExpression{

    public And(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double calculate(List<String> args, int index) throws Exception {
        if((left.calculate(args, index) == 1) &&(right.calculate(args,0) == 1))
            return 1;
        return 0;
    }
}
