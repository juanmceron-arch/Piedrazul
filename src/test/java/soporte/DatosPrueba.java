/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package soporte;
import dominio.enums.EstadoCita;
import dominio.modelo.Cita;
import dominio.modelo.Especialista;
import dominio.modelo.Paciente;


import java.time.LocalDate;
import java.time.LocalTime;
import negocio.DTO.AgendarCitaPeticion;
import negocio.DTO.ExportarCitas;
import negocio.DTO.ReAgendarCitas;

/**
 *
 * @author ramir
 */



public class DatosPrueba {

    public static Paciente crearPaciente() {
        return new Paciente(
                "P001",
                "Carlos",
                "Lopez",
                "3001234567",
                "Masculino",
                LocalDate.of(1990, 5, 20)
        );
    }

    public static Especialista crearEspecialista() {
        return new Especialista("ESP1", "Ana Ruiz", "Terapia");
    }

    public static Cita crearCita() {
        return new Cita(
                "C001",
                crearPaciente(),
                crearEspecialista(),
                LocalDate.of(2026, 3, 15),
                LocalTime.of(10, 0),
                60,
                EstadoCita.AGENDADA
        );
    }

    public static AgendarCitaPeticion crearAgendarRequestValido() {
        AgendarCitaPeticion request = new AgendarCitaPeticion();
        request.pacienteId = "P001";
        request.nombres = "Carlos";
        request.apellidos = "Lopez";
        request.telefono = "3001234567";
        request.genero = "Masculino";
        request.fechaNacimiento = LocalDate.of(1990, 5, 20);
        request.especialistaId = "ESP1";
        request.fecha = LocalDate.of(2026, 3, 15);
        request.hora = LocalTime.of(10, 0);
        request.duracionMinutos = 60;
        return request;
    }

    public static ExportarCitas crearExportarRequestValido(String rutaArchivo) {
        ExportarCitas request = new ExportarCitas();
        request.fecha = LocalDate.of(2026, 3, 15);
        request.especialistaId = "ESP1";
        request.rutaArchivo = rutaArchivo;
        return request;
    }

    public static ReAgendarCitas crearReagendarRequestValido() {
        ReAgendarCitas request = new ReAgendarCitas();
        request.citaId = "C001";
        request.nuevaFecha = LocalDate.of(2026, 3, 16);
        request.nuevaHora = LocalTime.of(9, 0);
        return request;
    }
}
