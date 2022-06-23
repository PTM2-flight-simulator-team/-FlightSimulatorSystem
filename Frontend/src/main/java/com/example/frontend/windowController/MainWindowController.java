package com.example.frontend.windowController;

import com.example.frontend.Application;
import com.example.frontend.FleetOverViewModel;
import com.example.frontend.FxmlLoader;
import Model.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
    private static double xOffset = 0;
    private static double yOffset = 0;

    // For moving top pane border
    public void moveMousePressed(MouseEvent mouseEvent)
    {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void moveMouseDragged(MouseEvent mouseEvent)
    {
        Application.primaryStage.setX(mouseEvent.getScreenX() - xOffset);
        Application.primaryStage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @FXML
    private void btnFleetOverview(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane fleetOverview = new Pane();
        try {
            fleetOverview = fxmlLoader.load(FxmlLoader.class.getResource("FleetOverview.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FleetOverviewController mc = fxmlLoader.getController();
        mc.initVM(m);
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
        System.out.println(mainPane.getChildren().size());
        MonitoringController mc = fxmlLoader.getController();
        mc.setModel(m);
        mc.initViewModel(m);
        mc.viewModel.GetAnal();
        mc.createJoyStick();
        //mc.createLineCharts();
        //mc.createCircleGraph();;
//        mc.createClocks();
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
        teleoperationController.setModel(m);
        teleoperationController.createJoyStick();
//        teleoperationController.createClocks();
        teleoperationController.initViewModel(m);
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
        TimeCapsuleController mc = fxmlLoader.getController();
        mc.setModel(m);
        mc.initViewModel(m);
        mc.createJoyStick();
        //mc.createLineCharts();
        //mc.createCircleGraph();
//        mc.createClocks();
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
