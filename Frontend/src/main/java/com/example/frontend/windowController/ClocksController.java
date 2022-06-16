package com.example.frontend.windowController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class ClocksController implements Initializable {
    @FXML
    Canvas speedClock;
    @FXML
    Line dialog;
    double mx, my;
    public void paintClock() {
        GraphicsContext gc = speedClock.getGraphicsContext2D();

        mx = speedClock.getWidth() / 2;
        my = speedClock.getHeight() / 2;
        gc.clearRect(0, 0, speedClock.getWidth(), speedClock.getHeight());
        //gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.strokeOval(mx - 50, my - 50, 100, 100);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paintClock();
    }
}
