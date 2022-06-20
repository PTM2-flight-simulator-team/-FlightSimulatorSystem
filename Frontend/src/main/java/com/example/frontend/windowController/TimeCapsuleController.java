package com.example.frontend.windowController;

import Model.ModelTools.*;
import Model.dataHolder.PlaneData;
import com.example.frontend.FxmlLoader;
import Model.Model;

import com.example.frontend.Point;
import com.example.frontend.SmallestEnclosingCircle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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

public class TimeCapsuleController implements Initializable {
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


    public Pair<Double, Double> latLongToOffsets(float latitude, float longitude, int mapWidth, int mapHeight) {
        final float fe = 180;
        float radius = mapWidth / (float) (2 * Math.PI);
        float latRad = degreesToRadians(latitude);
        float lonRad = degreesToRadians(longitude + fe);
        double x = lonRad * radius;
        double yFromEquator = radius * Math.log(Math.tan(Math.PI / 4 + latRad / 2));
        double y = mapHeight / 2 - yFromEquator;
        System.out.println("x" + x);
        System.out.println("y" + y);

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

    public void createLineCharts(List<CorrelatedFeatures> cf) {
        //.................Create line charts.................//
        NumberAxis bigX = new NumberAxis();
        NumberAxis bigY = new NumberAxis();
        LineChart bigChart = new LineChart(bigX, bigY);
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        if (cf.isEmpty()) {
            return;  //if there are no correlated features, maybe we should show a message to the user
        }
        TimeSeries ts2 = new TimeSeries("Frontend/src/main/java/Model/ModelTools/test.csv");

        sad.listOfPairs = cf;
        sad.detect(ts2);
        List<AnomalyReport> reports = sad.listOfExp;
        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
        int len = ts.getArray().size();
        XYChart.Series seriesBigChart = new XYChart.Series<>();
        seriesBigChart.setName("Big Chart");
        for (int i = 0; i < len; i++) {
            seriesBigChart.getData().add(new XYChart.Data<>(v1.get(i), v2.get(i)));
        }
        XYChart.Series linearRegressionSeries = new XYChart.Series();
        linearRegressionSeries.setName("Linear Regression");
        double max = max(v1);
        double min = min(v1);
        linearRegressionSeries.getData().add(new XYChart.Data<>(min, cf.get(0).lin_reg.f((float) min)));
        linearRegressionSeries.getData().add(new XYChart.Data<>(max, cf.get(0).lin_reg.f((float) max)));
        XYChart.Series anomalyPointsSeries = new XYChart.Series();
        anomalyPointsSeries.setName("Anomaly Points");
        for (int i = 0; i < sad.anomalyPoints.size(); i++) {
            anomalyPointsSeries.getData().add(new XYChart.Data<>(sad.anomalyPoints.get(i).x, sad.anomalyPoints.get(i).y));
        }
        bigChart.getData().addAll(seriesBigChart, linearRegressionSeries, anomalyPointsSeries);
        bigChartBorderPane.setCenter(bigChart);
        //.................Create area charts.................//
        createLittleGraph(v1, v2,len);
    }

    public void createCircleGraph(List<CorrelatedFeatures> cf) {
        List<com.example.frontend.Point> points = new ArrayList<>();
        //populate the points
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        sad.listOfPairs = cf;
        TimeSeries ts2 = new TimeSeries("Frontend/src/main/java/Model/ModelTools/test.csv");
        sad.detect(ts2);
        List<AnomalyReport> reports = sad.listOfExp;
        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
        for(int i = 0; i < v1.size(); i++){
            points.add(new com.example.frontend.Point(v1.get(i), v2.get(i)));
        }
        int len = ts.getArray().size();
        //create for loop that iterate points and find max and min from points
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (com.example.frontend.Point p : points) {
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
        }
        double zoom = 0.5;
        double upperBoundX = maxX + (maxX - minX)*zoom;
        double lowerBoundX = minX - (maxX - minX)*zoom;
        double upperBoundY = maxY + (maxY - minY)*zoom;
        double lowerBoundY = minY - (maxY - minY)*zoom;
        double biggestUpperBoundXY = Math.max(upperBoundX, upperBoundY);
        double smallestLowerBoundXY = Math.min(lowerBoundX, lowerBoundY);
        upperBoundX = biggestUpperBoundXY;
        lowerBoundX = smallestLowerBoundXY;
        upperBoundY = biggestUpperBoundXY;
        lowerBoundY = smallestLowerBoundXY;
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        ScatterChart chart = new ScatterChart(xAxis, yAxis);
        chart.setTitle("Circle Chart");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(lowerBoundX);
        xAxis.setUpperBound(upperBoundX);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(lowerBoundY);
        yAxis.setUpperBound(upperBoundY);
        XYChart.Series series1 = new XYChart.Series();
        for(int i = 0; i < points.size(); i++){
            series1.getData().add(new XYChart.Data(v1.get(i), v2.get(i)));
        }
        bigChartBorderPane.setCenter(chart);
        com.example.frontend.Circle circle2 = SmallestEnclosingCircle.makeCircle(points);
        XYChart.Series series2 = new XYChart.Series();
        for(int i = 0; i < 1000; i++){
            double x2 = circle2.c.x + circle2.r*Math.cos(2*Math.PI*i/1000);
            double y2 = circle2.c.y + circle2.r*Math.sin(2*Math.PI*i/1000);
            series2.getData().add(new XYChart.Data(x2, y2));
        }
        XYChart.Series anomalyPointsSeriesCircle = new XYChart.Series();
        anomalyPointsSeriesCircle.setName("Anomaly Points");
        for (int i = 0; i < sad.anomalyPoints.size(); i++) {
            com.example.frontend.Point p = new Point(sad.anomalyPoints.get(i).x, sad.anomalyPoints.get(i).y);
            double distance = Math.sqrt(Math.pow(p.x - circle2.c.x, 2) + Math.pow(p.y - circle2.c.y, 2));
            if(distance > circle2.r){
                anomalyPointsSeriesCircle.getData().add(new XYChart.Data<>(sad.anomalyPoints.get(i).x, sad.anomalyPoints.get(i).y));
            }
        }
        chart.getData().addAll(series2, series1, anomalyPointsSeriesCircle);
        //.................Create area charts.................//
        createLittleGraph(v1, v2,len);
    }

    public void createLittleGraph(Vector<Double> v1, Vector<Double> v2, int len) {
        NumberAxis leftX = new NumberAxis();
        NumberAxis leftY = new NumberAxis();
        AreaChart leftAreaChart = new AreaChart(leftX, leftY);
        leftAreaChart.setCreateSymbols(false);
        NumberAxis rightX = new NumberAxis();
        NumberAxis rightY = new NumberAxis();
        AreaChart rightAreaChart = new AreaChart(rightX, rightY);
        rightAreaChart.setCreateSymbols(false);
        XYChart.Series seriesLeftAreaChart = new XYChart.Series<>();
        seriesLeftAreaChart.setName("Left Area Chart");
        XYChart.Series seriesRightAreaChart = new XYChart.Series<>();
        seriesRightAreaChart.setName("Right Area Chart");
        for (int i = 0; i < len; i++) {
            seriesLeftAreaChart.getData().add(new XYChart.Data<>(i, v1.get(i))); //the x need to be the column of time
            seriesRightAreaChart.getData().add(new XYChart.Data<>(i, v2.get(i)));
        }
        leftX.setAutoRanging(false);
        leftX.setLowerBound(len - 20);
        leftX.setUpperBound(len);
        rightX.setAutoRanging(false);
        rightX.setLowerBound(len - 20);
        rightX.setUpperBound(len);
        leftAreaChart.getData().addAll(seriesLeftAreaChart);
        rightAreaChart.getData().addAll(seriesRightAreaChart);
        leftAreaChartBorderPane.setCenter(leftAreaChart);
        rightAreaChartBorderPane.setCenter(rightAreaChart);
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
        JoyStickController joyStick = (JoyStickController) fxmlLoader.getController();
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
            stop = true;

        }else{
            pause.setText("PAUSE");
            stop = false;
            pause.setVisible(false);
        }
    }

