package Network;

import java.util.Observable;
import java.util.Scanner;

/**
 * It's a class that handles the CLI of the SSH server
 */
public class SSHhandler extends Observable {

    // It's a flag that indicates if the CLI should stop.
    public volatile boolean stop = false;
    // It's a thread that runs the CLI
    private Thread CliThread;
    // It's a scanner that reads the input from the user.
    private Scanner reader;

    public SSHhandler(){

    }


    // It's a method that runs the CLI of the Agent.
    public void SSHCLI(){
        reader = new Scanner(System.in);
        String line = null;
        // It's printing the CLI options to the user.
        this.PrintCLI();
        while(!stop) {
            line = reader.nextLine();
            String[] words = line.split(" ");

            // It's checking if the user entered a command with less than 2 words.
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

            // It's notifying the observers that the state of the object has changed.
            setChanged();
            notifyObservers(c);
            // It's checking if the user entered the command "do shutdown" and if so it's stopping the CLI.
            if(line.equals("do shutdown")){
                Stop();
                break;
            }
            // It's printing the CLI options to the user again.
            this.PrintCLI();
          }
        }



    /**
     * This function prints the CLI options to the user
     */
    public void PrintCLI(){
        System.out.println("Enter you command from the current options:");
        System.out.println("set + variable + value,do printstream, do reset, do shutdown:");
    }

    /**
     * It creates a new thread, and then starts the CLI
     */
    public void runCli(){
        CliThread = new Thread("New Thread") {
            public void run() {
                SSHCLI();
            }
        };
        CliThread.start();
    }

    /**
     * This function sets the stop variable to true, and closes the reader, stop the CLI
     */
    public void Stop(){
        this.stop = true;
        reader.close();
    }
}
