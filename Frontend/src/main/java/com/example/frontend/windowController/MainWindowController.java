package com.example.frontend.windowController;

import com.example.frontend.FxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    //Images
    @FXML
    private ImageView icon;
    @FXML
    private ImageView btnExit;

    @FXML
    private BorderPane mainPane;

    //Buttons
    @FXML
    private Button btnFleet;

    @FXML
    private Button btnMonitoring;

    @FXML
    private void btnFleetOverview(ActionEvent event) {
        FxmlLoader obj = new FxmlLoader();
        Pane pane = obj.load("FleetOverview.fxml");
        mainPane.setCenter(pane);
    }
    @FXML
    private void btnMonitoring(ActionEvent event) {
        FxmlLoader obj = new FxmlLoader();
        Pane pane = obj.load("Monitoring.fxml");
        mainPane.setCenter(pane);
    }
    @FXML
    private void btnTeleoperation(ActionEvent event) {
        FxmlLoader obj = new FxmlLoader();
        Pane pane = obj.load("Teleoperation.fxml");
        mainPane.setCenter(pane);
    }
    @FXML
    private void btnTimeCapsule(ActionEvent event) {
        FxmlLoader obj = new FxmlLoader();
        Pane pane = obj.load("TimeCapsule.fxml");
        mainPane.setCenter(pane);
    }

    @FXML
    private void exitButton(MouseEvent mouse) {
        if (mouse.getSource() == btnExit) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
