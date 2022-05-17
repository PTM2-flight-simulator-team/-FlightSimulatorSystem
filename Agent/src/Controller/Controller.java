package Controller;

import Model.Model;
import NetworkManager.NetworkManager;

import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private NetworkManager networkManager;
    private Model model;

    public Controller(NetworkManager networkManager, Model model) {
        this.networkManager = networkManager;
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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
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
