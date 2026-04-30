module com.example.oodlegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;


    opens com.example.oodlegame.controller to javafx.fxml;
    opens com.example.oodlegame to javafx.fxml;
    opens com.example.oodlegame.model to javafx.base;
    exports com.example.oodlegame;
}