package com.example.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FxmlLoader {

    /**
     * It loads the FXML file and returns the root node of the FXML file
     *
     * @param fxmlFile The name of the fxml file you want to load.
     * @return A Pane object.
     */
    public static Pane load(String fxmlFile) {
        try {
            Pane pane = (Pane) FXMLLoader.load(FxmlLoader.class.getResource(fxmlFile));
            return pane;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static FXMLLoader load1(String fxmlFile) {
        try {
            return FXMLLoader.load(FxmlLoader.class.getResource(fxmlFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
