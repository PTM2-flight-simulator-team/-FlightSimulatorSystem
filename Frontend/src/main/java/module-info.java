module com.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.frontend to javafx.fxml;
    exports com.example.frontend;
    exports com.example.frontend.windowController;
    opens com.example.frontend.windowController to javafx.fxml;
    exports Model;
    opens Model to javafx.fxml;
}