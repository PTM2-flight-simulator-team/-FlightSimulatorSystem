package com.example.frontend.windowController;

import Model.Model;
import Model.dataHolder.*;
import com.example.frontend.FxmlLoader;
import com.example.frontend.TeleoperationViewModel;
import com.google.gson.JsonObject;
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
    public Button btnAutopilot;

    @FXML
    public Button btnManual;

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

    @FXML
    void joystickMouseClicked(MouseEvent event) {
        btnManual.setStyle("-fx-text-fill: #ffffff;-fx-background-color: #333399; ");
        btnAutopilot.setStyle("-fx-text-fill: #000000;-fx-background-color: #f0f0f5; ");
    }

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

    @FXML
    void submitText(MouseEvent event) {
        btnAutopilot.setStyle("-fx-text-fill: #ffffff;-fx-background-color: #333399; ");
        btnManual.setStyle("-fx-text-fill: #000000;-fx-background-color: #f0f0f5; ");
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


    public void createClocks() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane clocksPane = new Pane();
        try {
            clocksPane = fxmlLoader.load(FxmlLoader.class.getResource("Clocks.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clocksBorderPane.setCenter(clocksPane);
        ClocksController clocks = (ClocksController) fxmlLoader.getController();
        //clocks.initViewModel(m);
    }
        public void initViewModel(Model m) {
            this.vm = new TeleoperationViewModel(m);
            this.vm.addObserver(this);
            this.vm.SendGetPlains();
        }

    @Override
    public void update(Observable o, Object arg) {
        MyResponse<AnalyticsData> ad = (MyResponse<AnalyticsData>) arg;
        if(ad.value != null){
            pickPlane.getItems().clear();
            addItemsToComboBox(ad.value);
        }
    }

    private void addItemsToComboBox(AnalyticsData ad) {
        for (int i=0; i< ad.analyticList.size(); i++){
            PlaneAnalytic data = ad.analyticList.get(i);
            if (data.active){
                pickPlane.getItems().add(data._id);
            }
        }
    }

    public void selectID(ActionEvent act){
        selectedID = pickPlane.getValue().toString();
        joyStick.setPlaneID(selectedID);
    }
}


