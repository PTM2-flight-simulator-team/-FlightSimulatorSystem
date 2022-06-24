package com.example.frontend.windowController;

import Model.ModelTools.*;
import Model.dataHolder.AnalyticsData;
import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneData;
import com.example.frontend.*;
import Model.Model;

import com.example.frontend.Point;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeCapsuleController implements Initializable,Observer {
    //................Data members.................//
    private List<Point2D> list = new ArrayList<>();

    //................GUI..........................//
    Model m;
    @FXML
    private SplitPane split;
    //........................................//
    JoyStickController joyStick;
    @FXML
    ImageView img1;
    @FXML
    private BorderPane joyStickBorderPane;
    @FXML
    private BorderPane clocksBorderPane;
    @FXML
    private BorderPane bigChartBorderPane;
    @FXML
    private BorderPane leftAreaChartBorderPane;
    @FXML
    private BorderPane rightAreaChartBorderPane;
    @FXML
    private Button play, pause;
    @FXML
    private Slider mySlider;
    @FXML
    private TextField speed1;
    @FXML
    private Text speedTxt;
    private Thread thread;
    private volatile boolean stop = false; //flight is paused or not started yet
    private volatile int currenIndex;
    private ImageView plane;
    @FXML
    private AnchorPane airpane;
    @FXML
    private Button reset;
@FXML
    private ComboBox choosePlane, chooseflight, featureComboBox;
    ClocksController clocks;

    private List<List<String>> timeSeries;

    SharedGraphs sg = new SharedGraphs();

    private TimeCapsuleViewModel vm;

    public Pair<Double, Double> latLongToOffsets(float latitude, float longitude, int mapWidth, int mapHeight) {
        final float fe = 180;
        float radius = mapWidth / (float) (2 * Math.PI);
        float latRad = degreesToRadians(latitude);
        float lonRad = degreesToRadians(longitude + fe);
        double x = lonRad * radius;
        double yFromEquator = radius * Math.log(Math.tan(Math.PI / 4 + latRad / 2));
        double y = mapHeight / 2 - yFromEquator;
        return new Pair<Double, Double>(x, y);
    }

    //CPY
    public float degreesToRadians(float degrees) {
        return (float) (degrees * Math.PI) / 180;
    }

    public void setModel(Model m) {
        this.m = m;
    }

    public void createMonitoring() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane moniPane = new Pane();
        try {
            moniPane = fxmlLoader.load(FxmlLoader.class.getResource("Monitoring.fxml").openStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        MonitoringController mc = (MonitoringController) fxmlLoader.getController();
        mc.setModel(m);
    }

    public void dividePane() {
        split.setDividerPositions(0.6f, 0.4f);
    }

    SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
    List<List<String>> tsTrainList = sg.trainReader();
    TimeSeries ts = new TimeSeries(tsTrainList);

    public List<CorrelatedFeatures> findRequiredList(String name) {
        List<CorrelatedFeatures> correlatedFeatures = sad.listOfPairs;
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = new ArrayList<>();
        for (CorrelatedFeatures cf : correlatedFeatures) {
            if ((cf.getFeature1().equals(name) || cf.getFeature2().equals(name))) {
                correlatedFeatureOfWhatWeNeed.add(cf);
            }
        }
        return correlatedFeatureOfWhatWeNeed;
    }

    public double max(Vector<Double> v) {
        double max = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i) > max) {
                max = v.get(i);
            }
        }
        return max;
    }

    public double min(Vector<Double> v) {
        double min = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i) < min) {
                min = v.get(i);
            }
        }
        return min;
    }

    public void createJoyStick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane joyStickPane = new Pane();
        try {
            joyStickPane = fxmlLoader.load(FxmlLoader.class.getResource("JoyStick1.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        joyStickBorderPane.setCenter(joyStickPane);
        joyStick = (JoyStickController) fxmlLoader.getController();
        joyStick.disableJoyStick();
        joyStick.initViewModel(m);
    }

    public void createClocks(double airSpeedVal, double verticalSpeedVal,
                             double compassVal, double altimeterVal,
                             double attitudeVal, double turnCoordinatorVal) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane clocksPane = new Pane();
        try {
            clocksPane = fxmlLoader.load(FxmlLoader.class.getResource("Clocks1.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clocksBorderPane.setCenter(clocksPane);
        ClocksController clocks = (ClocksController) fxmlLoader.getController();
        clocks.createClocks(airSpeedVal,verticalSpeedVal,compassVal,altimeterVal,attitudeVal,turnCoordinatorVal);
    }


    public void pauseFlight() {
        if (stop == false) { //flight is stopped
            pause.setText("RESUME");
            reset.setVisible(true);
            stop = true;

        } else {
            pause.setText("PAUSE");
            pause.setVisible(false);
            reset.setVisible(false);
            play.setDisable(false);
            stop = false;
        }
    }

    public void startFlight() { //when play is clicked
        int startIndex = 0;
        for (int i = 1; i < timeSeries.size(); i++) {
            if (timeSeries.get(i).get(timeSeries.get(i).size() - 1).equals(speedTxt.getText())) {
                startIndex = i;
            }
        }
        double speed2 = Double.parseDouble(speed1.getText());
        int finalStartIndex = startIndex;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    SimpleDateFormat s1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    System.out.println(timeSeries.get(4).get(timeSeries.get(4).size() - 1));
                    Date date1 = s1.parse(timeSeries.get(4).get(timeSeries.get(4).size() - 1));
                    Date date2 = s1.parse(timeSeries.get(3).get(timeSeries.get(3).size() - 1));
                    double dat1 = date1.getTime();
                    double dat2 = date2.getTime();
                    double timeSleep = 0.1;
                    for (int i = finalStartIndex + 1; i < timeSeries.size(); i += speed2) {
                        if (mySlider.getValue()+speed2 >= timeSeries.size()){
                            stop = true;
                        }
                        if (!stop) { //airplane is flying
                            pause.setVisible(true); //you can press on pause now
                            String date = timeSeries.get(i).get(timeSeries.get(i).size() - 1);
                            speedTxt.setText(date); //every date from ts
                            mySlider.setValue(mySlider.getValue() + (100 * speed2 / 200));
                            currenIndex = i;
                            String aileron = timeSeries.get(currenIndex).get(0);
                            String elevator = timeSeries.get(currenIndex).get(1);
                            ChangePlanePositionByTime(currenIndex);
                            ChangeClocksStateByIndex(currenIndex);
                            UpdateJoyStickByIndex(aileron,elevator);
                            Thread.sleep(1000);
                        }
                    }
                } catch (InterruptedException | ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void UpdateJoyStickByIndex(String aileron, String elevator) {
        joyStick.mapAndSetValues(aileron,elevator);
    }


    public void feature(ActionEvent event) {
        sg.setData(this.timeSeries);
        sad.learnNormal(ts);
        String name = featureComboBox.getValue().toString();
        ;
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList(name);
        double maxCorr = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < correlatedFeatureOfWhatWeNeed.size(); i++) {
            if (correlatedFeatureOfWhatWeNeed.get(i).correlation > maxCorr) {
                index = i;
                maxCorr = correlatedFeatureOfWhatWeNeed.get(i).correlation;
            }
        }
        if (correlatedFeatureOfWhatWeNeed.isEmpty()) {
            sg.init(featureComboBox, bigChartBorderPane, leftAreaChartBorderPane, rightAreaChartBorderPane);
            System.out.println("No correlated features");
            return;
        }
        if (correlatedFeatureOfWhatWeNeed.get(index).correlation >= 0.8) {
            sg.createLineCharts(correlatedFeatureOfWhatWeNeed, this.bigChartBorderPane, this.leftAreaChartBorderPane, this.rightAreaChartBorderPane);
        }
        if (correlatedFeatureOfWhatWeNeed.get(index).correlation < 0.8) {
            sg.createCircleGraph(correlatedFeatureOfWhatWeNeed, this.bigChartBorderPane, this.leftAreaChartBorderPane, this.rightAreaChartBorderPane);
        }

    }

    public void initViewModel(Model m) {
        this.vm = new TimeCapsuleViewModel(m);
        this.vm.addObserver(this);
        this.vm.sendGetAnalytic();
    }


    public void ChangePlanePositionByTime(int indexInTimeSeries) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                float longitude = Float.parseFloat(timeSeries.get(indexInTimeSeries).get(2));
                float latitude = Float.parseFloat(timeSeries.get(indexInTimeSeries).get(3));
                Pair<Double, Double> pair = latLongToOffsets(latitude, longitude, 390, 312);
                plane.relocate(pair.getKey(), pair.getValue());
            }
        });
    }

    public void ChangeClocksStateByIndex(int indexInTimeSeries) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createClocks(
                        Double.parseDouble(timeSeries.get(indexInTimeSeries).get(5)),
                        Double.parseDouble(timeSeries.get(indexInTimeSeries).get(6)),
                        Double.parseDouble(timeSeries.get(indexInTimeSeries).get(12)),
                        Double.parseDouble(timeSeries.get(indexInTimeSeries).get(9)),
                        Double.parseDouble(timeSeries.get(indexInTimeSeries).get(10)),
                        Double.parseDouble(timeSeries.get(indexInTimeSeries).get(13)));
            }
        });
    }

    public void resetFlight() {
        //returns the flight to it's starting position and reset all the params
        stop = false; //change the flying state to false
        speedTxt.setText(timeSeries.get(1).get(timeSeries.get(1).size() - 1));
        mySlider.setValue(0);
        speed1.setText("1");
        pause.setVisible(false);
        float longitude = Float.parseFloat(timeSeries.get(1).get(3));
        float latitude = Float.parseFloat(timeSeries.get(1).get(4));
        Pair<Double, Double> pair = latLongToOffsets(latitude, longitude, 390, 311);
        plane.relocate(pair.getKey(), pair.getValue());
        reset.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 50% size from the original map
        String mapImgPath = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\planesmap.gif";
        img1.setImage(new Image(mapImgPath));

        clocks = new ClocksController();

        //as long as the flight isn't started you can't press on pause or reset
        pause.setVisible(false);
        reset.setVisible(false);

        String path = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\airplaneSymbol.png";
        plane = new ImageView(new Image(path));
        plane.setFitHeight(20);
        plane.setFitWidth(20);
        airpane.getChildren().add(plane); //puts the airplane on the map
    }

    /*
     * When the user selects a plane from the dropdown menu, the program will send a request to the server to get all the
     * flight IDs for that plane
     */
    public void onPlaneSelected(ActionEvent a) {
        this.vm.sendGetFlightIDS(choosePlane.getValue().toString());
    }

    // A method that is called when the user selects a flight.
    public void onFlightSelected(ActionEvent a) {
        this.vm.sendGetTS(choosePlane.getValue().toString(), chooseflight.getValue().toString());
    }


    @Override
    public void update(Observable o, Object arg) {
        MyResponse<String> s = (MyResponse<String>) arg;
        if (s.value instanceof String) {
            String id = s.value.split("-")[1];
            int count = Integer.parseInt(id);
            for (int i = 0; i <= count; i++) {
                chooseflight.getItems().add(i); //fills Choose-Flight Combo box with non-active flights
            }
            return;
        }
        MyResponse<AnalyticsData> ad = (MyResponse<AnalyticsData>) arg;
        if (ad.value instanceof AnalyticsData) {
            addNonActivePlanes(ad.value); //gets the non-actives planes
            return;
        }
        MyResponse<List<List<String>>> ts = (MyResponse<List<List<String>>>) arg;
        if (ts.value != null) {
            timeSeries = ts.value;
            initialLoad(timeSeries); //initialize all the data of the chosen flight
        }

    }

    private void initialLoad(List<List<String>> timeSeries){
        speedTxt.setText(timeSeries.get(1).get(timeSeries.get(1).size() - 1)); //start time of the chosen flight
        featureComboBox.setVisible(true);
        mySlider.valueProperty().addListener(new ChangeListener<Number>() { //change the slider before the flight start
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (stop == false) { //flight is paused or not started yet
                    int i = (int) ((mySlider.getValue() / 100) * ((timeSeries.size()) - 2)) + 1;
                    speedTxt.setText(timeSeries.get(i).get(timeSeries.get(i).size() - 1));
                }
            }
        });
        this.sg.setData(timeSeries);
        play.setDisable(false);
        mySlider.setDisable(false);
        speed1.setDisable(false);
        //the started longitude and latitude of the flight that chose (x & y)
        float longitude = Float.parseFloat(timeSeries.get(1).get(2));
        float latitude =  Float.parseFloat(timeSeries.get(1).get(3));
        //change the (x,y) on the map according to the correct map size
        Pair<Double,Double> pair = latLongToOffsets(latitude,longitude,390,311);
        plane.relocate(pair.getKey(),pair.getValue()); //relocate the plane to the start place
        sg.init(featureComboBox,  bigChartBorderPane,  leftAreaChartBorderPane,  rightAreaChartBorderPane);
    }

    private void addNonActivePlanes(AnalyticsData ad) { //add all the non-active airplanes to the combo-box
        for (int i = 0; i < ad.analyticList.size(); i++) {
            if (!ad.analyticList.get(i).active) {
                choosePlane.getItems().add(ad.analyticList.get(i)._id);
            }
        }

        featureComboBox.getItems().add("Aileron");
        featureComboBox.getItems().add("Elevator");
        featureComboBox.getItems().add("Rudder");
        featureComboBox.getItems().add("Longitude");
        featureComboBox.getItems().add("Latitude");
        featureComboBox.getItems().add("AirSpeed_kt");
        featureComboBox.getItems().add("VertSpeed");
        featureComboBox.getItems().add("Throttle_0");
        featureComboBox.getItems().add("Throttle_1");
        featureComboBox.getItems().add("Altitude");
        featureComboBox.getItems().add("PitchDeg");
        featureComboBox.getItems().add("RollDeg");
        featureComboBox.getItems().add("Heading");
        featureComboBox.getItems().add("TurnCoordinator");
    }
}