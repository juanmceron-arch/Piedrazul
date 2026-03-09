/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import soporte.DatosPrueba;
import soporte.DoblesPrueba.FakeCitaRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import negocio.servicio.ExportarCitasServicio;
import negocio.servicio.ResultadoExportacion;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ramir
 */

class ExportarCitasServicioTest {

    private FakeCitaRepository citaRepository;
    private ExportarCitasServicio servicio;

    @BeforeEach
    void setUp() {
        citaRepository = new FakeCitaRepository();
        servicio = new ExportarCitasServicio(citaRepository);
        citaRepository.guardarCita(DatosPrueba.crearCita());
    }

    @Test
    void debe_exportar_citas_a_csv(@TempDir Path tempDir) throws Exception {
        Path archivo = tempDir.resolve("citas.csv");

        ResultadoExportacion resultado = servicio.exportar(
                DatosPrueba.crearExportarRequestValido(archivo.toString())
        );

        assertTrue(resultado.isExitoso());
        assertTrue(Files.exists(archivo));

        String contenido = Files.readString(archivo);
        assertTrue(contenido.contains("id,fecha,hora,paciente,especialista,estado"));
        assertTrue(contenido.contains("C001"));
        assertTrue(contenido.contains("Carlos Lopez"));
    }

    @Test
    void debe_retornar_error_si_falta_fecha(@TempDir Path tempDir) {
        var request = DatosPrueba.crearExportarRequestValido(tempDir.resolve("citas.csv").toString());
        request.fecha = null;

        ResultadoExportacion resultado = servicio.exportar(request);

        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getErrores().contains("La fecha es obligatoria"));
    }

    @Test
    void debe_retornar_error_si_falta_especialista(@TempDir Path tempDir) {
        var request = DatosPrueba.crearExportarRequestValido(tempDir.resolve("citas.csv").toString());
        request.especialistaId = "";

        ResultadoExportacion resultado = servicio.exportar(request);

        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getErrores().contains("El terapista es obligatorio"));
    }

    @Test
    void debe_retornar_error_si_no_hay_citas_para_exportar(@TempDir Path tempDir) {
        var request = DatosPrueba.crearExportarRequestValido(tempDir.resolve("vacio.csv").toString());
        request.especialistaId = "ESP999";

        ResultadoExportacion resultado = servicio.exportar(request);

        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getErrores().contains("No hay citas para la fecha y terapista seleccionados"));
    }
}