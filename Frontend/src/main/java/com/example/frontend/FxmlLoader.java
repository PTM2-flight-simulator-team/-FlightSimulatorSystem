package com.example.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FxmlLoader {
    public static Pane load(String fxmlFile) {
        try {
            Pane pane = (Pane) FXMLLoader.load(FxmlLoader.class.getResource(fxmlFile));
            return pane;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
