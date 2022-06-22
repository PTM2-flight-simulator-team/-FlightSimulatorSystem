package com.example.frontend.windowController;

import Model.ModelTools.*;
import Model.dataHolder.AnalyticsData;
import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneData;
import com.example.frontend.*;
import Model.Model;

import com.example.frontend.Point;
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
    private Button play,pause;
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

    @FXML
    private ComboBox choosePlane,chooseflight, featureComboBox;
    ClocksController clocks = new ClocksController();

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

    public void createMonitoring(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane rachmany1 = new Pane();
        try {
            rachmany1 = fxmlLoader.load(FxmlLoader.class.getResource("Monitoring.fxml").openStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        MonitoringController mc =(MonitoringController) fxmlLoader.getController();
        mc.setModel(m);
    }

    public void dividePane(){
        split.setDividerPositions(0.6f,0.4f);
    }

    SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
    TimeSeries ts = new TimeSeries("Frontend/src/main/java/Model/ModelTools/train.csv");

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

//    public void createLineCharts(List<CorrelatedFeatures> cf) {
//        //.................Create line charts.................//
//        NumberAxis bigX = new NumberAxis();
//        NumberAxis bigY = new NumberAxis();
//        LineChart bigChart = new LineChart(bigX, bigY);
//        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
//        if (cf.isEmpty()) {
//            return;  //if there are no correlated features, maybe we should show a message to the user
//        }
//        TimeSeries ts2 = new TimeSeries("Frontend/src/main/java/Model/ModelTools/test.csv");
//
//        sad.listOfPairs = cf;
//        sad.detect(ts2);
//        List<AnomalyReport> reports = sad.listOfExp;
//        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
//        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
//        int len = ts.getArray().size();
//        XYChart.Series seriesBigChart = new XYChart.Series<>();
//        seriesBigChart.setName("Big Chart");
//        for (int i = 0; i < len; i++) {
//            seriesBigChart.getData().add(new XYChart.Data<>(v1.get(i), v2.get(i)));
//        }
//        XYChart.Series linearRegressionSeries = new XYChart.Series();
//        linearRegressionSeries.setName("Linear Regression");
//        double max = max(v1);
//        double min = min(v1);
//        linearRegressionSeries.getData().add(new XYChart.Data<>(min, cf.get(0).lin_reg.f((float) min)));
//        linearRegressionSeries.getData().add(new XYChart.Data<>(max, cf.get(0).lin_reg.f((float) max)));
//        XYChart.Series anomalyPointsSeries = new XYChart.Series();
//        anomalyPointsSeries.setName("Anomaly Points");
//        for (int i = 0; i < sad.anomalyPoints.size(); i++) {
//            anomalyPointsSeries.getData().add(new XYChart.Data<>(sad.anomalyPoints.get(i).x, sad.anomalyPoints.get(i).y));
//        }
//        bigChart.getData().addAll(seriesBigChart, linearRegressionSeries, anomalyPointsSeries);
//        bigChartBorderPane.setCenter(bigChart);
//        //.................Create area charts.................//
//        createLittleGraph(v1, v2,len);
//    }
//
//    public void createCircleGraph(List<CorrelatedFeatures> cf) {
//        List<com.example.frontend.Point> points = new ArrayList<>();
//        //populate the points
//        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
//        sad.listOfPairs = cf;
//        TimeSeries ts2 = new TimeSeries("Frontend/src/main/java/Model/ModelTools/test.csv");
//        sad.detect(ts2);
//        List<AnomalyReport> reports = sad.listOfExp;
//        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
//        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
//        for(int i = 0; i < v1.size(); i++){
//            points.add(new com.example.frontend.Point(v1.get(i), v2.get(i)));
//        }
//        int len = ts.getArray().size();
//        //create for loop that iterate points and find max and min from points
//        double maxX = Double.MIN_VALUE;
//        double maxY = Double.MIN_VALUE;
//        double minX = Double.MAX_VALUE;
//        double minY = Double.MAX_VALUE;
//        for (com.example.frontend.Point p : points) {
//            maxX = Math.max(maxX, p.x);
//            maxY = Math.max(maxY, p.y);
//            minX = Math.min(minX, p.x);
//            minY = Math.min(minY, p.y);
//        }
//        double zoom = 0.5;
//        double upperBoundX = maxX + (maxX - minX)*zoom;
//        double lowerBoundX = minX - (maxX - minX)*zoom;
//        double upperBoundY = maxY + (maxY - minY)*zoom;
//        double lowerBoundY = minY - (maxY - minY)*zoom;
//        double biggestUpperBoundXY = Math.max(upperBoundX, upperBoundY);
//        double smallestLowerBoundXY = Math.min(lowerBoundX, lowerBoundY);
//        upperBoundX = biggestUpperBoundXY;
//        lowerBoundX = smallestLowerBoundXY;
//        upperBoundY = biggestUpperBoundXY;
//        lowerBoundY = smallestLowerBoundXY;
//        NumberAxis xAxis = new NumberAxis();
//        NumberAxis yAxis = new NumberAxis();
//        ScatterChart chart = new ScatterChart(xAxis, yAxis);
//        chart.setTitle("Circle Chart");
//        xAxis.setAutoRanging(false);
//        xAxis.setLowerBound(lowerBoundX);
//        xAxis.setUpperBound(upperBoundX);
//        yAxis.setAutoRanging(false);
//        yAxis.setLowerBound(lowerBoundY);
//        yAxis.setUpperBound(upperBoundY);
//        XYChart.Series series1 = new XYChart.Series();
//        for(int i = 0; i < points.size(); i++){
//            series1.getData().add(new XYChart.Data(v1.get(i), v2.get(i)));
//        }
//        bigChartBorderPane.setCenter(chart);
//        com.example.frontend.Circle circle2 = SmallestEnclosingCircle.makeCircle(points);
//        XYChart.Series series2 = new XYChart.Series();
//        for(int i = 0; i < 1000; i++){
//            double x2 = circle2.c.x + circle2.r*Math.cos(2*Math.PI*i/1000);
//            double y2 = circle2.c.y + circle2.r*Math.sin(2*Math.PI*i/1000);
//            series2.getData().add(new XYChart.Data(x2, y2));
//        }
//        XYChart.Series anomalyPointsSeriesCircle = new XYChart.Series();
//        anomalyPointsSeriesCircle.setName("Anomaly Points");
//        for (int i = 0; i < sad.anomalyPoints.size(); i++) {
//            com.example.frontend.Point p = new Point(sad.anomalyPoints.get(i).x, sad.anomalyPoints.get(i).y);
//            double distance = Math.sqrt(Math.pow(p.x - circle2.c.x, 2) + Math.pow(p.y - circle2.c.y, 2));
//            if(distance > circle2.r){
//                anomalyPointsSeriesCircle.getData().add(new XYChart.Data<>(sad.anomalyPoints.get(i).x, sad.anomalyPoints.get(i).y));
//            }
//        }
//        chart.getData().addAll(series2, series1, anomalyPointsSeriesCircle);
//        //.................Create area charts.................//
//        createLittleGraph(v1, v2,len);
//    }
//
//    public void createLittleGraph(Vector<Double> v1, Vector<Double> v2, int len) {
//        NumberAxis leftX = new NumberAxis();
//        NumberAxis leftY = new NumberAxis();
//        AreaChart leftAreaChart = new AreaChart(leftX, leftY);
//        leftAreaChart.setCreateSymbols(false);
//        NumberAxis rightX = new NumberAxis();
//        NumberAxis rightY = new NumberAxis();
//        AreaChart rightAreaChart = new AreaChart(rightX, rightY);
//        rightAreaChart.setCreateSymbols(false);
//        XYChart.Series seriesLeftAreaChart = new XYChart.Series<>();
//        seriesLeftAreaChart.setName("Left Area Chart");
//        XYChart.Series seriesRightAreaChart = new XYChart.Series<>();
//        seriesRightAreaChart.setName("Right Area Chart");
//        for (int i = 0; i < len; i++) {
//            seriesLeftAreaChart.getData().add(new XYChart.Data<>(i, v1.get(i))); //the x need to be the column of time
//            seriesRightAreaChart.getData().add(new XYChart.Data<>(i, v2.get(i)));
//        }
//        leftX.setAutoRanging(false);
//        leftX.setLowerBound(len - 20);
//        leftX.setUpperBound(len);
//        rightX.setAutoRanging(false);
//        rightX.setLowerBound(len - 20);
//        rightX.setUpperBound(len);
//        leftAreaChart.getData().addAll(seriesLeftAreaChart);
//        rightAreaChart.getData().addAll(seriesRightAreaChart);
//        leftAreaChartBorderPane.setCenter(leftAreaChart);
//        rightAreaChartBorderPane.setCenter(rightAreaChart);
//    }

    public void createJoyStick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane joyStickPane = new Pane();
        try {
            joyStickPane = fxmlLoader.load(FxmlLoader.class.getResource("JoyStick1.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        joyStickBorderPane.setCenter(joyStickPane);
        JoyStickController joyStick = (JoyStickController) fxmlLoader.getController();
        joyStick.disableJoyStick();
        joyStick.initViewModel(m);
    }

    public void createClocks() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane clocksPane = new Pane();
        try {
            clocksPane = fxmlLoader.load(FxmlLoader.class.getResource("Clocks1.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clocksBorderPane.setCenter(clocksPane);
        ClocksController clocks = (ClocksController) fxmlLoader.getController();
        //clocks.initViewModel(m);
    }


    public void pauseFlight(){
        if (stop == false){
            pause.setText("RESUME");
            reset.setVisible(true);
            stop = true;

        }else{
            pause.setText("PAUSE");
            pause.setVisible(false);
            reset.setVisible(false);
            stop = false;
        }
    }

        public void startFlight(){
            int startIndex = 0;
            for(int i=1; i<timeSeries.size(); i++){
                if (timeSeries.get(i).get(timeSeries.get(i).size() - 1).equals(speedTxt.getText())){
                    startIndex = i;
                }
            }

            double speed2 = Double.parseDouble(speed1.getText());
            int inc = (int) ((mySlider.getValue() / 100) * ((timeSeries.size()-1))) +1;
            int finalStartIndex = startIndex;
            thread = new Thread(){
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
                            if (!stop) {
                                pause.setVisible(true);
                                speedTxt.setText(timeSeries.get(i).get(timeSeries.get(i).size() - 1));
                                mySlider.setValue(mySlider.getValue() + (100 * speed2 / 60));
                                //System.out.println("Flying...");
                                currenIndex = i;
                                ChangePlanePositionByTime(currenIndex);
                                ChangeClocksStateByIndex(currenIndex);
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


    public void feature(ActionEvent event){
        sg.setData(this.timeSeries);
        sad.learnNormal(ts);
        String name = ((MenuItem) event.getSource()).getText();
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList(name);
        double maxCorr = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < correlatedFeatureOfWhatWeNeed.size(); i++) {
            if (correlatedFeatureOfWhatWeNeed.get(i).correlation > maxCorr) {
                index = i;
                maxCorr = correlatedFeatureOfWhatWeNeed.get(i).correlation;
            }
        }
        System.out.println(correlatedFeatureOfWhatWeNeed.get(index).correlation);
        if(correlatedFeatureOfWhatWeNeed.get(index).correlation > 0.95) {
            sg.createLineCharts(correlatedFeatureOfWhatWeNeed, this.bigChartBorderPane, this.leftAreaChartBorderPane, this.rightAreaChartBorderPane);
        }
        if(correlatedFeatureOfWhatWeNeed.get(index).correlation < 0.95 && correlatedFeatureOfWhatWeNeed.get(0).correlation > 0.5) {
            sg.createCircleGraph(correlatedFeatureOfWhatWeNeed, this.bigChartBorderPane, this.leftAreaChartBorderPane, this.rightAreaChartBorderPane);
        }

    }

    public void initViewModel(Model m){
        this.vm = new TimeCapsuleViewModel(m);
        this.vm.addObserver(this);
        this.vm.sendGetAnalytic();
    }
    public void ChangePlanePositionByTime(int indexInTimeSeries){
            float longitude = Float.parseFloat(timeSeries.get(indexInTimeSeries).get(2));
            float latitude =  Float.parseFloat(timeSeries.get(indexInTimeSeries).get(3));
            Pair<Double,Double> pair = latLongToOffsets(latitude,longitude,390,312);
            plane.relocate(pair.getKey(),pair.getValue());
    }

    public void ChangeClocksStateByIndex(int indexInTimeSeries){
        double airSpeed = Double.parseDouble(timeSeries.get(indexInTimeSeries).get(4));
        clocks.speed.setValue(airSpeed);
        clocks.paintAirSpeed(airSpeed);
    }

    public void resetFlight(){
            stop = false;
            speedTxt.setText(timeSeries.get(1).get(timeSeries.get(1).size()-1));
            mySlider.setValue(0);
            speed1.setText("1");
            pause.setVisible(false);
            float longitude = Float.parseFloat(timeSeries.get(1).get(3));
            float latitude =  Float.parseFloat(timeSeries.get(1).get(4));
            Pair<Double,Double> pair = latLongToOffsets(latitude,longitude,390,311);
            plane.relocate(pair.getKey(),pair.getValue());
            reset.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // 50% size from the original map
        String mapImgPath = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\planesmap.gif";
        img1.setImage(new Image(mapImgPath));


        pause.setVisible(false);
        reset.setVisible(false);

            String path = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\airplaneSymbol.png";
            plane = new ImageView(new Image(path));
            plane.setFitHeight(20);
            plane.setFitWidth(20);
            airpane.getChildren().add(plane);
    }

    public void onPlaneSelected(ActionEvent a){
        this.vm.sendGetFlightIDS(choosePlane.getValue().toString());
    }
    public void onFlightSelected(ActionEvent a){
        this.vm.sendGetTS(choosePlane.getValue().toString(),chooseflight.getValue().toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<String> s = (MyResponse<String>) arg;
        if(s.value instanceof String){
            String id =  s.value.split("-")[1];
            int count = Integer.parseInt(id);
            for (int i=0; i <= count;i++){
                chooseflight.getItems().add(i);
            }
            return;
        }
        MyResponse<AnalyticsData> ad = (MyResponse<AnalyticsData>) arg;
        if(ad.value instanceof AnalyticsData){
            addActivePlanes(ad.value);
            return;
            //loadPlaneFlights
        }
        MyResponse<List<List<String>>> ts = (MyResponse<List<List<String>>>) arg;
        if(ts.value != null){
            timeSeries = ts.value;
            List<String> category = new ArrayList<>();
            category.add("Aileron");
            category.add("Elevator");
            category.add("Rudder");
            category.add("Longitude");
            category.add("Latitude");
            category.add("AirSpeed_kt");
            category.add("VertSpeed");
            category.add("Throttle_0");
            category.add("Throttle_1");
            category.add("Altitude");
            category.add("PitchDeg");
            category.add("RollDeg");
            category.add("Heading");
            category.add("TurnCoordinator");

            timeSeries.set(0, category);
            initialLoad();
            System.out.println("Got TS");
        }
    }

    private void addActivePlanes(AnalyticsData ad){
        for (int i =0; i < ad.analyticList.size(); i++){
            if (!ad.analyticList.get(i).active){
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

    private void initialLoad(){
        this.sg.setData(this.timeSeries);
        float longitude = Float.parseFloat(timeSeries.get(1).get(2));
        float latitude =  Float.parseFloat(timeSeries.get(1).get(3));
        Pair<Double,Double> pair = latLongToOffsets(latitude,longitude,390,311);
        plane.relocate(pair.getKey(),pair.getValue());
        sg.init( featureComboBox,  bigChartBorderPane,  leftAreaChartBorderPane,  rightAreaChartBorderPane);

        speedTxt.setText(timeSeries.get(1).get(timeSeries.get(1).size()-1));
        mySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (stop == false) {
                    int i = (int) ((mySlider.getValue() / 100) * ((timeSeries.size()) - 1)) + 1;
                    speedTxt.setText(timeSeries.get(i).get(timeSeries.get(i).size() - 1));
                }
            }
        });

    }
}