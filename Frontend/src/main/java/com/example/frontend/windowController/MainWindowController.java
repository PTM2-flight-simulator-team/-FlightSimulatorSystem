package com.example.frontend.windowController;

import com.example.frontend.FxmlLoader;
import Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
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

    public  static  BorderPane mainPaneStatic;

    @FXML
    private Pane topPane;

    @FXML
    private AnchorPane mainAnchorPane;

    //Buttons
    @FXML
    private Button btnFleet;

    @FXML
    private Button btnMonitoring;
    Model m;
    public  static  Model modelStatic;
    @FXML
    private void btnFleetOverview(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane fleetOverview = new Pane();
        try {
            fleetOverview = fxmlLoader.load(FxmlLoader.class.getResource("FleetOverview.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(fleetOverview);
    }
    @FXML
    private void btnMonitoring(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane monitoring = new Pane();
        try {
            monitoring = fxmlLoader.load(FxmlLoader.class.getResource("Monitoring.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(monitoring);
        MonitoringController mc = fxmlLoader.getController();
        mc.setModel(m);
        mc.createJoyStick();
        //mc.createLineCharts();
        //mc.createCircleGraph();
        mc.createClocks();
    }
    @FXML
    private void btnTeleoperation(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane teleopration = new Pane();
        try {
            teleopration = fxmlLoader.load(FxmlLoader.class.getResource("Teleoperation.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(teleopration);
        TeleoperationController teleoperationController = fxmlLoader.getController();
        teleoperationController.createJoyStick();
    }
    @FXML
    private void btnTimeCapsule(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane timeCapsule = new Pane();
        try {
            timeCapsule = fxmlLoader.load(FxmlLoader.class.getResource("TimeCapsule.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(timeCapsule);
    }

    @FXML
    private void exitButton(MouseEvent mouse) {
        if (mouse.getSource() == btnExit) {
            System.exit(0);
        }
    }

    public void setModel(Model m){
        this.m = m;
        modelStatic=m;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPaneStatic=mainPane;
        modelStatic=m;
    }
}
