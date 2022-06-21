package com.example.frontend.windowController;

import Model.Model;
import Model.dataHolder.AnalyticsData;
import Model.dataHolder.MyResponse;
import Model.dataHolder.PlaneAnalytic;
import Model.dataHolder.PlaneData;
import com.example.frontend.FleetOverViewModel;
import com.example.frontend.FxmlLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class FleetOverviewController implements Initializable, Observer {

    @FXML
    private ImageView refreshBtn;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private Button minusBtn;
    @FXML
    private Button plusBtn;
    @FXML
    private PieChart myPie;
    @FXML
    private Canvas worldMapCanvas;
    @FXML
    private BarChart myBar;

    @FXML
    private BarChart myBar2;

    @FXML
    private Pane pane;
    @FXML
    private LineChart lineC;

    @FXML
    private ScrollBar mapScrollBar;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView airp;

    @FXML
    private Pane worldMapPane;

    private Timer timer = new Timer();

    //For airplane direction
    double current_i = 75, current_j = 0;
    double prev_i = 0, prev_j = 0;
    double angle = 0;

    //Map Sizes
    double mapWidth = 750;
    double mapHeight = 590;

    FleetOverViewModel fvm;

    //Variables for zoomMap
    double initx;
    double inity;
    static int height;
    static int width;
    public String path;
    static Scene initialScene,View;
    double offSetX,offSetY,zoomlvl;

    private HashMap<String,ImageView> planeImages;
    public AnalyticsData lastAD;
    public FleetOverviewController() {
        planeImages = new HashMap<>();

    }

//---------------------------------------Charts-------------------------------------------//

    // active planes compared to inactive planes
    public void activePlanes(int avg) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Active Planes", avg), // avg - active ones
                new PieChart.Data("NonActive", 100 - avg) // 100% - avg - non active ones
        );
        myPie.setData(data);
    }


    // sorted accumulated nautical miles for individual plane since the beginning of the month
    public void singleSortedMiles(HashMap<Integer, Double> airplaneToMiles) {

        var data = new XYChart.Series<String, Number>();
        for (Map.Entry<Integer, Double> e : airplaneToMiles.entrySet().stream().sorted(comparingByValue()).toList()) {
            data.getData().add(new XYChart.Data<>(e.getKey() + "", e.getValue()));

        }

        myBar.getData().clear();
        myBar.getData().addAll(data);
    }


    public void multipleSortedMiles2(HashMap<Integer, List<Double>> airplaneList) {
        List<Double> averages = new ArrayList<>();
        for (Integer month : airplaneList.keySet()) {
            averages.add(airplaneList.get(month).stream().mapToDouble(a -> a).average().getAsDouble());
        }
        averages = averages.stream().sorted().collect(Collectors.toList());
        int cnt = 1;
        var data = new XYChart.Series<String, Number>();
        for (Double average : averages) {
            data.getData().add(new XYChart.Data<>(cnt + "", average));
            cnt++;
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

    // presents average sorted nautical miles of all the fleet for every month since the beginning of the year
//    public void multipleSortedMiles(HashMap<Integer, List<Integer>> airplaneList) {
//        HashMap<Integer, Integer> sums = new HashMap<>();
//        for (Map.Entry<Integer, List<Integer>> e : airplaneList.entrySet()) {
//            int sum = e.getValue().stream().mapToInt(a -> a).sum();
//            sums.put(e.getKey(), sum);
//        }
//        Map<Integer, Integer> sorted = sums
//                .entrySet()
//                .stream()
//                .sorted(comparingByValue())
//                .collect(
//                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
//                                LinkedHashMap::new));
//
//
//        var data = new XYChart.Series<String, Number>();
//        for (Map.Entry<Integer, Integer> entry : sorted.entrySet()) {
//            data.getData().add(new XYChart.Data<>(entry.getKey() + "", entry.getValue()));
//        }
//        myBar2.getData().clear();
//        myBar2.getData().addAll(data);
//    }

    //-------------------------------Functions-------------------------------//

    public Pair<Double, Double> latLongToOffsets(float latitude, float longitude, double mapWidth, double mapHeight) {
        final float fe = 180;
        float radius = (float)mapWidth / (float) (2 * Math.PI);
        float latRad = degreesToRadians(latitude);
        float lonRad = degreesToRadians(longitude + fe);
        double x = lonRad * radius;
        double yFromEquator = radius * Math.log(Math.tan(Math.PI / 4 + latRad / 2));
        double y = mapHeight / 2 - yFromEquator;
//        System.out.println("x" + x);
//        System.out.println("y" + y);

        return new Pair<Double, Double>(x, y);
    }

    public float degreesToRadians(float degrees) {
        return (float) (degrees * Math.PI) / 180;
    }

    public void initVM(Model m) {
        this.fvm = new FleetOverViewModel(m);
    }

    // manual refresh button of all the graphs
    public void refreshButton(MouseEvent e) {
        activePlanes(0);

        Random r = new Random();
        HashMap<Integer, List<Integer>> test = new HashMap<>();
        test.put(5, Arrays.asList(r.nextInt(10), r.nextInt(10), r.nextInt(10)));
        test.put(8, Arrays.asList(r.nextInt(10), r.nextInt(10), r.nextInt(10)));
        test.put(7, Arrays.asList(r.nextInt(10), r.nextInt(10), r.nextInt(10)));


        HashMap<Integer, Double> testSingleSortedMiles = new HashMap<>();
        testSingleSortedMiles.put(1, 49.0);
        testSingleSortedMiles.put(2, 5.0);
        testSingleSortedMiles.put(3, 30.0);
        singleSortedMiles(testSingleSortedMiles);


        HashMap<Integer, List<Double>> testMultipleSortedMiles = new HashMap<>();
        List<Double> t = new ArrayList<>();
        t.add(3.0);
        t.add(2.0);
        t.add(10.0);
        testMultipleSortedMiles.put(4, t);
        testMultipleSortedMiles.put(1, t);
        testMultipleSortedMiles.put(6, t);
        multipleSortedMiles2(testMultipleSortedMiles);


        HashMap<Integer, Integer> testLineChart = new HashMap<>();
        testLineChart.put(5, 250);
        testLineChart.put(8, 50);
        testLineChart.put(7, 100);
        lineChart(testLineChart);
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            MyResponse<AnalyticsData> ad = (MyResponse<AnalyticsData>) arg;
            lastAD = ad.value;
            updateVisuals(ad.value);
        } catch (ClassCastException e) {
            return;
        }
    }


    public void updateVisuals(AnalyticsData ad) {
        int countActivePlanes = 0;
        int fleetSize = ad.analyticList.size();

        for (int i = 0; i < ad.analyticList.size(); i++) {
            //For Planes
            float lati = Float.parseFloat(ad.analyticList.get(i).planeData.latitude);
            float longi = Float.parseFloat(ad.analyticList.get(i).planeData.longitude);
            Pair<Double, Double> pair = latLongToOffsets(lati, longi, mapWidth, mapHeight);
            ImageView p = planeImages.get(ad.analyticList.get(i)._id);
            if (p !=null){
                p.relocate(pair.getKey(),pair.getValue());
            }
            else
            {
                createPlaneView(ad.analyticList.get(i).planeData, pair);
                System.out.println("here");
            }

            if (ad.analyticList.get(i).active)
                countActivePlanes++;
        }


        //For Graphs
        HashMap<Integer, Double> map1 = new HashMap<>();
        HashMap<Integer, List<Double>> map2 = new HashMap<>();


        //for multiple
        for (int i = 0; i < ad.analyticList.size(); i++) {
            HashMap<String, Double> map = ad.analyticList.get(i).miles;
            List<Double> sum = new ArrayList<>();
            for (var value : map.values()) {
                sum.add(value);
            }
            map2.put(Integer.parseInt(ad.analyticList.get(i)._id), sum);
        }
        multipleSortedMiles2(map2);

        for (int i = 0; i < ad.analyticList.size(); i++) {
            String month1 = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.US);
            String month = "May";
            map1.put(Integer.parseInt(ad.analyticList.get(i)._id), ad.analyticList.get(i).miles.get(month1));

        }
        singleSortedMiles(map1);

    
        //lineChart(ad.analyticList.get(i),fleetSize);
        activePlanes(countActivePlanes);
    }


    public void createPlaneView(PlaneData pd, Pair<Double, Double> pair) {

        // String path = "D:\\GitHub\\FlightSimulatorSystem\\Frontend\\src\\main\\resources\\icons\\airplaneSymbol.png";
        String path = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\airplaneSymbol.png";
        ImageView planeIMG = new ImageView(new Image(path)); // russia
        airp = planeIMG; // needs to be done for all the planes (not just one) - Testing plane direction
        planeIMG.relocate(pair.getKey(), pair.getValue());
        Tooltip tooltip = new Tooltip("Plane name: " + pd.PlaneName + "\n" + "Flight direction: " + pd.heading + "\n" + "Altitude: " + pd.altitude + "\n" + "Speed (knots): " + pd.airSpeed_kt);
        Tooltip.install(planeIMG, tooltip);
        tooltip.setShowDelay(Duration.seconds(0.5));
        //tooltip.setFont();
        planeImages.put(pd.ID,planeIMG);
        worldMapPane.getChildren().add(planeIMG);
        addDoubleClick(planeIMG);
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
                        // mc.createCircleGraph();
                        mc.createClocks();

                    }
                }
            }
        });
    }

    // changing and presenting plane icon direction towards its location
    public void direction(double longitude, double latitude) {
        prev_i = current_i;
        prev_j = current_j;
        current_i = longitude;
        current_j = latitude;
        double delta_x = Math.abs(current_j - prev_j);
        double delta_y = Math.abs(current_i - prev_i);
        angle = Math.toDegrees(Math.atan(delta_y) / (delta_x)) - 180.0;
    }

    int zoomCounter = 0;
    GraphicsContext canvas;

    //   public void zoomIn()
