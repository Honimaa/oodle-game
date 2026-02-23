module com.example.oodlegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.oodlegame to javafx.fxml;
    exports com.example.oodlegame;
}