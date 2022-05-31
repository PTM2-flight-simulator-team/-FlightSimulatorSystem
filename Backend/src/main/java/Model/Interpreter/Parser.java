package Model.Interpreter;

import Model.Interpreter.Commands.Command;

import java.util.ArrayList;
import java.util.List;


public class Parser {

    List<Command> commandsQueue;
    Utils utils;

    public Parser(Utils utils) {
        this.commandsQueue = new ArrayList<>();
        this.utils = utils;

    }


    public void parse(List<String> tokens) {
        int len = tokens.size();
        int num = 1;
        int i = 0;
        int numOfArgs = -1;
    }
}
