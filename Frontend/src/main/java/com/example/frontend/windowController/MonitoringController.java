package com.example.frontend.windowController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Line;
import tools.Clocks;
import tools.JoyStick;

import java.net.URL;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MonitoringController implements Initializable {
    private List<Point2D> list = new ArrayList<>();

    @FXML
    private Canvas joyStick;
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
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("1", 1));
        series.getData().add(new XYChart.Data("2", 2));
        series.getData().add(new XYChart.Data("3", 3));
        series.getData().add(new XYChart.Data("4", 4));
        series.getData().add(new XYChart.Data("5", 5));
        anomalyGraph.setCreateSymbols(false);
        anomalyGraph.getData().addAll(series);
    }

    public void currentFeature() {

    }

    public void correlativeFeature() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JoyStick js = new JoyStick(joyStick);
        js.printJoyStick();
        Clocks clock = new Clocks(altLine);
        clock.rotateAltClock();
    }
}
