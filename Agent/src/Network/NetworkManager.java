package Network;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Network.Socket.MySocketHandler;

public class NetworkManager extends Observable implements Observer{
    MySocketHandler socketHandler;

    public NetworkManager(List<String> l){
        socketHandler = new MySocketHandler(l);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
         setChanged();
         notifyObservers(arg);
    }

    public void HandleGetDataFromFG(Object arg){
        // MyResponse res = (MyResponse)arg;
        // res.RespondWith(socketHandler.GetResponse());
    }

}
