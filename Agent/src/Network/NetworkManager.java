package Network;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Network.Socket.MySocketHandler;

public class NetworkManager extends Observable implements Observer{
    MySocketHandler socketHandler;

    public NetworkManager(List<String> l){
        socketHandler = new MySocketHandler(l);
        socketHandler.addObserver(this);
    }

    public MySocketHandler getSocketHandler() {
        return socketHandler;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        if(arg instanceof String)
        {
            setChanged();
            notifyObservers(arg);
            return;
        }
         setChanged();
         notifyObservers(arg);
    }

    public void setCommand(String command){
        this.socketHandler.setCommand(command);
    }

    public void HandleGetDataFromFG(Object arg){
        // MyResponse res = (MyResponse)arg;
        // res.RespondWith(socketHandler.GetResponse());
    }

    public void ShutDown(String analytic, ArrayList<ArrayList<String>> flightData){
        this.socketHandler.ShutDown(analytic,flightData);
//        LocalDateTime currentTime = LocalDateTime.now();
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//        String EndtTime = currentTime.format(timeFormatter);
//        notifyObservers("EndTime:"+EndtTime);
    }

    public void PrintStream(){
        this.socketHandler.PrintStream();
    }

    public void sendFlightDataToBackend(ArrayList<ArrayList<String>> list) {
        this.socketHandler.sendFlightDataToBackend(list);
    }
    public void sendAnalyticsToBack(String data){
        this.socketHandler.sendAnalyticsToBack(data);
    }

}
