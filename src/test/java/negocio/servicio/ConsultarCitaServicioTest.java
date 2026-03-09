/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;
import dominio.enums.EstadoCita;
import dominio.modelo.Cita;
import dominio.modelo.Especialista;
import dominio.modelo.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soporte.DoblesPrueba.FakeCitaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import negocio.servicio.ConsultarCitaServicio;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ramir
 */

class ConsultarCitaServicioTest {

    private FakeCitaRepository citaRepository;
    private ConsultarCitaServicio servicio;

    @BeforeEach
    void setUp() {
        citaRepository = new FakeCitaRepository();
        servicio = new ConsultarCitaServicio(citaRepository);

        Paciente paciente1 = new Paciente("P001", "Carlos", "Lopez", "3001", "M", LocalDate.of(1990, 1, 1));
        Paciente paciente2 = new Paciente("P002", "Laura", "Perez", "3002", "F", LocalDate.of(1991, 1, 1));

        Especialista especialista1 = new Especialista("ESP1", "Ana Ruiz", "Terapia");
        Especialista especialista2 = new Especialista("ESP2", "Carlos Gomez", "Fisioterapia");

        citaRepository.guardarCita(new Cita("C002", paciente2, especialista2, LocalDate.of(2026, 3, 20), LocalTime.of(11, 0), 60, EstadoCita.AGENDADA));
        citaRepository.guardarCita(new Cita("C001", paciente1, especialista1, LocalDate.of(2026, 3, 15), LocalTime.of(10, 0), 60, EstadoCita.AGENDADA));
    }

    @Test
    void debe_consultar_todas_las_citas_ordenadas() {
        List<Cita> citas = servicio.consultarTodas();

        assertEquals(2, citas.size());
        assertEquals("C001", citas.get(0).getId());
        assertEquals("C002", citas.get(1).getId());
    }

    @Test
    void debe_buscar_cita_por_id() {
        assertTrue(servicio.buscarPorId("C001").isPresent());
        assertTrue(servicio.buscarPorId("NO_EXISTE").isEmpty());
    }

    @Test
    void debe_filtrar_por_nombre_de_paciente() {
        List<Cita> citas = servicio.filtrar("carlos", "");

        assertEquals(1, citas.size());
        assertEquals("C001", citas.get(0).getId());
    }

    @Test
    void debe_filtrar_por_nombre_de_especialista() {
        List<Cita> citas = servicio.filtrar("", "ana");

        assertEquals(1, citas.size());
        assertEquals("C001", citas.get(0).getId());
    }
}
