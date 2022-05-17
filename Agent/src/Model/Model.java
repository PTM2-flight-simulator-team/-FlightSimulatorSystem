package Model;

import NetworkManager.NetworkManager;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {
    private List<Command> commandList;
    private AnalyticsHandler analyticsHandler;

    public Model(List<Command> commandList, AnalyticsHandler analyticsHandler) {
        this.commandList = commandList;
        this.analyticsHandler = analyticsHandler;
    }

    public List<Command> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<Command> commandList) {
        this.commandList = commandList;
    }

    public AnalyticsHandler getAnalyticsHandler() {
        return analyticsHandler;
    }

    public void setAnalyticsHandler(AnalyticsHandler analyticsHandler) {
        this.analyticsHandler = analyticsHandler;
    }
}