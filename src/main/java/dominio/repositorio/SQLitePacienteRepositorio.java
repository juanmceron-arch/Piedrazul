/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio.repositorio;

import dominio.modelo.Paciente;
import infrastuctura.conexion.SqlConexionBaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 *
 * @author ramir
 */
public class SQLitePacienteRepositorio implements PacienteRepositorio{
    
      
    
     @Override
    public void guardar(Paciente paciente) {
        String sql = """
            INSERT OR REPLACE INTO pacientes(id, nombres, apellidos, telefono, genero, fecha_nacimiento)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getId());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getTelefono());
            ps.setString(5, paciente.getGenero());
            ps.setString(6, paciente.getFechaNacimiento().toString());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error guardando paciente", e);
        }
    }

    @Override
    public Optional<Paciente> buscarPorId(String id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = new Paciente(
                            rs.getString("id"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("telefono"),
                            rs.getString("genero"),
                            java.time.LocalDate.parse(rs.getString("fecha_nacimiento"))
                    );
                    return Optional.of(paciente);
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error buscando paciente", e);
        }
    }
}
