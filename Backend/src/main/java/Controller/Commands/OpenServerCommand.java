package Controller.Commands;

import Controller.ServerConnection.AgentConnections.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class OpenServerCommand implements Command{
    int port = 5899;
    BufferedReader inFromClient;
    PrintWriter outToClient;

    @Override
    public void execute() {
        try {
            ServerSocket server = new ServerSocket(port);

            while (true){
                Socket client =server.accept();//client set connection
                ClientHandler clientHandler = new ClientHandler(client);
                clientHandler.run();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}