package com.example.oodlegame.controller;

import com.example.oodlegame.model.Usuario;
import com.example.oodlegame.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

        try {

            if(usuarioDAO.duplicateUser(user)){
                showAlert(Alert.AlertType.WARNING, "El nombre de usuario: " + user +" ya existe");
                return;
            } else if (usuarioDAO.duplicateEmail(email)) {
                showAlert(Alert.AlertType.WARNING, "El correo: " + email + " ya tiene una cuenta registrada");
                return;
            }

            Usuario nuevoUsuario = new Usuario();

            nuevoUsuario.setEmail(email);
            nuevoUsuario.setUsername(user);
            nuevoUsuario.setPassword(pass);

            boolean success = usuarioDAO.registrarUsuario(nuevoUsuario);

            if (success){
                showAlert(Alert.AlertType.CONFIRMATION, "Usuario registrado correctamente");
                clear();
            }else {
                showAlert(Alert.AlertType.ERROR, "No se pudo registrar el usuario, email o usuario ya existen");
            }
            }catch (Exception e){
                showAlert(Alert.AlertType.ERROR, "Error en la base de datos");
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
