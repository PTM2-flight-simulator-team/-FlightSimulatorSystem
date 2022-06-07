package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Controller implements Observer {
    Test server;
    Socket fg;
    PrintWriter out2fg;
    Interpreter interpreter;
    String fromInterpreter;
    boolean flagdata = false;


    public Controller() {
        server = new Test();
        interpreter = new Interpreter();
        interpreter.addObserver(this);
        server.addObserver(this);
        fromInterpreter = "";
        try {
            fg = new Socket("localhost" ,5402);
            out2fg = new PrintWriter(fg.getOutputStream());
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o==interpreter){
            fromInterpreter = interpreter.getDoCommand();
            out2fg.println(fromInterpreter);
            out2fg.flush();
        }
        if(o==server){
            HashMap<String, Double> data = ( HashMap<String, Double>)arg;
            this.setData(data);
            flagdata = true;
        }
    }
    public void setData(HashMap<String,Double> data){
        this.interpreter.setFGvars(data);
    }
    public void start(){
        new Thread(server::StartServer).start();
        for(int i =0; i<10000; i++);
        out2fg.println("set /consumables/fuel/tank[0]/selected 1");
        out2fg.println("set /consumables/fuel/tank[1]/selected 1");
        out2fg.println("set /consumables/fuel/tank[2]/selected 1");
        out2fg.println("set /consumables/fuel/tank[3]/selected 1");
        out2fg.println("set /controls/engines/current-engine/mixture 1");
        out2fg.println("set /engines/active-engine/carb_ice 0.0");
        out2fg.println("set /engines/active-engine/carb_icing_rate 0.0");
        out2fg.println("set /controls/engines/current-engine/carb-heat 1");
        out2fg.println("set /engines/active-engine/running 1");
        out2fg.println("set /engines/active-engine/auto-start 1");
        out2fg.println("set /engines/active-engine/cranking 1");


        out2fg.println("set /controls/engines/engine[0]/primer 3");
        out2fg.println("set /controls/engines/engine[0]/primer-lever 0");

        out2fg.println("set /controls/engines/current-engine/throttle 0.2");
        out2fg.println("set /controls/flight/elevator-trim -0.03");

        out2fg.println("set /controls/switches/dome-red 0");
        out2fg.println("set /controls/switches/dome-white 0");
        out2fg.println("set /controls/switches/magnetos 3");
        out2fg.println("set /controls/switches/master-bat 1");
        out2fg.println("set /controls/switches/master-alt 1");
        out2fg.println("set /controls/switches/master-avionics 1");
        out2fg.println("set /controls/switches/starter 1");

        out2fg.println("set /controls/lighting/beacon 1");
        out2fg.println("set /controls/lighting/taxi-light 0");

        out2fg.println("set /fdm/jsbsim/running 0");
        out2fg.println("set /fdm/jsbsim/inertia/pointmass-weight-lbs[0] 170");
        out2fg.println("set /fdm/jsbsim/inertia/pointmass-weight-lbs[1] 0");

        out2fg.println("set /sim/model/c172p/securing/tiedownL-visible 0");
        out2fg.println("set /sim/model/c172p/securing/tiedownR-visible 0");
        out2fg.println("set /sim/model/c172p/securing/tiedownT-visible 0");
        out2fg.println("set /sim/model/c172p/securing/pitot-cover-visible 0");
        out2fg.println("set /sim/model/c172p/securing/chock 0");
        out2fg.println("set /sim/model/c172p/securing/cowl-plugs-visible 0");
        out2fg.println("set /sim/model/c172p/cockpit/control-lock-placed 0");

        out2fg.println("set /controls/gear/gear-down-command 1");

        out2fg.println("set /sim/model/c172p/brake-parking 0");
        while(true){
            if(server.newData != null){
                if(flagdata == true)
                    interpreter.interpret();
                else {
                    for(String key: server.newData.keySet()){
                        System.out.println(key + "             " + server.newData.get(key));
                    }
                }
            }
        }
    }
}
