/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;

import dominio.enums.EstadoCita;
import dominio.modelo.Cita;
import dominio.modelo.Especialista;
import dominio.modelo.Paciente;
import dominio.repositorio.PacienteRepositorio;
import dominio.repositorio.citaRepositorio;
import dominio.repositorio.especialistaRepositorio;
import java.sql.SQLException;

import java.util.List;
import java.util.UUID;
import negocio.DTO.AgendarCitaPeticion;
import negocio.validacion.AgendarCitaValidacion;

/**
 *
 * @author ramir
 */
public class AgendarCitaServicio {

    private final citaRepositorio citaRepo;
    private final PacienteRepositorio pacienteRepo;
    private final especialistaRepositorio especialiRepo;
    private final AgendarCitaValidacion validacion;

    public AgendarCitaServicio(citaRepositorio citaRepo, PacienteRepositorio pacienteRepo, especialistaRepositorio especialiRepo, AgendarCitaValidacion validacion) {
        this.citaRepo = citaRepo;
        this.pacienteRepo = pacienteRepo;
        this.especialiRepo = especialiRepo;
        this.validacion = validacion;
    }

    public ResultadoAgendamiento agendar(AgendarCitaPeticion peticion) throws SQLException{
        List<String> errores = validacion.validar(peticion);

        if (!errores.isEmpty()) {
            return ResultadoAgendamiento.conErrores(errores);
        }

        if (citaRepo.existeCitaEnHorario(peticion.especialistaId, peticion.fecha, peticion.hora)) {
            return ResultadoAgendamiento.conErrores(
                    List.of("La hora seleccionada no está disponible")
            );
        }

        Especialista especialista = especialiRepo.buscarPorId(peticion.especialistaId)
                .orElseThrow(() -> new IllegalArgumentException("Especialista no encontrado"));

        Paciente paciente = new Paciente(
                peticion.pacienteId,
                peticion.nombres,
                peticion.apellidos,
                peticion.telefono,
                peticion.genero,
                peticion.fechaNacimiento);

        pacienteRepo.guardar(paciente);

        Cita cita = new Cita(
                UUID.randomUUID().toString(),
                paciente,
                especialista,
                peticion.fecha,
                peticion.hora,
                peticion.duracionMinutos,
                EstadoCita.AGENDADA);
        
        citaRepo.guardarCita(cita);

        return ResultadoAgendamiento.exitoso("Cita agendada con exito", cita);
    }

}
