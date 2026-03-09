/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dominio.repositorio;

import dominio.modelo.Cita;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ramir
 */
public interface citaRepositorio {
    
    void guardarCita(Cita cita)throws SQLException;
    void actualizarCita(Cita cita);
    Optional<Cita>  buscarPorId(String id);
    List<Cita> listarTodasCitas();
    List<Cita> buscarPorFechaYEspecialista(LocalDate fecha,String especialista);
    boolean existeCitaEnHorario(String especialista,LocalDate fecha, LocalTime hora);
}


