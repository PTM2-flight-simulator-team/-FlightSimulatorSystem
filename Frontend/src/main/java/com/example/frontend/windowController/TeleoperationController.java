package com.example.frontend.windowController;

import Model.dataHolder.TeleoperationsData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class TeleoperationController {

    FileChooser fileChooser = new FileChooser();

    @FXML
    private Button btnAutopilot;

    @FXML
    private Button btnLoad;

    @FXML
    private TextArea textArea;

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




}
