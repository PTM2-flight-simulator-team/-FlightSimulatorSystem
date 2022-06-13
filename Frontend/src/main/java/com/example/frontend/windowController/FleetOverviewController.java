package com.example.frontend.windowController;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.List;

public class FleetOverviewController {

    @FXML
    private ImageView refreshBtn;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;


    //Features:

    // redirecting to "Monitoring" tab for additional information about specific airplane
    public void doubleClick() {
    }

    // refreshing all airplanes locations presented in the map
    public void refreshMap() {
    }

    // manual refresh button of all the graphs
    public void refreshButton() {

    }

    // presenting a single plane information by clicking it once
    public void singlePlaneInfo(String name, int direction, int altitude, int velocity) {
        // name of the plane
        // flight direction (in degrees)
        // altitude (kilo feet - kft)
        // speed (knots - kn)
    }


    //zoom in / out?


    // changing and presenting plane icon direction towards its location
    public void direction(double longitude, double latitude) {
    }


    //Charts:

    // active planes compared to inactive planes
    public void activePlanes(int avg) {
    }

    // sorted accumulated nautical miles for individual plane since the beginning of the month
    public void singleSortedMiles(HashMap<Integer, List<Integer>> airplaneList) {
    }

    // presents average sorted nautical miles of all the fleet for every month since the beginning of the year
    public void multipleSortedMiles(HashMap<Integer, List<Integer>> airplaneList) {
    }

    // presents the fleet size relative to time
    public void lineChart(HashMap<Integer, List<Integer>> airplaneList) {
    }
}
