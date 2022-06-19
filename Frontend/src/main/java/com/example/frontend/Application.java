package com.example.frontend;

import Model.Model;
import com.example.frontend.windowController.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    double x,y = 0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Model m = new Model();
        MainWindowController mwc = loader.getController();
        JoyStickViewModel vm = new JoyStickViewModel(m);
        mwc.setModel(m);
        root.getStylesheets().add(getClass().getResource("css/chart.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("css/circleChart.css").toExternalForm());
        //stage.initStyle(StageStyle.UNDECORATED);
        //Mouse move around
//
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}