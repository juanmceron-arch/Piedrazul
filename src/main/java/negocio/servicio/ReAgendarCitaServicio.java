/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;

import dominio.modelo.Cita;
import dominio.repositorio.citaRepositorio;
import java.util.List;
import negocio.DTO.ReAgendarCitas;

/**
 *
 * @author ramir
 */
public class ReAgendarCitaServicio {
      private final citaRepositorio citaRepo;

    public ReAgendarCitaServicio(citaRepositorio citaRepo) {
        this.citaRepo = citaRepo;
    }

       

    public ResultadoReAgendamiento reagendar(ReAgendarCitas request) {
        Cita cita = citaRepo.buscarPorId(request.citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        boolean ocupado = citaRepo.existeCitaEnHorario(
                cita.getEspecialista().getId(),
                request.nuevaFecha,
                request.nuevaHora
        );

        if (ocupado) {
            return ResultadoReAgendamiento.conErrores(List.of("La nueva hora no está disponible"));
        }

        cita.reagendar(request.nuevaFecha, request.nuevaHora);
        citaRepo.actualizarCita(cita);

        return ResultadoReAgendamiento.exitoso("Cita reagendada exitosamente");
    }
}