    //key = time => value = row

        public void startFlight(){
        ArrayList<String[]> _records = new ArrayList<>();
        String csvName = "C:\\Users\\user\\Desktop\\Frontend1\\src\\main\\java\\Model\\ModelTools\\file2.csv";
        ArrayList<String> times = new ArrayList<>();
        File file = new File(csvName);
        try (BufferedReader br = new BufferedReader(new FileReader(csvName))){
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                _records.add(values);
            }

            for (int i=1; i<_records.size(); i++){
                ArrayList<String> list = new ArrayList<>();
                for (int j =0; j < _records.get(i).length; j++){
                    list.add(_records.get(i)[j]);
                }
            }

            int startIndex = 0;
            for(int i=1; i<_records.size(); i++){
                if (_records.get(i)[_records.get(i).length - 1].equals(speedTxt.getText())){
                    startIndex = i;
                }
            }


            double speed2 = Double.parseDouble(speed1.getText());
            int inc = (int) ((mySlider.getValue() / 100) * ((_records.size()-1))) +1;
            int finalStartIndex = startIndex;
            thread = new Thread(){
                @Override
                public void run() {

                    try {
                        SimpleDateFormat s1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        Date date1 = s1.parse(_records.get(3)[_records.get(3).length - 1]);
                        Date date2 = s1.parse(_records.get(2)[_records.get(2).length - 1]);
                        double dat1 = date1.getTime();
                        double dat2 = date2.getTime();
                        double timeSleep = (dat1 - dat2) * speed2;
                        for (int i = finalStartIndex + 1; i < _records.size(); i += speed2) {
                            if (!stop) {
                                pause.setVisible(true);
                                speedTxt.setText(_records.get(i)[_records.get(i).length - 1]);
                                mySlider.setValue(mySlider.getValue() + (100 * speed2 / 60));
                                //System.out.println("Flying...");
                                currenIndex = i;
                                Thread.sleep((long) timeSleep);
                            }
//                            if (i == _records.size()-1)
//                                System.out.println("Flight Finished");
                        }
                    } catch (InterruptedException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void feature(ActionEvent event){
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
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
        if(correlatedFeatureOfWhatWeNeed.get(index).correlation < 0.95 && correlatedFeatureOfWhatWeNeed.get(0).correlation > 0.5) {
            createCircleGraph(correlatedFeatureOfWhatWeNeed);
        }

    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 50% size from the original map
        String mapImgPath = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\planesmap.gif";
        img1.setImage(new Image(mapImgPath));


        ArrayList<String[]> _records = new ArrayList<>();
        String csvName = "C:\\Users\\user\\Desktop\\Frontend1\\src\\main\\java\\Model\\ModelTools\\file2.csv";
        ArrayList<String> times = new ArrayList<>();
        File file = new File(csvName);
        try (BufferedReader br = new BufferedReader(new FileReader(csvName))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                _records.add(values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //double speed2 = Double.parseDouble(speed1.getText());
        mySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (stop) {
                    int i = (int) ((mySlider.getValue() / 100) * ((_records.size()) - 1)) + 1;
                    int index;
                    if (i > currenIndex) {
                        index = i + currenIndex;
                    }else{
                        index = i;
                    }
                    System.out.println(i);
                    System.out.println(index);
                    speedTxt.setText(_records.get(index)[_records.get(index).length - 1]);
                }

            }
        });

    }
}