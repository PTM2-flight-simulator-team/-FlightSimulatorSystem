package com.example.frontend.windowController;

import Model.ModelTools.Point;
import Model.dataHolder.AnalyticsData;
import Model.dataHolder.PlaneAnalytic;
import Model.dataHolder.PlaneData;
import com.example.frontend.FxmlLoader;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class FleetOverviewController implements Initializable {

    @FXML
    private ImageView refreshBtn;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;

    @FXML
    private PieChart myPie;

    @FXML
    private BarChart myBar;

    @FXML
    private BarChart myBar2;

    @FXML
    private Pane pane;
    @FXML
    private LineChart lineC;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView airp;

    @FXML
    private Pane worldMapPane;

//    @FXML
//    private Label lbl;

    private int[][] coordinates;
    private final int NORMAL = 10;

    private Timer timer = new Timer();


    int current_i = 75, current_j = 0;
    int prev_i = 0, prev_j = 0;
    double angle = 0;

//---------------------------------------Charts-------------------------------------------//:

    // active planes compared to inactive planes
    public void activePlanes(int avg) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Active Planes", 50), // avg - active ones
                new PieChart.Data("NonActive", 30) // 100% - avg - non active ones
        );
        myPie.setData(data);
    }


    // sorted accumulated nautical miles for individual plane since the beginning of the month
    public void singleSortedMiles(HashMap<Integer, Integer> airplaneToMiles) {

        var data = new XYChart.Series<String, Number>();
        for (Map.Entry<Integer, Integer> e : airplaneToMiles.entrySet().stream().sorted(comparingByValue()).toList()) {
            data.getData().add(new XYChart.Data<>(e.getKey() + "", e.getValue()));

        }

        myBar.getData().clear();
        myBar.getData().addAll(data);
    }


    // presents average sorted nautical miles of all the fleet for every month since the beginning of the year
    public void multipleSortedMiles(HashMap<Integer, List<Integer>> airplaneList) {
        HashMap<Integer, Integer> sums = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> e : airplaneList.entrySet()) {
            int sum = e.getValue().stream().mapToInt(a -> a).sum();
            sums.put(e.getKey(), sum);
        }
        Map<Integer, Integer> sorted = sums
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));


        var data = new XYChart.Series<String, Number>();
        for (Map.Entry<Integer, Integer> entry : sorted.entrySet()) {
            data.getData().add(new XYChart.Data<>(entry.getKey() + "", entry.getValue()));
        }
        myBar2.getData().clear();
        myBar2.getData().addAll(data);
    }

    // presents the fleet size relative to time
    public void lineChart(HashMap<Integer, Integer> airplaneList) {
        var data = new XYChart.Series<String, Number>();


        for (Map.Entry<Integer, Integer> e : airplaneList.entrySet()) {
            data.getData().add(new LineChart.Data<>(e.getKey() + "", e.getValue()));
        }
        lineC.getData().clear();
        lineC.getData().add(data);
    }
    //-------------------------------------------------------------------------------------------

    public Pair<Double,Double> latLongToOffsets(float latitude, float longitude, int mapWidth, int mapHeight) {
        final float fe = 180;
        float radius = mapWidth / (float) (2 * Math.PI);
        float latRad = degreesToRadians(latitude);
        float lonRad = degreesToRadians(longitude + fe);
        double x = lonRad * radius;
        double yFromEquator = radius * Math.log(Math.tan(Math.PI / 4 + latRad / 2));
        double y = mapHeight / 2 - yFromEquator;
        System.out.println("x" + x);
        System.out.println("y" + y);

        return new Pair<Double,Double>(x,y);
    }

    public float degreesToRadians(float degrees) {
        return (float) (degrees * Math.PI) / 180;
    }


    public FleetOverviewController() {
        refreshMap();


    }

    public void setInitPlaneLocation(int i, int j) {
        current_j = j;
        current_i = i;

        airp.setLayoutY(current_j);
        airp.setLayoutX(current_i);
        airp.setRotate(angle);
    }
    //Features:

    // redirecting to "Monitoring" tab for additional information about specific airplane
    public void doubleClick() {
    }

    // refreshing all airplanes locations presented in the map
    public void refreshMap() {
        TimerTask getAirplanePos = new TimerTask() {
            @Override
            public void run() {
//                airp.setLayoutY(current_j);
//                airp.setLayoutX(current_i);
//                airp.setRotate(angle);


            }
        };
        timer.schedule(getAirplanePos, 1000L, 1000L);
    }

    // manual refresh button of all the graphs
    public void refreshButton(MouseEvent e) {
        activePlanes(0);
        Random r = new Random();
        HashMap<Integer, List<Integer>> test = new HashMap<>();
        test.put(5, Arrays.asList(r.nextInt(10), r.nextInt(10), r.nextInt(10)));
        test.put(8, Arrays.asList(r.nextInt(10), r.nextInt(10), r.nextInt(10)));
        test.put(7, Arrays.asList(r.nextInt(10), r.nextInt(10), r.nextInt(10)));


        // singleSortedMiles(test);

        HashMap<Integer, List<Integer>> test2 = new HashMap<>();
        test2.put(4, Arrays.asList(1, r.nextInt(10), 3));
        test2.put(1, Arrays.asList(r.nextInt(10), 9, 9));
        test2.put(6, Arrays.asList(1, r.nextInt(10), 1));


        multipleSortedMiles(test2);

        HashMap<Integer, Integer> testLineChart = new HashMap<>();
        testLineChart.put(5, 250);
        testLineChart.put(8, 50);
        testLineChart.put(7, 100);
        lineChart(testLineChart);
    }

    public void updateVisuals(AnalyticsData ad) {

    }


    public void addSingleClick(ImageView plane, PlaneData pd) {
        Label lbl = new Label(pd.ID);
        lbl.setVisible(false);
        plane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               // System.out.println("test");
                lbl.setVisible(true);
                lbl.setLayoutX(plane.getLayoutX());
                lbl.setLayoutY(plane.getLayoutY() + 25);
                lbl.setVisible(true);

            }
        });
    }

    public void addDoubleClick(ImageView plane) {

        plane.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double clicked");
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        Pane monitoring = new Pane();
                        try {
                            monitoring = fxmlLoader.load(FxmlLoader.class.getResource("Monitoring.fxml").openStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        MainWindowController.mainPaneStatic.setCenter(monitoring);
                        MonitoringController mc = fxmlLoader.getController();
                        mc.setModel(MainWindowController.modelStatic);
                        mc.createJoyStick();

                        //mc.createLineCharts();
                        mc.createCircleGraph();
                        mc.createClocks();

                    }
                }
            }
        });
    }

    // changing and presenting plane icon direction towards its location
    public void direction(int longitude, int latitude) {
        prev_i = current_i;
        prev_j = current_j;
        current_i = longitude;
        current_j = latitude;

        int delta_x = Math.abs(current_j - prev_j);
        int delta_y = Math.abs(current_i - prev_i);
        angle = Math.toDegrees(Math.atan(delta_y) / (delta_x)) - 180.0;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        HashMap<String, Integer> testMap = new HashMap<>();
//        testMap.put("1",3000);
//        testMap.put("2",5000);
        AnalyticsData ad = new AnalyticsData();
        ArrayList<PlaneAnalytic> list = new ArrayList<>();

        ad.analyticList = list;
        PlaneAnalytic p1 = new PlaneAnalytic();
        PlaneAnalytic p2 = new PlaneAnalytic();
        p1._id = "0";
        p1.Name = "pilot0";
        p2._id = "1";
        p2.Name = "pilot1";

        //  p1.miles.;

        p1.planeData = new PlaneData();
        p2.planeData = new PlaneData();
        p1.planeData.altitude = "61.524010";
        p1.planeData.longitude = "105.318756";
        p2.planeData.altitude = "41.524010";
        p2.planeData.longitude = "85.318756";

        String path2 = "D:\\GitHub\\FlightSimulatorSystem\\Frontend\\src\\main\\resources\\icons\\airplaneSymbol.png";
        ImageView testPlane1 = new ImageView(new Image(path2));
        ImageView testPlane2 = new ImageView(new Image(path2));
        worldMapPane.getChildren().add(testPlane1);
        worldMapPane.getChildren().add(testPlane2);

        Pair<Double,Double> testPair  =  latLongToOffsets(Float.parseFloat(p1.planeData.altitude),Float.parseFloat(p1.planeData.longitude),780,625);
        testPlane1.relocate(testPair.getKey(),testPair.getValue());
        Pair<Double,Double> testPair2  =  latLongToOffsets(Float.parseFloat(p2.planeData.altitude),Float.parseFloat(p2.planeData.longitude),780,625);
        testPlane2.relocate(testPair2.getKey(),testPair2.getValue());
     //   addSingleClick(testPlane1,p1.planeData);
        addSingleClick(testPlane2,p2.planeData);
        testPlane1.setLayoutX(testPair.getKey());
        testPlane1.setLayoutY(testPair.getValue());
//            iv.setLayoutY(r.nextInt(500 - 20));

        Label lbl = new Label(p1.planeData.ID);
        worldMapPane.getChildren().add(lbl);
        lbl.setVisible(false);
        testPlane1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // System.out.println("test");
                lbl.setVisible(true);
                lbl.setLayoutX(testPlane1.getLayoutX());
                lbl.setLayoutY(testPlane1.getLayoutY() + 25);
                lbl.setVisible(true);

            }
        });


        // p1.planeData = new PlaneData().altitude;
        //list.add(new PlaneAnalytic())

