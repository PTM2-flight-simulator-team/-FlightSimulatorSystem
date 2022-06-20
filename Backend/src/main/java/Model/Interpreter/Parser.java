package Model.Interpreter;

import Model.Interpreter.Commands.Command;

import java.util.ArrayList;
import java.util.List;


public class Parser {
    public volatile boolean stop = false;

    List<Command> commandsQueue;
//    Utils utils;

    public Parser() {
        this.commandsQueue = new ArrayList<>();
//        this.utils = utils;

    }


    public void parse(List<String> tokens, Interpreter interpreter) throws Exception {//iterate over the tokens and executing the code commands
        int len = tokens.size();
        int num = -1;
        System.out.println("code is running");
        int i = 0;
        while (!stop && i<len){
            if(interpreter.utils.isCommand(tokens.get(i))){
                num = (int)interpreter.utils.getCommand(tokens.get(i)).calculate(tokens, i);
                i += num;//jump the num of args that the command get as input
            }
            i++;
        }
    }
}
