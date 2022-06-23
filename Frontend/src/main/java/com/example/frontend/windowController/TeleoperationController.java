package com.example.frontend.windowController;

import Model.Model;
import Model.dataHolder.*;
import com.example.frontend.FxmlLoader;
import com.example.frontend.TeleoperationViewModel;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class TeleoperationController implements Observer {

    FileChooser fileChooser = new FileChooser();

    Model m;


    @FXML
    private Button btnShutdown;

    @FXML
    private Button btnAutopilot;

    @FXML
    private Button btnManual;

    @FXML
    private Button btnLoad;

    @FXML
    private TextArea textArea;

    @FXML
    private BorderPane joyStickBorderPane;

    @FXML
    private BorderPane clocksBorderPane;
    @FXML
    private ComboBox pickPlane;

    private TeleoperationViewModel vm;
    private String selectedID;
    private  JoyStickController joyStick;

    public void setModel(Model m) {
        this.m = m;
    }

    public void createJoyStick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane joyStickPane = new Pane();
        try {
            joyStickPane = fxmlLoader.load(FxmlLoader.class.getResource("JoyStick.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        joyStickBorderPane.setCenter(joyStickPane);
        joyStick = (JoyStickController) fxmlLoader.getController();
        //joyStick.disableJoyStick();
        joyStick.initViewModel(m);
    }

    public void createClocks(double airSpeedVal, double verticalSpeedVal,
                             double compassVal, double altimeterVal,
                             double attitudeVal, double turnCoordinatorVal) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane clocksPane = new Pane();
        try {
            clocksPane = fxmlLoader.load(FxmlLoader.class.getResource("Clocks.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clocksBorderPane.setCenter(clocksPane);
        ClocksController clocks = (ClocksController) fxmlLoader.getController();
        clocks.createClocks(airSpeedVal,verticalSpeedVal,compassVal,altimeterVal,attitudeVal,turnCoordinatorVal);
        //clocks.initViewModel(m);
    }



    public void initViewModel(Model m) {
        this.vm = new TeleoperationViewModel(m);
        this.vm.addObserver(this);
        this.vm.SendGetPlains();
    }

    // This function takes in an AnalyticsData object and adds all of the active planes to the ComboBox

    private void addItemsToComboBox(AnalyticsData ad) {
        for (int i=0; i< ad.analyticList.size(); i++){
            PlaneAnalytic data = ad.analyticList.get(i);
            if (data.active){
                pickPlane.getItems().add(data._id);
            }
        }
    }

    // When the user selects a plane from the dropdown menu, the plane's ID is stored in the variable selectedID
    public void selectID(ActionEvent act){
        selectedID = pickPlane.getValue().toString();
        joyStick.setPlaneID(selectedID);
        this.vm.startService(selectedID);
    }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<AnalyticsData> ad = (MyResponse<AnalyticsData>) arg;
        if(ad.value instanceof AnalyticsData){
            pickPlane.getItems().clear();
            addItemsToComboBox(ad.value);
        }
        MyResponse<PlaneData> pd = ( MyResponse<PlaneData>) arg;
        if(pd.value instanceof  PlaneData) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    createClocks(
                            Double.parseDouble(pd.value.airSpeed_kt),
                            Double.parseDouble(pd.value.vertSpeed),
                            Double.parseDouble(pd.value.heading),
                            Double.parseDouble(pd.value.altitude),
                            Double.parseDouble(pd.value.pitchDeg),
                            Double.parseDouble(pd.value.turnCoordinator));
                }
            });
        }
    }

    // It reloads the Teleoperation pane - used after planeshutdown
    void reloadPane(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane teleoperation = new Pane();
        try {
            teleoperation = fxmlLoader.load(FxmlLoader.class.getResource("Teleoperation.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainWindowController.mainPaneStatic.setCenter(teleoperation);
        TeleoperationController mc = fxmlLoader.getController();
        mc.setModel(MainWindowController.modelStatic);
        mc.createJoyStick();

    }


    // When the shutdown button is clicked, send a post request to the server to shutdown the selected ViewModel.
    @FXML
    void shutdown(MouseEvent event) {
        this.vm.sendPostShutdown(selectedID);
        reloadPane();
    }

     // When the joystick is clicked, the manual button is set to a blue background and white text,
     // and the autopilot button is set to a white background and black text
    @FXML
    void joystickMouseClicked(MouseEvent event) {
        btnManual.setStyle("-fx-text-fill: #ffffff;-fx-background-color: #333399;-fx-border-color:white; ");
        btnAutopilot.setStyle("-fx-text-fill: #000000;-fx-background-color: #f0f0f5; -fx-border-color:white;");
    }

     //The function `getText` is called when the user clicks on the `Open` button. It opens a file chooser window and
     // allows the user to select a file. The contents of the file are then read and displayed in the text area.
    @FXML
    void getText(MouseEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                textArea.appendText(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // When the user clicks the submit button, the text in the text area is sent to the server
    @FXML
    void submitText(MouseEvent event) {
        btnAutopilot.setStyle("-fx-text-fill: #ffffff;-fx-background-color: #333399; -fx-border-color:white;");
        btnManual.setStyle("-fx-text-fill: #000000;-fx-background-color: #f0f0f5; -fx-border-color:white;");
        TeleoperationsData toData = new TeleoperationsData();
        String text = textArea.getText();
        String[] lines = text.split("\n");
        JsonObject code = toData.code;
        int i = 1;
        for (String s : lines) {
            code.addProperty(String.valueOf(i), s);
            i++;
        }
        vm.sendCode(selectedID, toData);
    }

}