//        double h = img1.getFitHeight() * NORMAL;//1000 0
//        double w = img1.getFitWidth() * NORMAL;//500 0
//        System.out.println("width" + img1.getFitWidth());
//        System.out.println("height" + img1.getFitHeight());

        latLongToOffsets(61.524010f, 105.318756f, 780, 625);


        ImageView iv2 = new ImageView(new Image(path2));
        worldMapPane.getChildren().add(iv2);
        iv2.relocate(618, 141);
        activePlanes(0);



        //----Gprahs tests
        //-----singleSortedMiles Test----------//

        HashMap<Integer, List<Integer>> test = new HashMap<>();
//        test.put(5, Arrays.asList(1, 2, 3));
//        test.put(8, Arrays.asList(9, 9, 9));
//        test.put(7, Arrays.asList(1, 1, 1));
        //...
        //..
        //20 planes
        // plane ID -> Miles from the beginning of the month

        // singleSortedMiles(test);

        //---------singleSortedMiles Test2----------//
        HashMap<Integer, Integer> test1New = new HashMap<>();
        test1New.put(5, 250);
        test1New.put(8, 50);
        test1New.put(7, 100);
        singleSortedMiles(test1New);

        //Test for multiple bar
        HashMap<Integer, List<Integer>> test2 = new HashMap<>();
        test2.put(4, Arrays.asList(1, 5, 3));
        test2.put(1, Arrays.asList(77, 14, 9));
        test2.put(6, Arrays.asList(1, 88, 1));


        multipleSortedMiles(test2);

        //------Testing Line Chart-------// Mapping month number to fleet size
        HashMap<Integer, Integer> testLineChart = new HashMap<>();
        testLineChart.put(5, 250);
        testLineChart.put(8, 50);
        testLineChart.put(7, 100);
        lineChart(testLineChart);

        System.out.println("w: " + img1.getFitWidth() + " h: " + img1.getFitHeight());

     //   coordinates = new int[(int) w][(int) h];

 //       Random r = new Random();
