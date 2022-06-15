package Network.Socket;

import java.util.*;

import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import Network.Socket.Handlers.BackendHandler;
import Network.Socket.Handlers.FlightgearHandler;

public class MySocketHandler extends Observable implements Observer {
    
    private FlightgearHandler fgHandler;
    private BackendHandler backHandler;
    public MySocketHandler(List l){
        fgHandler = new FlightgearHandler(l);
        backHandler = new BackendHandler("127.0.0.1",5899);

        fgHandler.addObserver(this);
        backHandler.addObserver(this);
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
        if(o.getClass().equals(fgHandler.getClass())){
            if(arg instanceof String){
                setChanged();
                notifyObservers(arg);
            }
            //System.out.println("Airplane Data Sent...");
            //backHandler.SendAirplaneData();
            PlaneData data =  (PlaneData)arg;
            if(data != null)
            {
                backHandler.SendPlainData(data);
//                String Analytic = "Analytic:" +"altitude "+ data.getAltitude() + " speed " + data.getAirSpeed_kt(); // add all the data you want to compare
//                setChanged();
//                notifyObservers(Analytic);
                setChanged();
                notifyObservers(data);
            }
        }
        if(o.getClass().equals(backHandler.getClass())){
            setChanged();
            notifyObservers(arg);
        }
        
    }

    public void setCommand(String command){
        this.fgHandler.WriteToFG(command);
    }

    public void ShutDown(String analytic, ArrayList<ArrayList<String>> flightData){
        this.fgHandler.Stop();
        this.sendFlightDataToBackend(flightData);
        AnalyticsData analyticsData = new AnalyticsData(analytic);
        this.backHandler.sendFinalAnalytics(analyticsData);
//        this.backHandler.sendFinalAnalytics(analytic);
        System.out.println("sent Final Analytics");
        System.out.println("The analytics are:");
        System.out.println(analytic);
        this.backHandler.Stop();
        System.out.println("Stopped everything");
    }

    public void PrintStream(){
        this.fgHandler.getMyData().Print();
    }

    public void sendFlightDataToBackend(ArrayList<ArrayList<String>> list) {
        this.backHandler.sendFlightDataToBackend(list);
    }
    public void sendAnalyticsToBack(String data){
        AnalyticsData analyticsData = new AnalyticsData(data);
        this.backHandler.sendFinalAnalytics(analyticsData);
    }

}
