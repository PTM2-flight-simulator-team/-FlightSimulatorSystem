package com.example.frontend;

import Model.ModelTools.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class SharedGraphs {
    List<List<String>> tsTrainList = trainReader();
    TimeSeries ts = new TimeSeries(tsTrainList);
    List<List<String>> data;

    public SharedGraphs() {
    }

    /**
     * It reads a JSON file and returns a list of lists of strings
     *
     * @return A list of lists of strings.
     */
    public List<List<String>> trainReader() {
        String json = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("Frontend/src/main/java/Model/ModelTools/trainJSON.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            json += scanner.nextLine();
        }
        List<List<String>> train = new Gson().fromJson(json, new TypeToken<List<List<String>>>() {
        }.getType());
        List<List<String>> ts = new ArrayList<>();
        for (List<String> line : train) {
            if (line.size() == 15) {
                ts.add(line);
            }
        }
        return ts;
    }

    /**
     * It creates a line chart with a linear regression line, a line chart with the data points, and a line chart with the
     * anomaly points
     *
     * @param cf                 List of CorrelatedFeatures objects
     * @param bigChartBorderPane The BorderPane that will contain the big chart.
     * @param left               the left border pane
     * @param right              the right border pane
     */
    public void createLineCharts(List<CorrelatedFeatures> cf, BorderPane bigChartBorderPane, BorderPane left, BorderPane right) {
        //.................Create line charts.................//
        NumberAxis bigX = new NumberAxis();
        NumberAxis bigY = new NumberAxis();
        LineChart bigChart = new LineChart(bigX, bigY);
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        TimeSeries tsTest = new TimeSeries(data);
        sad.listOfPairs = cf;
        sad.detect(tsTest);
        List<AnomalyReport> reports = sad.listOfExp;
        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
        Vector<Double> d1 = tsTest.getColByName(cf.get(0).getFeature1());
        Vector<Double> d2 = tsTest.getColByName(cf.get(0).getFeature2());
        int len = tsTest.getArray().size();
        XYChart.Series seriesBigChart = new XYChart.Series<>();
        seriesBigChart.setName("Big Line Chart");
        for (int i = 0; i < len; i++) {
            seriesBigChart.getData().add(new XYChart.Data<>(d1.get(i), d2.get(i)));
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
        bigChart.getData().addAll(linearRegressionSeries, seriesBigChart, anomalyPointsSeries);
        bigChartBorderPane.setCenter(bigChart);
        //.................Create area charts.................//
        createLittleGraph(v1, v2, len, left, right);
    }

    /**
     * This function creates a circle graph and two area charts
     *
     * @param bigChartBorderPane The BorderPane that will contain the circle chart.
     * @param left               the left border pane
     * @param right              the right border pane
     */
    public void createCircleGraph(List<CorrelatedFeatures> cf, BorderPane bigChartBorderPane, BorderPane left, BorderPane right) {
        List<com.example.frontend.Point> points = new ArrayList<>();
        //populate the points
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        sad.listOfPairs = cf;
        TimeSeries tsTest = new TimeSeries(data);
        //sad.detect(ts2);
        List<AnomalyReport> reports = sad.listOfExp;
        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
        Vector<Double> d1 = tsTest.getColByName(cf.get(0).getFeature1());
        Vector<Double> d2 = tsTest.getColByName(cf.get(0).getFeature2());
        for (int i = 0; i < v1.size(); i++) {
            points.add(new com.example.frontend.Point(v1.get(i), v2.get(i)));
        }
        int len = tsTest.getArray().size();
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
        for (int i = 0; i < d1.size(); i++) {
            series1.getData().add(new XYChart.Data(d1.get(i), d2.get(i)));
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
        createLittleGraph(d1, d2, len, left, right);
    }

    /**
     * This function takes in two vectors of doubles, the length of the vectors, and two border panes. It then creates two
     * area charts, one for each vector, and adds them to the border panes
     *
     * @param v1                       the vector of the left area chart
     * @param v2                       the vector of the data you want to plot
     * @param len                      the length of the vector
     * @param leftAreaChartBorderPane  the BorderPane that the left AreaChart will be placed in
     * @param rightAreaChartBorderPane the BorderPane that the right AreaChart will be placed in
     */
    public void createLittleGraph(Vector<Double> v1, Vector<Double> v2, int len, BorderPane leftAreaChartBorderPane, BorderPane rightAreaChartBorderPane) {
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

    /**
     * It initializes the charts
     */
    public void init(ComboBox featureComboBox, BorderPane bigChartBorderPane, BorderPane leftAreaChartBorderPane, BorderPane rightAreaChartBorderPane) {
        if (featureComboBox.getItems().isEmpty()) {
            featureComboBox.getItems().addAll("Aileron", "Elevator", "Rudder", "Longitude", "Latitude", "AirSpeed_kt", "VertSpeed",
                    "Throttle_0", "Throttle_1", "Altitude", "PitchDeg", "RollDeg", "Heading", "TurnCoordinator");
        }
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

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
