package com.example.oodlegame.controller;

import com.example.oodlegame.service.UsuarioDAO;
import com.example.oodlegame.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink btnRegistrar;


    @FXML private void validarLogin() {

        String email = txtEmail.getText();
        String password = txtPassword.getText();
        int intentos = 0;

        if (email.isEmpty() || password.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Complete todos los campos");
            return;
        }

        try {

            byte[] dig = MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes(StandardCharsets.UTF_8));
            String passHash = HexFormat.of().formatHex(dig);

            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuarioEncontrado = dao.buscarEmail(email);

            if (usuarioEncontrado != null && usuarioEncontrado.getPassword().equals(passHash)){
                showAlert(Alert.AlertType.CONFIRMATION, "Bienvenido " + usuarioEncontrado.getUsername());
                irMenu(usuarioEncontrado.getUsername());
            } else {
                showAlert(Alert.AlertType.ERROR, "Correo o contraseña incorrectos");
                clear();

                intentos ++;
                if (intentos >= 3){
                    btnLogin.setDisable(true);
                    showAlert(Alert.AlertType.ERROR, "Demasiados intentos, aplicacion bloqueada");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al iniciar sesion, intente nuevamente");
        }
    }


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


    private void irMenu(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            MenuController menuController = loader.getController();
            menuController.setUsername(username);
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String msg){
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }


    private void clear(){
        txtEmail.clear();
        txtPassword.clear();
    }
}