//   {
//       System.out.println("zoomIn");
//    //this.img1.setScaleX(1.1);
//    //this.img1.setScaleY(1.1);
//    this.canvas.scale(1.1,1.1);
//   }
//   public void zoomOut()
//   {
//       System.out.println("zoomOut");
//       if (zoomCounter<5)
//       {
//           this.canvas.clearRect(0,0,1000,1000);
//           this.canvas.scale(0.9,0.9);
//           //this.createPlaneView(mapHeight,mapWidth);
//       }
//        zoomCounter++;
//   }

    public void zoomMap()
    {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        String path = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\planesmap.gif";
        Image source = new Image(path);
        img1 = new ImageView(source);

        img1.setPreserveRatio(false);
        img1.setFitWidth(850);
        img1.setFitHeight(495);
        height = (int) source.getHeight();
        width = (int) source.getWidth();
        System.out.println("height = "+height+"\nwidth = "+width);
        HBox zoom = new HBox(10);
        zoom.setAlignment(Pos.CENTER);

        Slider zoomLvl = new Slider();
        zoomLvl.setMax(2);
        zoomLvl.setMin(1);
        zoomLvl.setMaxWidth(200);
        zoomLvl.setMinWidth(200);
        Label hint = new Label("Zoom Level");
        Label value = new Label("1.0");

        offSetX = width/2;
        offSetY = height/2;



        zoom.getChildren().addAll(hint,zoomLvl,value);
        BorderPane imageView = new BorderPane();
        imageView.setCenter(img1);

        zoomLvl.valueProperty().addListener(e->{
            zoomlvl = zoomLvl.getValue();
            double newValue = (double)((int)(zoomlvl*10))/10;
            value.setText(newValue+"");
            if(offSetX<(width/newValue)/2) {
                offSetX = (width/newValue)/2;
            }
            if(offSetX>width-((width/newValue)/2)) {
                offSetX = width-((width/newValue)/2);
            }
            if(offSetY<(height/newValue)/2) {
                offSetY = (height/newValue)/2;
            }
            if(offSetY>height-((height/newValue)/2)) {
                offSetY = height-((height/newValue)/2);
            }
            mapHeight = 590 / newValue;
            mapWidth = 750 / newValue;

            //update planes location
            updateVisuals(lastAD);
            Rectangle2D rec = new Rectangle2D(offSetX-((width/newValue)/2), offSetY-((height/newValue)/2), width/newValue, height/newValue);
//            System.out.println(rec.getHeight());
//            System.out.println(rec.getWidth());



            img1.setViewport(rec);
        });

//        root.getChildren().addAll(imageView,zoom);
        worldMapPane.getChildren().addAll(imageView,zoom);
//        View = new Scene(root,(img1.getFitWidth())+70,(image.getFitHeight())+150);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       // this.canvas = this.worldMapCanvas.getGraphicsContext2D();


        String refreshBtnPath = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\refreshBtn.png";
        refreshBtn.setImage(new Image(refreshBtnPath));

//        String mapImgPath = System.getProperty("user.dir") + "\\Frontend\\src\\main\\resources\\icons\\planesmap.gif";
//        img1.setImage(new Image(mapImgPath));
        zoomMap();
        System.out.println(mapHeight);
        System.out.println(mapWidth);
        AnalyticsData ad = new AnalyticsData();
        ArrayList<PlaneAnalytic> list = new ArrayList<>();
        ad.analyticList = list;

        PlaneAnalytic p1 = new PlaneAnalytic();
        PlaneAnalytic p2 = new PlaneAnalytic();

        p1._id = "0";
        p1.Name = "pilot0";
        p2._id = "1";
        p2.Name = "pilot1";


        p1.planeData = new PlaneData();
        p2.planeData = new PlaneData();

        p1.planeData.latitude = "51.507351"; //London
        p1.planeData.longitude = "-0.127758"; //London
        p2.planeData.latitude = "-14.235004"; //Lebanon
        p2.planeData.longitude = "-51.925282"; //Lebanon


        p1.planeData.ID = "0";
        p1.planeData.PlaneName = "Plane 0";
        p1.planeData.heading = "312.332";
        p1.planeData.altitude = "1231312";
        p1.planeData.airSpeed_kt = "33333";

        p2.planeData.ID = "1";
        p2.planeData.PlaneName = "Plane 1";
        p2.planeData.heading = "312.332";
        p2.planeData.altitude = "1231312";
        p2.planeData.airSpeed_kt = "33333";

        p1.miles = new HashMap<>();
        p1.miles.put("May",30.6);
        p1.miles.put("June",99.0);
        p2.miles = new HashMap<>();
        p2.miles.put("May",30.6);
        p2.miles.put("June",99.0);

        lastAD = ad;
        //Adding Planes To the plane List
        list.add(p1);
        list.add(p2);

        //-----Testing Drawing Planes-----//
        updateVisuals(ad);
//        latLongToOffsets(61.524010f, 105.318756f, 780, 625);


        //----------Testing direction function----------//
//        direction(21.546700, 39.194839);
//        airp.setRotate(angle);

        //----------------------------Graphs tests----------------------------//

        //---------singleSortedMiles Test2----------// Mapping plane id to miles
        HashMap<Integer, Double> test1New = new HashMap<>();
        test1New.put(5, 250.0);
        test1New.put(8, 50.0);
        test1New.put(7, 100.0);
        singleSortedMiles(test1New);

        //Test for multiple bar
//        HashMap<Integer, List<Integer>> test2 = new HashMap<>();
//        test2.put(4, Arrays.asList(44, 59, 39)); // 47.3
//        test2.put(1, Arrays.asList(77, 14, 9)); //33.3
//        test2.put(6, Arrays.asList(1, 8, 1)); //3.3
//        multipleSortedMiles2(test2);


        //---------Line Chart----------// Mapping month number to fleet size
        HashMap<Integer, Integer> testLineChart = new HashMap<>();
        testLineChart.put(5, 250);
        testLineChart.put(8, 50);
        testLineChart.put(7, 100);
        lineChart(testLineChart);

        activePlanes(0);

//        System.out.println("w: " + img1.getFitWidth() + " h: " + img1.getFitHeight());
    }

}               