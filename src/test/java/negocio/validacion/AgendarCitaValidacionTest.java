/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.validacion;
import soporte.DatosPrueba;
import org.junit.jupiter.api.Test;

import java.util.List;
import negocio.DTO.AgendarCitaPeticion;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author ramir
 */


class AgendarCitaValidacionTest {

    private final AgendarCitaValidacion validador = new AgendarCitaValidacion();

    @Test
    void debe_retornar_sin_errores_cuando_request_es_valido() {
        AgendarCitaPeticion request = DatosPrueba.crearAgendarRequestValido();

        List<String> errores = validador.validar(request);

        assertTrue(errores.isEmpty());
    }

    @Test
    void debe_retornar_errores_cuando_faltan_campos_obligatorios() {
        AgendarCitaPeticion request = new AgendarCitaPeticion();

        List<String> errores = validador.validar(request);

        assertFalse(errores.isEmpty());
        assertTrue(errores.contains("El ID del paciente es obligatorio"));
        assertTrue(errores.contains("Los nombres son obligatorios"));
        assertTrue(errores.contains("Los apellidos son obligatorios"));
    }

    @Test
    void debe_retornar_error_cuando_telefono_es_invalido() {
        AgendarCitaPeticion request = DatosPrueba.crearAgendarRequestValido();
        request.telefono = "abc";

        List<String> errores = validador.validar(request);

        assertTrue(errores.contains("El teléfono tiene formato inválido"));
    }

    @Test
    void debe_retornar_error_cuando_duracion_no_es_valida() {
        AgendarCitaPeticion request = DatosPrueba.crearAgendarRequestValido();
        request.duracionMinutos = 0;

        List<String> errores = validador.validar(request);

        assertTrue(errores.contains("La duración debe ser mayor a cero"));
    }
}