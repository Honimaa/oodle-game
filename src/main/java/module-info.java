module com.example.oodlegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oodlegame to javafx.fxml;
    exports com.example.oodlegame;
}