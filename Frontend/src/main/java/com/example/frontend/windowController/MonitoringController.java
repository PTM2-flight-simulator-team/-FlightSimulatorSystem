package com.example.frontend.windowController;

import Model.ModelTools.*;
import Model.dataHolder.MyResponse;

import com.example.frontend.FxmlLoader;
import Model.Model;

import com.example.frontend.MonitoringViewModel;
import com.example.frontend.Point;
import com.example.frontend.SmallestEnclosingCircle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MonitoringController implements Initializable, Observer {
    //................Data members.................//
    private List<Point2D> list = new ArrayList<>();

    //................GUI..........................//

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

    List<List<String>> data;

    MonitoringViewModel viewModel;
    Model m;

    //..................Constructor.................//
    public MonitoringController() {
        this.data = new ArrayList<>();
    }

    public void initViewModel(Model m) {
        this.viewModel = new MonitoringViewModel(m);
        viewModel.addObserver(this);
        this.data = new ArrayList<>();
    }

    public void setModel(Model m) {
        this.m = m;
    }

    SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
    TimeSeries ts = new TimeSeries(
            "Frontend/src/main/java/Model/ModelTools/train.csv");

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

    public void feature(ActionEvent event) {
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
        if (correlatedFeatureOfWhatWeNeed.isEmpty()) {
            init();
            System.out.println("No correlated features");
            return;
        }
        if (correlatedFeatureOfWhatWeNeed.get(index).correlation >= 0.95) {
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        }
        if (correlatedFeatureOfWhatWeNeed.get(index).correlation < 0.95
                && correlatedFeatureOfWhatWeNeed.get(0).correlation > 0.5) {
            createCircleGraph(correlatedFeatureOfWhatWeNeed);
        }
        if (correlatedFeatureOfWhatWeNeed.get(index).correlation < 0.5) {
            createZScoreGraph(correlatedFeatureOfWhatWeNeed);
        }
    }

    public void createLineCharts(List<CorrelatedFeatures> cf) {
        //.................Create line charts.................//
        NumberAxis bigX = new NumberAxis();
        NumberAxis bigY = new NumberAxis();
        LineChart bigChart = new LineChart(bigX, bigY);
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        TimeSeries ts2 = new TimeSeries(
                "Frontend/src/main/java/Model/ModelTools/test.csv");
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
        double max = StatLib.max(v1);
        double min = StatLib.min(v1);
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
        createLittleGraph(v1, v2, len);
    }

    public void createCircleGraph(List<CorrelatedFeatures> cf) {
        List<com.example.frontend.Point> points = new ArrayList<>();
        //populate the points
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        sad.listOfPairs = cf;
        TimeSeries tsTest = new TimeSeries(
                "Frontend/src/main/java/Model/ModelTools/test.csv");
        //sad.detect(ts2);
        List<AnomalyReport> reports = sad.listOfExp;
        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
        for (int i = 0; i < v1.size(); i++) {
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
        double upperBoundX = maxX + (maxX - minX) * zoom;
        double lowerBoundX = minX - (maxX - minX) * zoom;
        double upperBoundY = maxY + (maxY - minY) * zoom;
        double lowerBoundY = minY - (maxY - minY) * zoom;
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
        for (int i = 0; i < points.size(); i++) {
            series1.getData().add(new XYChart.Data(v1.get(i), v2.get(i)));
        }
        bigChartBorderPane.setCenter(chart);
        com.example.frontend.Circle circle2 = SmallestEnclosingCircle.makeCircle(points);
        XYChart.Series series2 = new XYChart.Series();
        for (int i = 0; i < 1000; i++) {
            double x2 = circle2.c.x + circle2.r * Math.cos(2 * Math.PI * i / 1000);
            double y2 = circle2.c.y + circle2.r * Math.sin(2 * Math.PI * i / 1000);
            series2.getData().add(new XYChart.Data(x2, y2));
        }
        XYChart.Series anomalyPointsSeriesCircle = new XYChart.Series();
        anomalyPointsSeriesCircle.setName("Anomaly Points");
        int lengthTsTest = tsTest.getColByName(cf.get(0).getFeature1()).size();
        for (int i = 0; i < lengthTsTest; i++) {
            com.example.frontend.Point p = new Point(
                    tsTest.getColByName(cf.get(0).getFeature1()).get(i), tsTest.getColByName(cf.get(0).getFeature2()).get(i));
            double distance = Math.sqrt(Math.pow(p.x - circle2.c.x, 2) + Math.pow(p.y - circle2.c.y, 2));
            if (distance > circle2.r) {
                anomalyPointsSeriesCircle.getData().add(new XYChart.Data<>(p.x, p.y));
            }
        }
        chart.getData().addAll(series2, series1, anomalyPointsSeriesCircle);
        //.................Create area charts.................//
        createLittleGraph(v1, v2, len);
    }

    public void createZScoreGraph(List<CorrelatedFeatures> cf) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart zScoreChart = new LineChart(x, y);
        zScoreChart.setTitle("Z-Score Chart");
        //populate the points
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        sad.listOfPairs = cf;
        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
        XYChart.Series trainPoints = new XYChart.Series();
        double maxtx = Double.MIN_VALUE;
        double mean = StatLib.avgZ(v1);
        double std = StatLib.stdZ(v1);
        for (int i = 0; i < v1.size(); i++) {
            double x0 = v1.get(i);
            double z = (x0 - mean) / std;
            maxtx = Math.max(maxtx, z);
            trainPoints.getData().add(new XYChart.Data(x0, z));
        }
        TimeSeries tsTest = new TimeSeries(
                "Frontend/src/main/java/Model/ModelTools/test.csv");
        double meanTest = StatLib.avgZ(tsTest.getColByName(cf.get(0).getFeature1()));
        double stdTest = StatLib.stdZ(tsTest.getColByName(cf.get(0).getFeature1()));
        XYChart.Series anomalies = new XYChart.Series();
        anomalies.setName("Anomalies");
        for (int i = 0; i < tsTest.getColByName(cf.get(0).getFeature1()).size(); i++) {
            double x0 = tsTest.getColByName(cf.get(0).getFeature1()).get(i);
            double z = (x0 - meanTest) / stdTest;
            if (z > maxtx) {
                anomalies.getData().add(new XYChart.Data(x0, z));
            }
        }
        zScoreChart.getData().addAll(trainPoints, anomalies);
        bigChartBorderPane.setCenter(zScoreChart);
        createLittleGraph(v1, v2, v1.size());
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
            seriesRightAreaChart.getData().add(new XYChart.Data<>(i, v2.get(i))); //the x need to be the column of time
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

    public void init(){
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart chart = new LineChart(x, y);
        LineChart chart2 = new LineChart(x, y);
        LineChart chart3 = new LineChart(x, y);
        chart.setAnimated(false);
        x.setTickLabelsVisible(false);
        x.setTickMarkVisible(false);
        y.setTickLabelsVisible(false);
        y.setTickMarkVisible(false);
        chart2.setAnimated(false);
        chart3.setAnimated(false);
        bigChartBorderPane.setCenter(chart);
        leftAreaChartBorderPane.setCenter(chart2);
        rightAreaChartBorderPane.setCenter(chart3);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<List<List<String>>> data = (MyResponse<List<List<String>>>) arg;
    }
}
