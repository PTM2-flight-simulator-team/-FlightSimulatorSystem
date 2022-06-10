package com.example.frontend;

import com.example.frontend.windowController.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

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
        //stage.initStyle(StageStyle.UNDECORATED);
        //Mouse move around
//        root.setOnMousePressed(mouseEvent -> {
//            x=mouseEvent.getSceneX();
//            y=mouseEvent.getSceneY();
//        });
//
//        root.setOnMouseDragged(mouseEvent -> {
//            stage.setX(mouseEvent.getScreenX() - x);
//            stage.setY(mouseEvent.getScreenY() - y);
//
//        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}