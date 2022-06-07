package Network.Socket;

import java.io.ObjectStreamConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import CommonClasses.PlainData;
import Network.Socket.Handlers.BackendHandler;
import Network.Socket.Handlers.FlightgearHandler;

public class MySocketHandler extends Observable implements Observer {
    
    private FlightgearHandler fgHandler;
    private BackendHandler backHandler;
    public MySocketHandler(List l){
        fgHandler = new FlightgearHandler(l);
        backHandler = new BackendHandler("127.0.0.1",5899);

        fgHandler.addObserver(this);
    }

    public String GetResponse(){
        return "<h1>This is get response</h1>";
    }

    public FlightgearHandler getFgHandler() {
        return fgHandler;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        if(o instanceof FlightgearHandler){
            if(arg instanceof String){
                setChanged();
                notifyObservers(arg);
            }
            PlainData data =  (PlainData)arg;
            if(data != null)
            {
                backHandler.SendPlainData(data);
                String Analytic = "Analytic:" +"altitude "+ data.getAltitude() + " speed " + data.getAirSpeed_kt(); // add all the data you want to compare
                setChanged();
                notifyObservers(Analytic);
            }
        }
        if(o instanceof BackendHandler){
            setChanged();
            notifyObservers(arg);
        }
        
    }

    public void setCommand(String command){
        this.fgHandler.WriteToFG(command);
    }

    public void ShutDown(){
        this.backHandler.Stop();
        this.fgHandler.Stop();
    }

    public void PrintStream(){
        this.fgHandler.getMyData().Print();
    }

}