//        for (int i = 0; i < 10; i++) {
//            String path = "D:\\GitHub\\FlightSimulatorSystem\\Frontend\\src\\main\\resources\\icons\\airplaneSymbol.png";
//            ImageView iv = new ImageView(new Image(path));
//            iv.setLayoutX(r.nextInt(1000 - 20) + 75);
//            iv.setLayoutY(r.nextInt(500 - 20));
//            Label lbl = new Label("kkk\nyyyy");
//            lbl.setVisible(false);
//            iv.setRotate(r.nextDouble(360) - 180);
//            iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    System.out.println("test");
//                    lbl.setVisible(true);
//                    lbl.setLayoutX(iv.getLayoutX());
//                    lbl.setLayoutY(iv.getLayoutY() + 20);
//                    lbl.setVisible(true);
//
//                }
//            });

//            iv.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
//            {
//                @Override
//                public void handle(MouseEvent mouseEvent)
//                {
//                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
//                        if (mouseEvent.getClickCount() == 2) {
//                            System.out.println("Double clicked");
//                            FXMLLoader fxmlLoader = new FXMLLoader();
//                            Pane monitoring = new Pane();
//                            try {
//                                monitoring = fxmlLoader.load(FxmlLoader.class.getResource("Monitoring.fxml").openStream());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            MainWindowController.mainPaneStatic.setCenter(monitoring);
//                            MonitoringController mc = fxmlLoader.getController();
//                            mc.setModel(MainWindowController.modelStatic);
//                            mc.createJoyStick();
//
//                            //mc.createLineCharts();
//                            mc.createCircleGraph();
//                            mc.createClocks();
//
//                        }
//                    }
//
//                }
//            });
//            pane.getChildren().add(iv);
//            pane.getChildren().add(lbl);
//        }


    }


//            public void mousePlaneClick(MouseEvent mouseEvent) {
//                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
//                    if (mouseEvent.getClickCount() == 2) {
//                        System.out.println("Double clicked");
//                        FXMLLoader fxmlLoader = new FXMLLoader();
//                        Pane monitoring = new Pane();
//                        try {
//                            monitoring = fxmlLoader.load(FxmlLoader.class.getResource("Monitoring.fxml").openStream());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        MainWindowController.mainPaneStatic.setCenter(monitoring);
//                        MonitoringController mc = fxmlLoader.getController();
//                        mc.setModel(MainWindowController.modelStatic);
//                        mc.createJoyStick();
//
//                        //mc.createLineCharts();
//                        mc.createCircleGraph();
//                        mc.createClocks();
//
//                    }
//                }
//            }
    //      }
}