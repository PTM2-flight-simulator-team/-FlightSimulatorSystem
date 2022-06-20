package Model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class MyLogger {
    private static final String path = "Agent/src/Logs.txt";
    private static final PrintWriter pt;

    static {
        try {
            pt = new PrintWriter(new FileOutputStream(path),true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void LogMessage(String message){
        pt.println(message);
    }
}