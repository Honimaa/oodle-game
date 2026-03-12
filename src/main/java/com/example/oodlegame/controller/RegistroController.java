package com.example.oodlegame.controller;

import com.example.oodlegame.model.Usuario;
import com.example.oodlegame.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class RegistroController {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnNewUser;


    @FXML
    private void Registrar(){

        String email = txtEmail.getText();
        String user = txtUser.getText();
        String pass = txtPassword.getText();
        String npass = txtConfirmPassword.getText();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (email.isEmpty() || user.isEmpty() || pass.isEmpty() || npass.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Completa todos los campos");
            return;
        }

        if (pass.length()>6){
            showAlert(Alert.AlertType.WARNING, "La contraseña debe tener al menos 6 caracteres");
            return;
        }

        try {

            if(usuarioDAO.duplicateUser(user)){
                showAlert(Alert.AlertType.WARNING, "El nombre de usuario: " + user +" ya existe");
                return;
            } else if (usuarioDAO.duplicateEmail(email)) {
                showAlert(Alert.AlertType.WARNING, "El correo: " + email + " ya tiene una cuenta registrada");
                return;
            }

            Usuario nuevoUsuario = new Usuario();

            //metodo para hashear la contrasena
            byte[] dig = MessageDigest.getInstance("SHA-256")
                    .digest(pass.getBytes(StandardCharsets.UTF_8));
            String passHash = HexFormat.of().formatHex(dig);

            nuevoUsuario.setEmail(email);
            nuevoUsuario.setUsername(user);
            nuevoUsuario.setPassword(passHash);

            boolean success = usuarioDAO.registrarUsuario(nuevoUsuario);

            if (success){
                showAlert(Alert.AlertType.CONFIRMATION, "Usuario registrado correctamente");
                clear();
                irLogin();
            }else {
                showAlert(Alert.AlertType.ERROR, "No se pudo registrar el usuario, email o usuario ya existen");
            }
            }catch (Exception e){
                showAlert(Alert.AlertType.ERROR, "Error en la base de datos");
        }
    }

    @FXML private void irLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            LoginController LoginController = loader.getController();
            Stage stage = (Stage) btnNewUser.getScene().getWindow();
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
        txtUser.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
    }
}
