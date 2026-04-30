package com.example.oodlegame.service;

import com.example.oodlegame.model.Partida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


public class PartidaDAO {

    public boolean guardarPartida(Partida partida) throws SQLException {
        String sql = "insert into partidas (usuario_id, ecuacion_objetivo, intentos_usados, victoria, fecha)" +
                    "values (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, partida.getUsuario().getId());
            stmt.setString(2, partida.getEcuacionObjetivo());
            stmt.setInt(3, partida.getIntentosUsados());
            stmt.setBoolean(4, partida.isVictoria());
            stmt.setTimestamp(5, Timestamp.valueOf(partida.getFecha()));

            stmt.executeUpdate();
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return true;
        }
    }

    public List <Partida> obtenerPartidasUsuario(int usuarioId) {

        List <Partida> lista = new ArrayList<>();

        String sql = "select * from partidas where usuario_id = ? order by fecha desc";

        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, usuarioId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                Partida partida = new Partida();
                partida.setId(rs.getInt("id"));
                partida.setEcuacionObjetivo(rs.getString("ecuacion_objetivo"));
                partida.setIntentosUsados(rs.getInt("intentos_usados"));
                partida.setVictoria(rs.getBoolean("victoria"));
                partida.setFecha(rs.getTimestamp("fecha").toLocalDateTime());

                lista.add(partida);
            }

        }catch (SQLException e){
            e.printStackTrace();
            return obtenerPartidasDemo();
        }

        return lista;
    }

    private List<Partida> obtenerPartidasDemo() {
        List<Partida> lista = new ArrayList<>();

        lista.add(new Partida(1, null, "8/8*9+6=15", 5, true, LocalDateTime.now().minusDays(1)));
        lista.add(new Partida(2, null, "2*3+4-2=8", 6, false, LocalDateTime.now().minusDays(2)));
        lista.add(new Partida(3, null, "9-4+2*3=11", 4, true, LocalDateTime.now().minusDays(3)));
        lista.add(new Partida(4, null, "7+2*3-5=8", 4, true, LocalDateTime.now().minusDays(4)));

        return lista;
    }

}
