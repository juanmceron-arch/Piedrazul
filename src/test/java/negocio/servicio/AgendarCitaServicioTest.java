/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;
import dominio.modelo.Especialista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soporte.DatosPrueba;
import soporte.DoblesPrueba.FakeCitaRepository;
import soporte.DoblesPrueba.FakeEspecialistaRepository;
import soporte.DoblesPrueba.FakePacienteRepository;
import negocio.DTO.AgendarCitaPeticion;
import negocio.servicio.AgendarCitaServicio;
import negocio.servicio.ResultadoAgendamiento;
import negocio.validacion.AgendarCitaValidacion;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ramir
 */


class AgendarCitaServicioTest {

    private FakeCitaRepository citaRepository;
    private FakePacienteRepository pacienteRepository;
    private FakeEspecialistaRepository especialistaRepository;
    private AgendarCitaServicio servicio;

    @BeforeEach
    void setUp() {
        citaRepository = new FakeCitaRepository();
        pacienteRepository = new FakePacienteRepository();
        especialistaRepository = new FakeEspecialistaRepository();
        especialistaRepository.guardar(new Especialista("ESP1", "Ana Ruiz", "Terapia"));

        servicio = new AgendarCitaServicio(
                citaRepository,
                pacienteRepository,
                especialistaRepository,
                new AgendarCitaValidacion()
        );
    }

    @Test
    void debe_agendar_cita_exitosamente()throws Exception{
        AgendarCitaPeticion request = DatosPrueba.crearAgendarRequestValido();

        ResultadoAgendamiento resultado = servicio.agendar(request);

        assertTrue(resultado.isExitoso());
        assertEquals("Cita agendada con exito", resultado.getMensaje());
        assertEquals(1, citaRepository.listarTodasCitas().size());
        assertEquals(1, pacienteRepository.totalGuardados());
    }

    @Test
    void no_debe_agendar_si_hay_errores_de_validacion()throws Exception {
        AgendarCitaPeticion request = new AgendarCitaPeticion();

        ResultadoAgendamiento resultado = servicio.agendar(request);

        assertFalse(resultado.isExitoso());
        assertFalse(resultado.getErrores().isEmpty());
        assertEquals(0, citaRepository.listarTodasCitas().size());
    }

    @Test
    void no_debe_agendar_si_el_horario_ya_esta_ocupado()throws Exception {
        citaRepository.guardarCita(DatosPrueba.crearCita());
        AgendarCitaPeticion request = DatosPrueba.crearAgendarRequestValido();

        ResultadoAgendamiento resultado = servicio.agendar(request);

        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getErrores().contains("La hora seleccionada no está disponible"));
    }

    @Test
    void debe_lanzar_error_si_el_especialista_no_existe() {
        AgendarCitaPeticion request = DatosPrueba.crearAgendarRequestValido();
        request.especialistaId = "ESP999";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.agendar(request)
        );

        assertEquals("Especialista no encontrado", exception.getMessage());
    }
}