package Controller;

import Controller.Commands.OpenServerCommand;
import Controller.ServerConnection.FrontConnection.MyHttpServer;

public class MainController {

    public static void main(String[] args){
//        OpenServerCommand sever = new OpenServerCommand();
//        sever.execute();
        MyHttpServer server = new MyHttpServer();
        server.run();

    }


}