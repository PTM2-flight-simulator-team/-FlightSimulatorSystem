package com.example.frontend.windowController;

import Model.ModelTools.AnomalyReport;
import Model.ModelTools.CorrelatedFeatures;
import Model.ModelTools.SimpleAnomalyDetector;
import Model.ModelTools.TimeSeries;
import com.example.frontend.FxmlLoader;
import Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MonitoringController implements Initializable {
    //................Data members.................//
    private List<Point2D> list = new ArrayList<>();

    //................GUI..........................//

    @FXML
    private MenuItem aileron;

    @FXML
    private MenuItem airSpeed_kt;

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
    Model m;

    //.........................................//

    public void setModel(Model m) {
        this.m = m;
    }

    SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
    TimeSeries ts = new TimeSeries(
            "C:\\Users\\roey\\IdeaProjects\\FlightSimulatorSystem\\Frontend\\src\\main\\java\\Model\\ModelTools\\train.csv");

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

    public void aileron(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("aileron");
        if (event.getSource() == aileron) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void airSpeed_kt(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("airSpeed_kt");
        if (event.getSource() == airSpeed_kt) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void vertSpeed(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("vertSpeed");
        if (event.getSource() == vertSpeed) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void altitude(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("altitude");
        if (event.getSource() == altitude) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void elevator(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("elevator");
        if (event.getSource() == elevator) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void rudder(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("rudder");
        if (event.getSource() == rudder) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void flaps(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("flaps");
        if (event.getSource() == flaps) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void longitude(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("longitude");
        if (event.getSource() == longitude) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void heading(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("heading");
        if (event.getSource() == heading) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void latitude(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("latitude");
        if (event.getSource() == latitude) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void pitchDeg(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("pitchDeg");
        if (event.getSource() == pitchDeg) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void rollDeg(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("rollDeg");
        if (event.getSource() == rollDeg) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void throttle_0(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("throttle_0");
        if (event.getSource() == throttle_0) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void throttle_1(ActionEvent event) {
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("throttle_1");
        if (event.getSource() == throttle_1) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
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

    public void createCircleGraph(){
        Circle circle = new Circle(1);
        circle.setCenterX(45);
        circle.setCenterY(338);
        double x0 = 45;
        double y0 = 338;
        double lenX = 369;
        double lenY = 363;
        circle.setStrokeWidth(5);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        //add effect
        circle.setEffect(new Lighting());
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        ScatterChart chart = new ScatterChart(xAxis, yAxis);
        chart.setTitle("Circle Chart");
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(1.0, 2.0));
        series1.getData().add(new XYChart.Data(0.9, 2.0));
        double upperBoundX = 1.1;
        double lowerBoundX = 0.0;
        double upperBoundY = 2.25;
        double lowerBoundY = 0.0;
        double rangeX = upperBoundX - lowerBoundX;
        double rangeY = upperBoundY - lowerBoundY;
        double x = x0 + lenX * ((1.0)/rangeX);
        double y = y0 - lenY * ((2.0)/rangeY);
        //circle.setCenterX(x);
        //circle.setCenterY(y);
        System.out.println(x + " " + y);
        chart.getData().add(series1);
        bigChartBorderPane.setCenter(chart);
        bigChartBorderPane.getChildren().add(circle);
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
        TimeSeries ts2 = new TimeSeries(
                "C:\\Users\\roey\\IdeaProjects\\FlightSimulatorSystem\\Frontend\\src\\main\\java\\Model\\ModelTools\\test.csv");

        double maxCorr = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < cf.size(); i++) {
            if (cf.get(i).correlation > maxCorr) {
                index = i;
                maxCorr = cf.get(i).correlation;
            }
        }
        for (CorrelatedFeatures correlatedFeatures: cf) {
            if (correlatedFeatures != cf.get(index)) {
                cf.remove(correlatedFeatures);
            }
        }
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
            joyStickPane = fxmlLoader.load(FxmlLoader.class.getResource("JoyStick.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        joyStickBorderPane.setCenter(joyStickPane);
        JoyStickController joyStick = (JoyStickController) fxmlLoader.getController();
        //joyStick.disableJoyStick();
        joyStick.initViewModel(m);
    }

    public void createClocks() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane clocksPane = new Pane();
        try {
            clocksPane = fxmlLoader.load(FxmlLoader.class.getResource("Clocks.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clocksBorderPane.setCenter(clocksPane);
        ClocksController clocks = (ClocksController) fxmlLoader.getController();
        //clocks.initViewModel(m);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
