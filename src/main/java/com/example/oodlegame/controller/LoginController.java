package com.example.oodlegame.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtMail;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink btnRegistrar;



    @FXML private void irRegistrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Registro.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            RegistroController registroController = loader.getController();
            Stage stage = (Stage) btnRegistrar.getScene().getWindow();
            stage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
