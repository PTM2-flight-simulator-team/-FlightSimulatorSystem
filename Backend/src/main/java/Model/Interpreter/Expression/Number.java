package Model.Interpreter.Expression;

import java.util.List;

public class Number implements Expression{

	private double value;
	
	public Number(double value) {
		this.value=value;
	}
	
	public void setValue(double value){
		this.value=value;
	}

	@Override
	public double calculate(List<String> args, int index) {
		return this.value;
	}
}
