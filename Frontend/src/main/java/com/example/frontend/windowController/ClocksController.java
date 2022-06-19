package com.example.frontend.windowController;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
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
        //create an air speed gauge

        Gauge airSpeed = new Gauge();

        airSpeed.setSkin(new ModernSkin(airSpeed));  //ModernSkin : you guys can change the skin
        airSpeed.setTitle("AIRSPEED");  //title
        airSpeed.setUnit("Km / h");  //unit
        airSpeed.setUnitColor(Color.WHITE);
        airSpeed.setDecimals(0);
        airSpeed.setValue(50); //deafult position of needle on gauage
        airSpeed.setAnimated(true);
        //gauge.setAnimationDuration(500);

        airSpeed.setValueColor(Color.WHITE);
        airSpeed.setTitleColor(Color.WHITE);
        airSpeed.setSubTitleColor(Color.WHITE);
        airSpeed.setBarColor(Color.rgb(0, 214, 215));
        airSpeed.setNeedleColor(Color.RED);
        airSpeed.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        airSpeed.setThreshold(85);
        airSpeed.setThresholdVisible(true);
        airSpeed.setTickLabelColor(Color.rgb(151, 151, 151));
        airSpeed.setTickMarkColor(Color.WHITE);
        airSpeed.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
        bp.setCenter(airSpeed);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paintClock();

    }
}
