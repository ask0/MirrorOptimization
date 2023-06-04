module com.example.mirroroptimization {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.mirroroptimization to javafx.fxml;
    exports com.example.mirroroptimization;
}