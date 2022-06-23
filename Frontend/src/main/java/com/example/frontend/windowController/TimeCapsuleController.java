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
    private MenuItem aileron;

    @FXML
    private MenuItem airSpeed_kt;
    @FXML
    ImageView img1;
    @FXML
    ImageView worldMapPane;
    @FXML
    private MenuItem vertSpeed;
    @FXML
    private Text playspeed;

    @FXML
    private MenuItem altitude;

    @FXML
    private MenuItem elevator;

    @FXML
    private MenuItem rudder;

    @FXML
    private MenuItem flaps;

    @FXML
    private MenuItem longitude;

    @FXML
    private MenuItem heading;

    @FXML
    private MenuItem latitude;

    @FXML
    private MenuItem pitchDeg;

    @FXML
    private MenuItem rollDeg;

    @FXML
    private MenuItem throttle_0;

    @FXML
    private MenuItem throttle_1;
    @FXML
    private MenuItem turnCoordinator;

    @FXML
    private SplitMenuButton splitMenuItem;
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
    private volatile boolean stop = false;
    private volatile int currenIndex;
    private ImageView plane;
    @FXML
    private AnchorPane airpane;
    @FXML
    private Button load;
    @FXML
    private Button reset;

    private volatile boolean isFlightFinished = false;

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
        if (stop == false) {
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

    public void startFlight() {
        int startIndex = 0;
        for (int i = 1; i < timeSeries.size(); i++) {
            if (timeSeries.get(i).get(timeSeries.get(i).size() - 1).equals(speedTxt.getText())) {
                startIndex = i;
            }
        }

        double speed2 = Double.parseDouble(speed1.getText());
        int inc = (int) ((mySlider.getValue() / 100) * ((timeSeries.size() - 1))) + 1;
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
                    double timeSleep = (dat1 - dat2) / speed2;
                    for (int i = finalStartIndex + 1; i < timeSeries.size(); i += speed2) {
                        if (mySlider.getValue()+speed2 >= timeSeries.size()){
                            isFlightFinished = true;
                            stop = true;
                        }
                        if (!stop) {
                            pause.setVisible(true);
                            speedTxt.setText(timeSeries.get(i).get(timeSeries.get(i).size() - 1));
                            mySlider.setValue(mySlider.getValue() + (100 * speed2 / 200));
                            currenIndex = i;
                            String aileron = timeSeries.get(currenIndex).get(0);
                            String elevator = timeSeries.get(currenIndex).get(1);
                            System.out.println("aileron = " + aileron + "," + "elevator = " + elevator);
                            ChangePlanePositionByTime(currenIndex);
                            ChangeClocksStateByIndex(currenIndex);
                            UpdateJoyStickByIndex(aileron,elevator);
                            Thread.sleep((long) timeSleep);
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
        float longitude = Float.parseFloat(timeSeries.get(indexInTimeSeries).get(2));
        float latitude = Float.parseFloat(timeSeries.get(indexInTimeSeries).get(3));
        Pair<Double, Double> pair = latLongToOffsets(latitude, longitude, 390, 312);
        plane.relocate(pair.getKey(), pair.getValue());
    }

    public void ChangeClocksStateByIndex(int indexInTimeSeries) {
        //clocks.speed.setValue(airSpeed);
//        clocks.setAirSpeedClock(airSpeed1);
//        clocks.setAirSpeed(airSpeed1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                clocks.setAirSpeed(airSpeed1);
                System.out.println(timeSeries.get(indexInTimeSeries).get(5));
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
        stop = false;
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

        pause.setVisible(false);
        reset.setVisible(false);



        String path = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\airplaneSymbol.png";
        plane = new ImageView(new Image(path));
        plane.setFitHeight(20);
        plane.setFitWidth(20);
        airpane.getChildren().add(plane);
    }

    public void onPlaneSelected(ActionEvent a) {
        this.vm.sendGetFlightIDS(choosePlane.getValue().toString());
    }

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
                chooseflight.getItems().add(i);
            }
            return;
        }
        MyResponse<AnalyticsData> ad = (MyResponse<AnalyticsData>) arg;
        if (ad.value instanceof AnalyticsData) {
            addActivePlanes(ad.value);
            return;
            //loadPlaneFlights
        }
        MyResponse<List<List<String>>> ts = (MyResponse<List<List<String>>>) arg;
        if (ts.value != null) {
            timeSeries = ts.value;
            System.out.println("Got TS");
            initialLoad(timeSeries);
        }

    }

    private void initialLoad(List<List<String>> timeSeries){
        speedTxt.setText(timeSeries.get(1).get(timeSeries.get(1).size() - 1));
        featureComboBox.setVisible(true);
        mySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (stop == false) {
                    int i = (int) ((mySlider.getValue() / 100) * ((timeSeries.size()) - 1)) + 1;
                    speedTxt.setText(timeSeries.get(i).get(timeSeries.get(i).size() - 1));
                }
            }
        });
        this.sg.setData(timeSeries);
        play.setDisable(false);
        mySlider.setDisable(false);
        speed1.setDisable(false);
        float longitude = Float.parseFloat(timeSeries.get(1).get(2));
        float latitude =  Float.parseFloat(timeSeries.get(1).get(3));
        Pair<Double,Double> pair = latLongToOffsets(latitude,longitude,390,311);
        plane.relocate(pair.getKey(),pair.getValue());
        sg.init(featureComboBox,  bigChartBorderPane,  leftAreaChartBorderPane,  rightAreaChartBorderPane);
        System.out.println("Initialized...");
    }

    private void addActivePlanes(AnalyticsData ad) {
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