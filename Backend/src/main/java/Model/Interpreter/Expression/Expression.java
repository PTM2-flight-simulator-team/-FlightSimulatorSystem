package Model.Interpreter.Expression;

import java.util.List;

public interface Expression {
	public double calculate(List<String> args, int index) throws Exception;
}
