/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;
import dominio.modelo.Cita;
import dominio.modelo.Especialista;
import dominio.enums.EstadoCita;
import dominio.modelo.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soporte.DatosPrueba;
import soporte.DoblesPrueba.FakeCitaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import negocio.servicio.HorarioSugeridoServicio;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ramir
 */


class HorarioSugeridoServicioTest {

    private FakeCitaRepository citaRepository;
    private HorarioSugeridoServicio servicio;

    @BeforeEach
    void setUp() {
        citaRepository = new FakeCitaRepository();
        servicio = new HorarioSugeridoServicio(citaRepository);

        citaRepository.guardarCita(DatosPrueba.crearCita());

        Paciente paciente2 = new Paciente("P002", "Laura", "Perez", "3002", "F", LocalDate.of(1991, 1, 1));
        Especialista especialista = new Especialista("ESP1", "Ana Ruiz", "Terapia");

        citaRepository.guardarCita(new Cita(
                "C002",
                paciente2,
                especialista,
                LocalDate.of(2026, 3, 16),
                LocalTime.of(8, 0),
                60,
                EstadoCita.AGENDADA
        ));
    }

    @Test
    void debe_retornar_horarios_disponibles() {
        List<String> horarios = servicio.obtenerHorariosSugeridos("C001", LocalDate.of(2026, 3, 16));

        assertFalse(horarios.isEmpty());
        assertFalse(horarios.contains("08:00"));
        assertTrue(horarios.contains("09:00"));
    }

    @Test
    void debe_fallar_si_la_cita_no_existe() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.obtenerHorariosSugeridos("NO_EXISTE", LocalDate.of(2026, 3, 16)));
    }
}