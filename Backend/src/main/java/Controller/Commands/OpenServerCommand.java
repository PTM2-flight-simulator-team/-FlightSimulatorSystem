package Controller.Commands;

import Controller.Controller;
import Controller.ServerConnection.AgentConnections.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class OpenServerCommand extends AbstactCommand {
    int port = 5899;
    BufferedReader inFromClient;
    PrintWriter outToClient;
    volatile boolean lock = true;

    @Override
    public void execute() {
        try {
            System.out.println("Thread id:" + Thread.currentThread().getId());
            System.out.println("inside openserver execute");

            ServerSocket server = new ServerSocket(port);
            while (lock){
                System.out.println("Thread id:" + Thread.currentThread().getId());
                System.out.println("inside openserver while, waiting to client...");
                Socket client =server.accept();//client set connection
                System.out.println("accepted");
                ClientHandler clientHandler = new ClientHandler(client);
                System.out.println("observers num: " + this.countObservers());
                setChanged();
                this.notifyObservers(clientHandler);
               // clientHandler.run();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        System.out.println("inside openserver run,thread id : "+ Thread.currentThread().getId());
        this.execute();
    }
    public void stopServer(){
        lock = false;
    }
}