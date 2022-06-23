package com.example.frontend.windowController;

import Model.ModelTools.*;
import Model.dataHolder.AnalyticsData;
import Model.dataHolder.MyResponse;

import Model.dataHolder.PlaneAnalytic;
import Model.dataHolder.PlaneData;
import com.example.frontend.*;
import Model.Model;

import com.example.frontend.Point;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MonitoringController implements Initializable, Observer {
    //................Data members.................//
    List<List<String>> data;
    MonitoringViewModel viewModel;
    Model m;
    SimpleAnomalyDetector sad = new SimpleAnomalyDetector();

    SharedGraphs sg = new SharedGraphs();
    List<List<String>> tsTrainList = sg.trainReader();
    TimeSeries ts = new TimeSeries(tsTrainList);
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

    @FXML
    private SplitMenuButton splitMenuItem;
    @FXML
    private ComboBox featureComboBox;
    String selectedFeature = "";

    @FXML
    private ComboBox planesComboBox;

    //..................Constructor.................//
    public MonitoringController() {
        this.data = new ArrayList<>();
    }

    public void initViewModel(Model m) {
        this.viewModel = new MonitoringViewModel(m);
        viewModel.addObserver(this);
        this.data = new ArrayList<>();
//        viewModel.startService();
    }

    public void setPlaneID(ActionEvent planeID) {
        String name = planesComboBox.getValue().toString();
        viewModel.startService(name);

    }
    public void setPlaneID(String id){

        viewModel.startService(id);
    }

    public void setModel(Model m) {
        this.m = m;
    }

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
        reload();
    }
    public void onSelectFeature(ActionEvent a){
        selectedFeature = featureComboBox.getValue().toString();
    }

    public void reload() {
        if(selectedFeature == null || selectedFeature.equals(""))
            return;
        selectedFeature = featureComboBox.getValue().toString();
        System.out.println(selectedFeature);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList(selectedFeature);
        double maxCorr = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < correlatedFeatureOfWhatWeNeed.size(); i++) {
            if (correlatedFeatureOfWhatWeNeed.get(i).correlation > maxCorr) {
                index = i;
                maxCorr = correlatedFeatureOfWhatWeNeed.get(i).correlation;
            }
        }
        System.out.println(maxCorr);
        if (correlatedFeatureOfWhatWeNeed.isEmpty()) {
             sg.init( featureComboBox,  bigChartBorderPane,  leftAreaChartBorderPane,  rightAreaChartBorderPane);
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
        joyStick.disableJoyStick();
        joyStick.initViewModel(m);
    }

    public void createClocks(double airSpeedVal, double verticalSpeedVal,
                             double compassVal, double altimeterVal,
                             double attitudeVal, double turnCoordinatorVal) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane clocksPane = new Pane();
        try {
            clocksPane = fxmlLoader.load(FxmlLoader.class.getResource("Clocks.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clocksBorderPane.setCenter(clocksPane);
        ClocksController clocks = (ClocksController) fxmlLoader.getController();
        clocks.createClocks(airSpeedVal,verticalSpeedVal,compassVal,altimeterVal,attitudeVal,turnCoordinatorVal);
        //clocks.initViewModel(m);
    }

//    public void init(){
////        "Aileron","Elevator","Rudder","Longitude","Latitude","AirSpeed_kt","VertSpeed",
////            "Throttle_0","Throttle_1","Altitude","PitchDeg","RollDeg","Heading","TurnCoordinator","Time"
//        if(featureComboBox.getItems().isEmpty()){
//            featureComboBox.getItems().addAll("Aileron","Elevator","Rudder","Longitude","Latitude","AirSpeed_kt","VertSpeed",
//                    "Throttle_0","Throttle_1","Altitude","PitchDeg","RollDeg","Heading","TurnCoordinator");
//        }
//        NumberAxis x = new NumberAxis();
//        NumberAxis y = new NumberAxis();
//        LineChart chart = new LineChart(x, y);
//        LineChart chart2 = new LineChart(x, y);
//        LineChart chart3 = new LineChart(x, y);
//        chart.setAnimated(false);
//        x.setTickLabelsVisible(false);
//        x.setTickMarkVisible(false);
//        y.setTickLabelsVisible(false);
//        y.setTickMarkVisible(false);
//        chart2.setAnimated(false);
//        chart3.setAnimated(false);
//        bigChartBorderPane.setCenter(chart);
//        leftAreaChartBorderPane.setCenter(chart2);
//        rightAreaChartBorderPane.setCenter(chart3);
//    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sg.init( featureComboBox,  bigChartBorderPane,  leftAreaChartBorderPane,  rightAreaChartBorderPane);
        sad.learnNormal(ts);
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<AnalyticsData> ad = (MyResponse<AnalyticsData>) arg;

        if(ad.value instanceof AnalyticsData) {
            if (ad.value != null) {
                for (PlaneAnalytic plane : ad.value.analyticList) {
                    if (plane.active == true)
                        planesComboBox.getItems().add(plane._id);
                }
                return;
            }
        }
        data = this.viewModel.getData();
        sg.setData(data);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int len = data.size() - 1;
                createClocks(
                        Double.parseDouble(data.get(len).get(5)),
                        Double.parseDouble(data.get(len).get(6)),
                        Double.parseDouble(data.get(len).get(12)),
                        Double.parseDouble(data.get(len).get(9)),
                        Double.parseDouble(data.get(len).get(10)),
                        Double.parseDouble(data.get(len).get(13)));
                reload();
            }
        });
    }
}
