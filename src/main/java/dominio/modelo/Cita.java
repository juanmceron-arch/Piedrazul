/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio.modelo;

import dominio.enums.EstadoCita;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ramir
 */
public class Cita {
    
    private final String id;
    private final Paciente paciente;
    private final Especialista especialista;
    private  LocalDate fecha;
    private  LocalTime hora;
    private final int duracionMinutos;
    private EstadoCita estadoCita;

    public Cita(String id, Paciente paciente, Especialista especialista, LocalDate fecha, LocalTime hora, int duracionMinutos, EstadoCita estadoCita) {
        this.id = id;
        this.paciente = paciente;
        this.especialista = especialista;
        this.fecha = fecha;
        this.hora = hora;
        this.duracionMinutos = duracionMinutos;
        this.estadoCita = estadoCita;
    }

    public String getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public EstadoCita getEstadoCita() {
        return estadoCita;
    }
    
    
       public void cancelar() {
        this.estadoCita = EstadoCita.CANCELADA;
    }

    public void reagendar(LocalDate nuevaFecha, LocalTime nuevaHora) {
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        this.estadoCita = EstadoCita.REAGENDADA;
    }
}
