package Controller;

import Controller.Commands.OpenServerCommand;
import Controller.ServerConnection.FrontConnection.MyHttpServer;

public class MainController {

    public static void main(String[] args){
//        OpenServerCommand sever = new OpenServerCommand();
//        sever.execute();
//        MyHttpServer server = new MyHttpServer();
//        server.run();
        Controller c = new Controller();
        System.out.println("Thread id:" + Thread.currentThread().getId());
        System.out.println("main thread died");


    }


}