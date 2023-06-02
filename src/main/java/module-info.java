module com.example.mirroroptimization {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.mirroroptimization to javafx.fxml;
    exports com.example.mirroroptimization;
}