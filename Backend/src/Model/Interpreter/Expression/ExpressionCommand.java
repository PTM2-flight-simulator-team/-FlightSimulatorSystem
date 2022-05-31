package Model.Interpreter.Expression;

import Model.Interpreter.Commands.AbstractCommand;

public class ExpressionCommand implements Expression{

    AbstractCommand c;

    public ExpressionCommand(AbstractCommand command) {
        this.c = command;
    }

    @Override
    public double calculate() {
        return 0;
    }
}
