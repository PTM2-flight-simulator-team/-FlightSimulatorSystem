module com.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires eu.hansolo.medusa;


    opens com.example.frontend to javafx.fxml;
    exports com.example.frontend;
    exports com.example.frontend.windowController;
    opens com.example.frontend.windowController to javafx.fxml;
    exports Model;
    opens Model to javafx.fxml;
    exports Model.dataHolder to com.google.gson;


}