package com.example.frontend;

import Model.Model;
import Model.ModelTools.TimeSeries;
import com.example.frontend.windowController.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application extends javafx.application.Application {
    public static Stage primaryStage;
    double x,y = 0;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Model m = new Model();
        MainWindowController mwc = loader.getController();
//        JoyStickViewModel vm = new JoyStickViewModel(m);
        mwc.setModel(m);
        root.getStylesheets().add(getClass().getResource("css/chart.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("css/circleChart.css").toExternalForm());
        stage.initStyle(StageStyle.UNDECORATED); //Disables "windowed mode"
        //Mouse move around
//        TimeSeries ts = new TimeSeries(
//                "D:\\Program Files (x86)\\GitHub\\FlightSimulatorSystem\\Frontend\\src\\main\\java\\Model\\ModelTools\\file1.csv");
        List<List<String>> list = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        List<String> temp1 = new ArrayList<>();
        List<String> temp2 = new ArrayList<>();
        list.add(temp);
        list.add(temp1);
        list.add(temp2);
        list.get(0).add("A");
        list.get(1).add("12");
        list.get(2).add("13");
        list.get(0).add("B");
        list.get(1).add("20");
        list.get(2).add("25");
        list.get(0).add("Time");
        list.get(1).add("14-06-2022 15:14:01");
        list.get(2).add("14-06-2022 15:14:02");
        TimeSeries ts = new TimeSeries(list);
        System.out.println();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}