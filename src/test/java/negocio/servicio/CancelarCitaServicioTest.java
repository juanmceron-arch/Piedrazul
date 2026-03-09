/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;

/**
 *
 * @author ramir
 */

import dominio.enums.EstadoCita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soporte.DatosPrueba;
import soporte.DoblesPrueba.FakeCitaRepository;
import negocio.servicio.CancelarCitaServicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CancelarCitaServicioTest {

    private FakeCitaRepository citaRepository;
    private CancelarCitaServicio servicio;

    @BeforeEach
    void setUp() {
        citaRepository = new FakeCitaRepository();
        servicio = new CancelarCitaServicio(citaRepository);
        citaRepository.guardarCita(DatosPrueba.crearCita());
    }

    @Test
    void debe_cancelar_cita_exitosamente() {
        String mensaje = servicio.cancelar("C001");

        assertEquals("Cita cancelada exitosamente", mensaje);
        assertEquals(EstadoCita.CANCELADA, citaRepository.buscarPorId("C001").orElseThrow().getEstadoCita());
    }

    @Test
    void debe_fallar_si_la_cita_no_existe() {
        assertThrows(IllegalArgumentException.class, () -> servicio.cancelar("NO_EXISTE"));
    }
}
