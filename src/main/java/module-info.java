module com.example.oodlegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;


    opens com.example.oodlegame.controller to javafx.fxml;
    opens com.example.oodlegame to javafx.fxml;
    exports com.example.oodlegame;
}