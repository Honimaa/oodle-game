package com.example.oodlegame.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button btnNewMatch;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnClose;

    @FXML
    private Label welcome;


    public void setUsername(String username){
        welcome.setText("Bienvenido, " + username + "!");
    }


    @FXML private void irPartida(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Partida.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            LoginController LoginController = loader.getController();
            Stage stage = (Stage) btnNewMatch.getScene().getWindow();
            stage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML private void irHistorial(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Historial.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            LoginController LoginController = loader.getController();
            Stage stage = (Stage) btnHistory.getScene().getWindow();
            stage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML private void cerrarSesion(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            LoginController LoginController = loader.getController();
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
