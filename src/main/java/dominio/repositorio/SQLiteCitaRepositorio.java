/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio.repositorio;

import dominio.enums.EstadoCita;
import dominio.modelo.Cita;
import dominio.modelo.Especialista;
import dominio.modelo.Paciente;
import infrastuctura.conexion.SqlConexionBaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ramir
 */


public class SQLiteCitaRepositorio implements citaRepositorio {

   

    @Override
    public void guardarCita(Cita cita) {
        String sql = """
            INSERT INTO citas(id, paciente_id, especialista_id, fecha, hora, duracion_minutos, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cita.getId());
            ps.setString(2, cita.getPaciente().getId());
            ps.setString(3, cita.getEspecialista().getId());
            ps.setString(4, cita.getFecha().toString());
            ps.setString(5, cita.getHora().toString());
            ps.setInt(6, cita.getDuracionMinutos());
            ps.setString(7, cita.getEstadoCita().name());
            ps.executeUpdate();

        } catch (Exception e) {
            //throw new RuntimeException("Error guardando cita", e);
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarCita(Cita cita) {
        String sql = """
            UPDATE citas
            SET fecha = ?, hora = ?, duracion_minutos = ?, estado = ?
            WHERE id = ?
        """;

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cita.getFecha().toString());
            ps.setString(2, cita.getHora().toString());
            ps.setInt(3, cita.getDuracionMinutos());
            ps.setString(4, cita.getEstadoCita().name());
            ps.setString(5, cita.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando cita", e);
        }
    }

    @Override
    public Optional<Cita> buscarPorId(String id) {
        String sql = """
            SELECT c.id as cita_id, c.fecha, c.hora, c.duracion_minutos, c.estado,
                   p.id as paciente_id, p.nombres, p.apellidos, p.telefono, p.genero, p.fecha_nacimiento,
                   e.id as especialista_id, e.nombre as especialista_nombre, e.especialidad
            FROM citas c
            JOIN pacientes p ON c.paciente_id = p.id
            JOIN especialistas e ON c.especialista_id = e.id
            WHERE c.id = ?
        """;

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCita(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando cita", e);
        }
    }

    @Override
    public List<Cita> listarTodasCitas() {
        String sql = """
            SELECT c.id as cita_id, c.fecha, c.hora, c.duracion_minutos, c.estado,
                   p.id as paciente_id, p.nombres, p.apellidos, p.telefono, p.genero, p.fecha_nacimiento,
                   e.id as especialista_id, e.nombre as especialista_nombre, e.especialidad
            FROM citas c
            JOIN pacientes p ON c.paciente_id = p.id
            JOIN especialistas e ON c.especialista_id = e.id
            ORDER BY c.fecha, c.hora
        """;

        List<Cita> citas = new ArrayList<>();

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }
            return citas;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error listando citas", e);
        }
    }

    @Override
    public List<Cita> buscarPorFechaYEspecialista(LocalDate fecha, String especialistaId) {
        String sql = """
            SELECT c.id as cita_id, c.fecha, c.hora, c.duracion_minutos, c.estado,
                   p.id as paciente_id, p.nombres, p.apellidos, p.telefono, p.genero, p.fecha_nacimiento,
                   e.id as especialista_id, e.nombre as especialista_nombre, e.especialidad
            FROM citas c
            JOIN pacientes p ON c.paciente_id = p.id
            JOIN especialistas e ON c.especialista_id = e.id
            WHERE c.fecha = ? AND e.id = ?
            ORDER BY c.hora
        """;

        List<Cita> citas = new ArrayList<>();

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fecha.toString());
            ps.setString(2, especialistaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapearCita(rs));
                }
            }

            return citas;
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando citas por fecha y especialista", e);
        }
    }

    @Override
    public boolean existeCitaEnHorario(String especialistaId, LocalDate fecha, LocalTime hora) {
        String sql = """
            SELECT COUNT(*) 
            FROM citas
            WHERE especialista_id = ?
              AND fecha = ?
              AND hora = ?
              AND estado <> 'CANCELADA'
        """;

        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, especialistaId);
            ps.setString(2, fecha.toString());
            ps.setString(3, hora.toString());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error validando horario disponible", e);
        }
    }

private Cita mapearCita(ResultSet rs) throws SQLException {
    Paciente paciente = new Paciente(
            rs.getString("paciente_id"),
            rs.getString("nombres"),
            rs.getString("apellidos"),
            rs.getString("telefono"),
            rs.getString("genero"),
            java.time.LocalDate.parse(rs.getString("fecha_nacimiento"))
    );

    Especialista especialista = new Especialista(
            rs.getString("especialista_id"),
            rs.getString("especialista_nombre"),
            rs.getString("especialidad")
    );

    return new Cita(
            rs.getString("cita_id"),
            paciente,
            especialista,
            java.time.LocalDate.parse(rs.getString("fecha")),
            java.time.LocalTime.parse(rs.getString("hora")),
            rs.getInt("duracion_minutos"),
            EstadoCita.valueOf(rs.getString("estado"))
    );
}
    
    
}
