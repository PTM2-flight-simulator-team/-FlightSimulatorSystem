package Model.Interpreter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Test extends Observable {
    int clientPort = 5402;
    int serverPort = 5400;
    public HashMap<String,Double> newData = new HashMap<>();
    ServerSocket server;
    Socket serverAccept;
    Socket client;
    Thread serverThread;
    Thread clientThread;
    PrintWriter outToFG;

//    public Test() {
//        new Thread(this::StartServer).start();
//    }

    public void StartServer(){
        try {
            List<String> l = new ArrayList<>();
            try {
                File myObj = new File("C:\\Users\\shaha\\OneDrive\\Documents\\GitHub\\FlightSimulatorSystem\\Backend\\src\\main\\java\\Model\\Interpreter\\properties.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String[] tmp;
                    String data = myReader.nextLine().split(",")[1];
                    tmp = data.split("/");
                    data = tmp[tmp.length-1];
                    l.add(data);
                    System.out.println(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            server = new ServerSocket(serverPort);
            serverAccept = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(serverAccept.getInputStream()));
            String line;
            while((line = in.readLine())!=null)
            {
                // System.out.println(line);
                String[] vals =line.split(",");
                for(int i = 0; i < vals.length; i++){
                    newData.put(l.get(i),Double.parseDouble(vals[i]));
                }
                System.out.println(newData);
                setChanged();
                notifyObservers(newData);
                // System.out.println(newData.toString());
            }
            in.close();
            serverAccept.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
