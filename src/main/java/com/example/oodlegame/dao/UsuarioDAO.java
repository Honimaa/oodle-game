package com.example.oodlegame.dao;

import com.example.oodlegame.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean registrarUsuario(Usuario usuario) throws SQLException {
        String sql = "insert into usuarios (email, username, password) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getUsername());
            stmt.setString(3, usuario.getPassword());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarEmail(String email){
        String sql = "select * from usuarios where email = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));

                return usuario;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validaLogin(String email, String password){
        Usuario usuario = buscarEmail(email);

        if (usuario == null){
            return false;
        }

        return usuario.getPassword().equals(password);
    }
}
