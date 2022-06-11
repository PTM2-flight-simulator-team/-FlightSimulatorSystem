package Model.Interpreter.Expression;

import java.util.List;

public class Or extends BinaryExpression{
    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double calculate(List<String> args, int index) throws Exception {
        if((left.calculate(args, index) == 0) &&(right.calculate(args,0) == 0))
            return 0;
        return 1;
    }
}
