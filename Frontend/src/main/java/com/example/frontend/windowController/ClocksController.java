package com.example.frontend.windowController;

import Model.Model;
import com.example.frontend.ClocksViewModel;
import com.example.frontend.JoyStickViewModel;
import eu.hansolo.medusa.*;
import eu.hansolo.medusa.skins.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class ClocksController implements Initializable,Observer {
    @FXML
    BorderPane bp1 = new BorderPane();
    @FXML
    BorderPane bp2 = new BorderPane();
    @FXML
    private StackPane stackbp2;
    @FXML
    BorderPane bp3 = new BorderPane();
    @FXML
    BorderPane bp4 = new BorderPane();
    @FXML
    private StackPane stackpanebp4;
    @FXML
    BorderPane bp5 = new BorderPane();
    @FXML
    BorderPane bp6 = new BorderPane();
    Gauge airSpeed;
    ClocksViewModel vm;

    DoubleProperty compassDegree, speed, verticalSpeed;

    public ClocksController() {
        compassDegree = new SimpleDoubleProperty();
        verticalSpeed = new SimpleDoubleProperty();
        speed = new SimpleDoubleProperty();
        airSpeed = new Gauge();
    }

    public void initViewModel(Model m) {
        this.vm = new ClocksViewModel(m);
        vm.addObserver(this);
        vm.compassDegree.bindBidirectional(compassDegree);
        vm.verticalSpeed.bindBidirectional(verticalSpeed);
        vm.speed.bindBidirectional(speed);
    }

    public void paintAirSpeed(double val) {
        //create an air speed gauge
        Gauge airSpeed = new Gauge();
        airSpeed.setSkin(new ModernSkin(airSpeed));  //ModernSkin : you guys can change the skin
        airSpeed.setTitle("AIRSPEED");  //title
        airSpeed.setUnit("MI / H");  //unit
        airSpeed.setUnitColor(Color.WHITE);
        airSpeed.setDecimals(0);
        airSpeed.setMinValue(0);
        airSpeed.setMaxValue(500);
        airSpeed.setValue(val); //deafult position of needle on gauage
        airSpeed.setAnimated(true);
        airSpeed.setValueColor(Color.WHITE);
        airSpeed.setTitleColor(Color.WHITE);
        airSpeed.setSubTitleColor(Color.WHITE);
        airSpeed.setBarColor(Color.rgb(0, 214, 215));
        airSpeed.setNeedleColor(Color.RED);
        airSpeed.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        airSpeed.setTickLabelColor(Color.rgb(151, 151, 151));
        airSpeed.setTickMarkColor(Color.WHITE);
        airSpeed.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
        bp1.setCenter(airSpeed);
    }

    public void paintVerticalSpeed(double val) {
        //create an air speed gauge

        Gauge verticalSpeed = GaugeBuilder.create()
                .skinType(Gauge.SkinType.VERTICAL)
                .title("100 FEET PER MINUTER")
                .unit("VERTICAL SPEED")
                .minValue(-100)
                .maxValue(100)
                .value(val)
                .animated(true)
                .foregroundBaseColor(Color.BLACK)
                .build();

        bp6.setCenter(verticalSpeed);
    }

    public void paintCompass(double val) {
        Gauge compass = GaugeBuilder.create()
                .minValue(0)
                .maxValue(359)
                .startAngle(180)
                .angleRange(360)
                .autoScale(false)
                .customTickLabelsEnabled(true)
                .customTickLabels("N", "", "", "", "", "", "", "", "",
                        "E", "", "", "", "", "", "", "", "",
                        "S", "", "", "", "", "", "", "", "",
                        "W", "", "", "", "", "", "", "", "")
                .customTickLabelFontSize(72)
                .minorTickMarksVisible(false)
                .mediumTickMarksVisible(false)
                .majorTickMarksVisible(false)
                .valueVisible(false)
                .needleType(Gauge.NeedleType.FAT)
                .needleShape(Gauge.NeedleShape.ANGLED)
                .knobType(Gauge.KnobType.FLAT)
                .knobColor(Gauge.DARK_COLOR)
                .borderPaint(Gauge.DARK_COLOR)
                .animated(true)
                .animationDuration(500)
                .needleBehavior(Gauge.NeedleBehavior.OPTIMIZED)
                .build();
        compass.setValue(val);
        bp5.setCenter(compass);
    }

    public void paintAltimeter(double val) {
//        Clock altimeter = new Clock();
//        altimeter.setSkin(new PlainClockSkin(altimeter));
//        altimeter.setTitle("ALTIMETER");
//        altimeter.setAlarmColor(Color.WHITE);
//        altimeter.setBorderPaint(Color.BLACK);
//        altimeter.setBackgroundPaint(Color.BLACK);
//        altimeter.setHourColor(Color.WHITE);
//        altimeter.setMinuteColor(Color.WHITE);
//        altimeter.setSecondColor(Color.WHITE);
//        altimeter.setTickLabelColor(Color.WHITE);
//        altimeter.setTitleColor(Color.WHITE);
//        altimeter.setHourTickMarkColor(Color.WHITE);
//        altimeter.setMinuteTickMarkColor(Color.WHITE);
//        altimeter.setTitleColor(Color.WHITE);
//        altimeter.setTitleVisible(true);
//        altimeter.setTimeMs((long)val);
//        Gauge altimeter = new Gauge();
//        altimeter.setSkin(new LcdSkin(altimeter));
//        altimeter.setTitle("ALTIMETER");
//        altimeter.setUnit("FEET");
//        altimeter.setMaxValue(5000);
//        altimeter.setMinValue(-1000);
//        altimeter.setValue(val); //deafult position of needle on gauage
//        altimeter.setPrefSize(bp3.getPrefHeight() / 2, bp3.getPrefWidth() / 2);
////        altimeter.setAnimated(true);
//        altimeter.setValueColor(Color.WHITE);
//        altimeter.setTitleColor(Color.WHITE);
//        altimeter.setSubTitleColor(Color.WHITE);
//        altimeter.setBarColor(Color.rgb(0, 214, 215));
//        altimeter.setTickLabelColor(Color.rgb(151, 151, 151));
//        altimeter.setTickMarkColor(Color.WHITE);
//        altimeter.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
//        Label altimeter1 = new Label("altimeter\n"+ val+"");
//        Gauge altimeter = new Gauge();
//        altimeter.setSkin(new ModernSkin(altimeter));  //ModernSkin : you guys can change the skin
//        altimeter.setTitle("ALTIMETER");  //title
//        altimeter.setDecimals(0);
//        altimeter.setMinValue(0);
//        altimeter.setMaxValue(500);
//        altimeter.setValue(val);
        Gauge altimeter = GaugeBuilder.create()
                .skinType(Gauge.SkinType.LCD)
                .animated(true)
                .title("ALTIMETER")
                .unit("FT")
                .lcdDesign(LcdDesign.BLACK)
                .thresholdVisible(true)
                .build();
        altimeter.setValue(val);
        bp3.setCenter(altimeter);
    }

    public void paintAttitude(double val) {
        String path = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\AltitudeInnderCircle.png";
        String pathOuter = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\AltitudeOuterCircle.png";
        ImageView imageView = new ImageView(new Image(path));
        ImageView imageViewInner = new ImageView(new Image(pathOuter));
        imageView.setFitHeight(bp2.getPrefHeight() / 2);
        imageView.setFitWidth(bp2.getPrefWidth() / 2);
        imageViewInner.setFitHeight(bp2.getPrefHeight() / 2);
        imageViewInner.setFitWidth(bp2.getPrefWidth() / 2);
        imageView.setRotate(val);
        stackbp2.getChildren().addAll(imageView, imageViewInner);

    }

    public void paintTurnCoordinator(double val) {
        String path = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\TurnCoordinatorInnerCircle.png";
        String pathOuter = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\TurnCoordinatorOuterCircle.png";
        ImageView imageView = new ImageView(new Image(path));
        ImageView imageViewInner = new ImageView(new Image(pathOuter));
        imageView.setFitHeight(bp2.getPrefHeight() / 2);
        imageView.setFitWidth(bp2.getPrefWidth() / 2);
        imageViewInner.setFitHeight(bp2.getPrefHeight() / 2);
        imageViewInner.setFitWidth(bp2.getPrefWidth() / 2);
        imageView.setRotate(val);
        stackpanebp4.getChildren().addAll(imageView, imageViewInner);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void createClocks(double airSpeedVal, double verticalSpeedVal,
                             double compassVal, double altimeterVal,
                             double attitudeVal, double turnCoordinatorVal){
        paintAirSpeed(airSpeedVal);
        paintVerticalSpeed(verticalSpeedVal);
        paintCompass(compassVal);
        paintAltimeter(altimeterVal);
        paintAttitude(attitudeVal);
        paintTurnCoordinator(turnCoordinatorVal);
    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
