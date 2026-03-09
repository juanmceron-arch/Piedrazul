/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.controlador;

import dominio.modelo.Cita;
import java.time.LocalDate;
import java.util.List;
import negocio.DTO.AgendarCitaPeticion;
import negocio.DTO.ExportarCitas;
import negocio.DTO.ReAgendarCitas;
import negocio.servicio.AgendarCitaServicio;
import negocio.servicio.CancelarCitaServicio;
import negocio.servicio.ConsultarCitaServicio;
import negocio.servicio.ExportarCitasServicio;
import negocio.servicio.HorarioSugeridoServicio;
import negocio.servicio.ReAgendarCitaServicio;
import negocio.servicio.ResultadoAgendamiento;
import negocio.servicio.ResultadoExportacion;
import negocio.servicio.ResultadoReAgendamiento;
import presentacion.modelo.citaModelo;

/**
 *
 * @author ramir
 */
public class citaControlador {
    

    private final AgendarCitaServicio agendarCitaService;
    private final ConsultarCitaServicio consultarCitaService;
    private final citaModelo model;

    private final CancelarCitaServicio cancelarCitaSer;
    private final ExportarCitasServicio exportarCitasSer;
    private final HorarioSugeridoServicio horarioSugeridoSer;
    private final ReAgendarCitaServicio reagendarCitaSer;

    public citaControlador(AgendarCitaServicio agendarCitaService, ConsultarCitaServicio consultarCitaService, citaModelo model, CancelarCitaServicio cancelarCitaSer, ExportarCitasServicio exportarCitasSer, HorarioSugeridoServicio horarioSugeridoSer, ReAgendarCitaServicio reagendarCitaSer) {
        this.agendarCitaService = agendarCitaService;
        this.consultarCitaService = consultarCitaService;
        this.model = model;
        this.cancelarCitaSer = cancelarCitaSer;
        this.exportarCitasSer = exportarCitasSer;
        this.horarioSugeridoSer = horarioSugeridoSer;
        this.reagendarCitaSer = reagendarCitaSer;
    }


 

 

    public void agendarCita(AgendarCitaPeticion request) {
        try {
            ResultadoAgendamiento resultado = agendarCitaService.agendar(request);

            if (resultado.isExitoso()) {
                List<Cita> citas = consultarCitaService.consultarTodas();
                model.setCitas(citas);
                model.setMensaje(resultado.getMensaje());
            } else {
                model.setErrores(resultado.getErrores());
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.setErrores(List.of("Error al agendar la cita: " + e.getMessage()));
        }
    }

    public void cargarCitas() {
        try {
            List<Cita> citas = consultarCitaService.consultarTodas();
            model.setCitas(citas);
        } catch (Exception e) {
            e.printStackTrace();
            model.setErrores(List.of("Error al consultar citas: " + e.getMessage()));
        }
    }
    
     public void filtrarCitas(String paciente, String especialista) {
        try {
            List<Cita> citas = consultarCitaService.filtrar(paciente, especialista);
            model.setCitas(citas);
        } catch (Exception e) {
            model.setErrores(List.of("Error al filtrar citas: " + e.getMessage()));
        }
    }

    public void cargarDetalleCita(String citaId) {
        try {
            Cita cita = consultarCitaService.buscarPorId(citaId)
                    .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
            model.setCitaSeleccionada(cita);
        } catch (Exception e) {
            model.setErrores(List.of("Error al consultar detalle: " + e.getMessage()));
        }
    }

    public void cancelarCita(String citaId) {
        try {
            String mensaje = cancelarCitaSer.cancelar(citaId);
            cargarCitas();
            model.setMensaje(mensaje);
        } catch (Exception e) {
            model.setErrores(List.of("Error al cancelar cita: " + e.getMessage()));
        }
    }

    public void exportarCitas(ExportarCitas request) {
        try {
            ResultadoExportacion resultado = exportarCitasSer.exportar(request);
            if (resultado.isExitoso()) {
                model.setMensaje(resultado.getMensaje());
            } else {
                model.setErrores(resultado.getErrores());
            }
        } catch (Exception e) {
            model.setErrores(List.of("Error al exportar citas: " + e.getMessage()));
        }
    }

    public void cargarHorariosSugeridos(String citaId, LocalDate fecha) {
        try {
            model.setHorariosSugeridos(horarioSugeridoSer.obtenerHorariosSugeridos(citaId, fecha));
        } catch (Exception e) {
            model.setErrores(List.of("Error al cargar horarios sugeridos: " + e.getMessage()));
        }
    }

    public void reagendarCita(ReAgendarCitas request) {
        try {
            ResultadoReAgendamiento resultado = reagendarCitaSer.reagendar(request);
            if (resultado.isExitoso()) {
                cargarCitas();
                model.setMensaje(resultado.getMensaje());
            } else {
                model.setErrores(resultado.getErrores());
            }
        } catch (Exception e) {
            model.setErrores(List.of("Error al reagendar cita: " + e.getMessage()));
        }
    }
}
    

