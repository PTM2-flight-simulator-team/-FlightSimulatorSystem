package com.example.frontend.windowController;

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
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private LineChart<?, ?> anomalyGraph;
    @FXML
    private BorderPane joyStickBorderPane;
    @FXML
    private BorderPane bigChartBorderPane;

//    @FXML
//    private LineChart<?, ?> bigChart;
//
//    @FXML
//    private CategoryAxis bigX;
//
//    @FXML
//    private NumberAxis bigY;

    Model m;
    //.........................................//

    @FXML
    void pitch(ActionEvent event) {
    }

//        anomalyGraph.getXAxis().setTickLabelsVisible(false);
//        anomalyGraph.setCreateSymbols(false);


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
        TimeSeries ts = new TimeSeries("C:\\Users\\roey\\IdeaProjects\\FlightSimulatorSystem\\Frontend\\src\\main\\java\\Model\\ModelTools\\file1.csv");
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        sad.learnNormal(ts);
        List<CorrelatedFeatures> correlatedFeatures = sad.listOfPairs;
        Vector<Double> s1 = ts.getColByName(correlatedFeatures.get(1).feature1);
        Vector<Double> s2 = ts.getColByName(correlatedFeatures.get(1).feature2);
        int len = ts.getArray().size();
        XYChart.Series seriesBigChart = new XYChart.Series<>();
        seriesBigChart.setName("Big Chart");
        for (int i = 0; i < len; i++) {
//            seriesBigChart.getData().add(new XYChart.Data<>(s1.get(i), s2.get(i)));
            seriesBigChart.getData().add(new XYChart.Data<>(ts.getColByName("A").get(i), ts.getColByName("B").get(i)));
        }
        //bigChart.getXAxis().setTickLabelsVisible(false);
        XYChart.Series series2 = new XYChart.Series();
        double max = max(s1);
        double min = min(s1);
//
//        series2.getData().add(new XYChart.Data(String.valueOf(0), correlatedFeatures.get(1).lin_reg.f((float)min)));
//
//        series2.getData().add(new XYChart.Data(String.valueOf(450), correlatedFeatures.get(1).lin_reg.f((float)max)));
        bigChart.getData().addAll(seriesBigChart);
        bigChartBorderPane.setCenter(bigChart);

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
