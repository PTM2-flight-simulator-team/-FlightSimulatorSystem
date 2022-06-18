package com.example.frontend.windowController;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.skins.ModernSkin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.ResourceBundle;

public class ClocksController implements Initializable {
    @FXML
    BorderPane bp = new BorderPane();
    @FXML

    public void paintClock() {
        Gauge gauge = new Gauge();
        gauge.setSkin(new ModernSkin(gauge));  //ModernSkin : you guys can change the skin
        gauge.setTitle("COOL IT HELP");  //title
        gauge.setUnit("Km / h");  //unit
        gauge.setUnitColor(Color.WHITE);
        gauge.setDecimals(0);
        gauge.setValue(50); //deafult position of needle on gauage
        gauge.setAnimated(true);
        //gauge.setAnimationDuration(500);

        gauge.setValueColor(Color.WHITE);
        gauge.setTitleColor(Color.WHITE);
        gauge.setSubTitleColor(Color.WHITE);
        gauge.setBarColor(Color.rgb(0, 214, 215));
        gauge.setNeedleColor(Color.RED);
        gauge.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        gauge.setThreshold(85);
        gauge.setThresholdVisible(true);
        gauge.setTickLabelColor(Color.rgb(151, 151, 151));
        gauge.setTickMarkColor(Color.WHITE);
        gauge.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
        bp.setCenter(gauge);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paintClock();

    }
}
