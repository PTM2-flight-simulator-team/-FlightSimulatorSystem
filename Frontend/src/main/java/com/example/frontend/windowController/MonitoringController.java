package com.example.frontend.windowController;

import com.example.frontend.FxmlLoader;
import com.example.frontend.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import tools.Clocks;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    Model m;
    //.........................................//

    @FXML
    void pitch(ActionEvent event) {
        fillListTest();
        showPoints(list);
    }

    public void fillListTest() {
        list.add(new Point2D(0, 0));
        list.add(new Point2D(1, 1));
        list.add(new Point2D(2, 2));
        list.add(new Point2D(3, 3));
        list.add(new Point2D(4, 4));
        list.add(new Point2D(5, 5));
    }

    public void showPoints(List<Point2D> list) {
        anomalyGraph.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("hi", 0));
        series.getData().add(new XYChart.Data("hi", 1));
        series.getData().add(new XYChart.Data("hello", 2));
        series.getData().add(new XYChart.Data("bye", 3));
        series.getData().add(new XYChart.Data("sex", 4));
        series.getData().add(new XYChart.Data("dfgija", 5));

        anomalyGraph.getXAxis().setTickLabelsVisible(false);
        anomalyGraph.setCreateSymbols(false);
        anomalyGraph.getData().add(series);
    }

    public void setModel(Model m){
        this.m = m;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        Clocks clock = new Clocks(altLine);
        clock.rotateAltClock();
    }
}
