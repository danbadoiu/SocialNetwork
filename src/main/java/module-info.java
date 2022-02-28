module com.example.dani {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.dani to javafx.fxml;
    exports com.example.dani;
    exports com.example.controller;
    opens com.example.controller to javafx.fxml;
}