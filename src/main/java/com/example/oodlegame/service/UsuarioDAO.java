package com.example.oodlegame.service;

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

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

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


    public boolean duplicateUser(String username){
        String sql = "select count(*) from usuarios where username = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public boolean duplicateEmail(String email){
        String sql = "select count(*) from usuarios where email = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
