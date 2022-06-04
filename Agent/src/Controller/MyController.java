package Controller;

import Model.MyModel;
import Network.NetworkManager;
import java.util.Observable;
import java.util.Observer;

public class MyController implements Observer {
    private NetworkManager networkManager;
    private MyModel model;

    public MyController(MyModel model) {
        this.networkManager = new NetworkManager(model.GetNamesList());
        this.model = model;
        model.addObserver(this);
        networkManager.addObserver(this);
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public MyModel getModel() {
        return model;
    }

    public void setModel(MyModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass().equals(model.getClass())) {

        }
        else if (o.getClass().equals(networkManager.getClass())){

        }
    }
}
