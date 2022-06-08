package Controller;

import Controller.Commands.OpenServerCommand;
import Controller.ServerConnection.ClientHandler;

import java.net.ServerSocket;

public class MainController {

    public static void main(String[] args){
        OpenServerCommand sever = new OpenServerCommand();
        sever.execute();
    }
}