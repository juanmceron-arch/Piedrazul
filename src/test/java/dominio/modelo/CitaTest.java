/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio.modelo;
import dominio.enums.EstadoCita;
import dominio.modelo.Cita;
import soporte.DatosPrueba;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author ramir
 */



class CitaTest {

    @Test
    void debe_cancelar_cita_cambiando_estado() {
        Cita cita = DatosPrueba.crearCita();

        cita.cancelar();

        assertEquals(EstadoCita.CANCELADA, cita.getEstadoCita());
    }

    @Test
    void debe_reagendar_cita_cambiando_fecha_hora_y_estado() {
        Cita cita = DatosPrueba.crearCita();
        LocalDate nuevaFecha = LocalDate.of(2026, 3, 20);
        LocalTime nuevaHora = LocalTime.of(15, 0);

        cita.reagendar(nuevaFecha, nuevaHora);

        assertEquals(nuevaFecha, cita.getFecha());
        assertEquals(nuevaHora, cita.getHora());
        assertEquals(EstadoCita.REAGENDADA, cita.getEstadoCita());
    }
}
