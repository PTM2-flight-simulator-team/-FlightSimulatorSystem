package Model.Interpreter;

import Model.Interpreter.Commands.Command;

import java.util.ArrayList;
import java.util.List;


public class Parser {

    List<Command> commandsQueue;
//    Utils utils;

    public Parser() {
        this.commandsQueue = new ArrayList<>();
//        this.utils = utils;

    }


    public void parse(List<String> tokens) throws Exception {//iterate over the tokens and executing the code commands
        int len = tokens.size();
        int num = -1;
        System.out.println("code is running");
        for(int i = 0; i<len;i++){
            if(Utils.isCommand(tokens.get(i))){
                num = (int)Utils.getCommand(tokens.get(i)).calculate(tokens, i);
                i += num;//jump the num of args that the command get as input
            }
        }
    }
}
