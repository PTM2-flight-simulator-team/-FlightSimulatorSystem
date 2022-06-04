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

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        if(o instanceof FlightgearHandler){
            PlainData data =  (PlainData)arg;
            if(data != null)
            {
                backHandler.SendPlainData(data);
            }
        }
        if(o instanceof BackendHandler){
            setChanged();
            notifyObservers(arg);
        }
        
    }

}
