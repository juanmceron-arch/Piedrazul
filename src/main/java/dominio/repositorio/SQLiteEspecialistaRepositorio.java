/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio.repositorio;

import dominio.modelo.Especialista;
import infrastuctura.conexion.SqlConexionBaseDatos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteEspecialistaRepositorio implements especialistaRepositorio{


    @Override
    public Optional<Especialista> buscarPorId(String id) {
        String sql = "SELECT * FROM especialistas WHERE id = ?";

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Especialista(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getString("especialidad")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando especialista", e);
        }
    }

    @Override
    public List<Especialista> listarTodos() {
        String sql = "SELECT * FROM especialistas ORDER BY nombre";
        List<Especialista> especialistas = new ArrayList<>();

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                especialistas.add(new Especialista(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("especialidad")
                ));
            }

            return especialistas;
        } catch (SQLException e) {
            throw new RuntimeException("Error listando especialistas", e);
        }
    }

    @Override
    public void guardar(Especialista especialista) {
        String sql = """
            INSERT OR REPLACE INTO especialistas(id, nombre, especialidad)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, especialista.getId());
            ps.setString(2, especialista.getNombre());
            ps.setString(3, especialista.getEspecialidad());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error guardando especialista", e);
        }
    }
    
}
    