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
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MonitoringController implements Initializable {
    //................Data members.................//
    private List<Point2D> list = new ArrayList<>();

    //................GUI..........................//


    @FXML
    private Line altLine;
    @FXML
    private Button btn;
    @FXML
    private MenuItem pitch;
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

    @FXML
    void pitch(ActionEvent event) {
    }

    public void setModel(Model m) {
        this.m = m;
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

    public void createLineCharts() {
        //.................Create line charts.................//
        NumberAxis bigX = new NumberAxis();
        NumberAxis bigY = new NumberAxis();
        LineChart bigChart = new LineChart(bigX, bigY);
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        TimeSeries ts = new TimeSeries(
                "C:\\Users\\roey\\IdeaProjects\\FlightSimulatorSystem\\Frontend\\src\\main\\java\\Model\\ModelTools\\train.csv");
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatures = sad.listOfPairs;

        TimeSeries ts2 = new TimeSeries(
                "C:\\Users\\roey\\IdeaProjects\\FlightSimulatorSystem\\Frontend\\src\\main\\java\\Model\\ModelTools\\test.csv");
        sad.detect(ts2);
        List<AnomalyReport> reports = sad.listOfExp;
        double maxCorrelation = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < correlatedFeatures.size(); i++) {
            if (correlatedFeatures.get(i).correlation > maxCorrelation) {
                index = i;
                maxCorrelation = correlatedFeatures.get(i).correlation;
            }
        }
        //CorrelatedFeatures cf = correlatedFeatures.get(index); //if not exist then need to be fixed
        CorrelatedFeatures cf = correlatedFeatures.get(1);
        Vector<Double> v1 = ts.getColByName(cf.feature1);
        Vector<Double> v2 = ts.getColByName(cf.feature2);
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
        linearRegressionSeries.getData().add(new XYChart.Data<>(min, cf.lin_reg.f((float)min)));
        linearRegressionSeries.getData().add(new XYChart.Data<>(max, cf.lin_reg.f((float)max)));
        XYChart.Series anomalyPointsSeries = new XYChart.Series();
        anomalyPointsSeries.setName("Anomaly Points");
        for(int i = 0; i < sad.anomalyPoints.size(); i++){
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
        leftX.setLowerBound(len -20);
        leftX.setUpperBound(len);
        rightX.setAutoRanging(false);
        rightX.setLowerBound(len -20);
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
