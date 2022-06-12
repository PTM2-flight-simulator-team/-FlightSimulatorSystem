package Model.Interpreter.Expression;

import java.util.List;

public class Minus extends BinaryExpression {

	public Minus(Expression left, Expression right) {
		super(left, right);
	}

	@Override
	public double calculate(List<String> args, int index) throws Exception {
		return left.calculate(args,index)-right.calculate(args,index);
	}

}
