package Model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * A logger class that can be used to log messages to a file.
 */
public class MyLogger {
    // Declaring a constant variable called path.
    private static final String path = System.getenv("APPDATA") + "\\Agent\\resources\\Logs.txt";
    // Declaring a variable called pt that is a PrintWriter.
    private static final PrintWriter pt;

    // This is a static initializer. It is used to initialize the static variables of the class.
    static {
        try {
            pt = new PrintWriter(new FileOutputStream(path),true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The function takes a string as an argument and prints it to the console
     *
     * @param message The message to be logged.
     */
    public static void LogMessage(String message){
        pt.println(message);
    }
}