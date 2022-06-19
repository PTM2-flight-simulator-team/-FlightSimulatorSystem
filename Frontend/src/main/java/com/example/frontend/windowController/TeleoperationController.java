package com.example.frontend.windowController;

import Model.Model;
import Model.dataHolder.TeleoperationsData;
import com.example.frontend.FxmlLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class TeleoperationController {

    FileChooser fileChooser = new FileChooser();

    Model m;

    @FXML
    private Button btnAutopilot;

    @FXML
    private Button btnLoad;

    @FXML
    private TextArea textArea;

    @FXML
    private BorderPane joyStickBorderPane;



    @FXML
    void getText(MouseEvent event){
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                textArea.appendText(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void submitText(MouseEvent event) {
        btnAutopilot.setStyle("-fx-text-fill: #ffffff;-fx-background-color: #333399; ");
        TeleoperationsData toData = new TeleoperationsData();
        String text = textArea.getText();
        String[] lines = text.split("\n");
        HashMap<String,String> hashMap = toData.code;
        int i = 1;
        for (String s: lines) {
            hashMap.put(Integer.toString(i),s);
            i++;
        }
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
        JoyStickController joyStick = (JoyStickController) fxmlLoader.getController();
        //joyStick.disableJoyStick();
        joyStick.initViewModel(m);
    }




}
