package Model.Interpreter.Expression;

import Model.Interpreter.Commands.AbstractCommand;

import java.util.List;

public class ExpressionCommand implements Expression{//object adapter

    AbstractCommand c;

    public ExpressionCommand(AbstractCommand command) {
        this.c = command;
    }

    @Override
    public double calculate(List<String> args, int index) throws Exception {
        return c.execute(args, index);
    }
}
