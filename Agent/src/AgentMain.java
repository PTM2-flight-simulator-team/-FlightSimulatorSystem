import Controller.MyController;
import Model.MyModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AgentMain {
    public static void main(String[] args) {
        fileCreator();
        MyModel model = new MyModel();
        MyController controller = new MyController(model);
    }

    public static void fileCreator(){
        String appdataDirPathString =  System.getenv("APPDATA") + "\\Agent\\resources";
        Path path = Paths.get(appdataDirPathString);
        if (Files.exists(path)){
            return;
        }
        else{
            createFoldersAndDirs(appdataDirPathString);
        }
    }

    private static void createFoldersAndDirs(String path) {
        PrintWriter pw;
        //create dirs
        File directory = new File(path);
        directory.mkdirs();

        //create files
        File properties = new File(path + "\\properties.txt");
        try {
            properties.createNewFile();
            try {
                pw = new PrintWriter(new FileOutputStream(properties));
                pw.write(propertiesTxt);
                pw.flush();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File logs = new File(path + "\\Logs.txt");
        try {
            logs.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File pd = new File(path + "\\PlaneData.txt");
        try {
            pd.createNewFile();
            try {
                pw = new PrintWriter(new FileOutputStream(pd));
                pw.println("id=0");
                pw.println("name=JLO");
                pw.flush();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    final static String propertiesTxt = "aileron,/controls/flight/aileron[0]\n" +
            "elevator,/controls/flight/elevator\n" +
            "rudder,/controls/flight/rudder\n" +
            "flaps,/controls/flight/flaps\n" +
            "slats,/controls/flight/slats\n" +
            "speedbrake,/controls/flight/speedbrake\n" +
            "throttle_0,/controls/engines/current-engine/throttle\n" +
            "throttle_1,/controls/engines/engine[1]/throttle\n" +
            "engine-pump_0,/controls/hydraulic/system[0]/engine-pump\n" +
            "engine-pump_1,/controls/hydraulic/system[1]/engine-pump\n" +
            "electric-pump_0,/controls/hydraulic/system[0]/electric-pump\n" +
            "electric-pump_1,/controls/hydraulic/system[1]/electric-pump\n" +
            "external-power,/controls/electric/external-power\n" +
            "APU-generator,/controls/electric/APU-generator\n" +
            "latitude-deg,/position/latitude-deg\n" +
            "longitude-deg,/position/longitude-deg\n" +
            "altitude-ft,/position/altitude-ft\n" +
            "roll-deg,/orientation/roll-deg\n" +
            "pitch-deg,/orientation/pitch-deg\n" +
            "heading-deg,/orientation/heading-deg\n" +
            "side-slip-deg,/orientation/side-slip-deg\n" +
            "airspeed-kt,/velocities/airspeed-kt\n" +
            "glideslope,/velocities/glideslope\n" +
            "vertical-speed-fps,/velocities/vertical-speed-fps\n" +
            "airspeed-indicator_indicated-speed-kt,/instrumentation/airspeed-indicator/indicated-speed-kt\n" +
            "altimeter_indicated-altitude-ft,/instrumentation/altimeter/indicated-altitude-ft\n" +
            "altimeter_pressure-alt-ft,/instrumentation/altimeter/pressure-alt-ft\n" +
            "attitude-indicator_indicated-pitch-deg,/instrumentation/attitude-indicator/indicated-pitch-deg\n" +
            "attitude-indicator_indicated-roll-deg,/instrumentation/attitude-indicator/indicated-roll-deg\n" +
            "attitude-indicator_internal-pitch-deg,/instrumentation/attitude-indicator/internal-pitch-deg\n" +
            "attitude-indicator_internal-roll-deg,/instrumentation/attitude-indicator/internal-roll-deg\n" +
            "encoder_indicated-altitude-ft,/instrumentation/encoder/indicated-altitude-ft\n" +
            "encoder_pressure-alt-ft,/instrumentation/encoder/pressure-alt-ft\n" +
            "gps_indicated-altitude-ft,/instrumentation/gps/indicated-altitude-ft\n" +
            "gps_indicated-ground-speed-kt,/instrumentation/gps/indicated-ground-speed-kt\n" +
            "gps_indicated-vertical-speed,/instrumentation/gps/indicated-vertical-speed\n" +
            "indicated-heading-deg,/instrumentation/heading-indicator/offset-deg\n" +
            "magnetic-compass_indicated-heading-deg,/instrumentation/magnetic-compass/indicated-heading-deg\n" +
            "slip-skid-ball_indicated-slip-skid,/instrumentation/slip-skid-ball/indicated-slip-skid\n" +
            "turn-indicator_indicated-turn-rate,/instrumentation/turn-indicator/indicated-turn-rate\n" +
            "vertical-speed-indicator_indicated-speed-fpm,/instrumentation/vertical-speed-indicator/indicated-speed-fpm\n" +
            "engine_rpm,/engines/engine/rpm\n";
}