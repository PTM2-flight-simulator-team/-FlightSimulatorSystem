package Network;

import java.io.IOException;
import java.net.SocketException;
import java.util.Observable;
import java.util.Scanner;

public class SSHhandler extends Observable {

    public volatile boolean stop = false;
    private Thread CliThread;
    private Scanner reader;

    public SSHhandler(){

    }


    public void SSHCLI(){
        reader = new Scanner(System.in);
        String line = null;
        this.PrintCLI();
        while(!stop) {
//            line = reader.nextLine();
//                System.out.println(line);
            line = reader.nextLine();
            String[] words = line.split(" ");

            if (words.length < 2)
                continue;

            NetworkCommand c = new NetworkCommand();
            c.fromObj = this;
            c.fullArg = line;
            c.path = words[1];
            if (words[0].toLowerCase().equals("set")) {
                c.action = CommandAction.Set;
                c.value = words[2];
            } else if (words[0].toLowerCase().equals("get")) {
                c.action = CommandAction.Get;
            } else {
                c.action = CommandAction.Do;
            }

            setChanged();
            notifyObservers(c);
            if(line.equals("do shutdown")){
                Stop();
                break;
            }
            this.PrintCLI();
          }
//        reader.close();
//        System.out.println("existed ssh loop");
        }



    public void PrintCLI(){
        System.out.println("Enter you command from the current options:");
        System.out.println("set + variable + value,do printstream, do reset, do shutdown:");
    }

    public void runCli(){
        CliThread = new Thread("New Thread") {
            public void run() {
                SSHCLI();
            }
        };
        CliThread.start();
    }

    public void Stop(){
        this.stop = true;
        reader.close();
//        CliThread.stop();
    }
}
