/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;

import dominio.modelo.Cita;
import dominio.repositorio.citaRepositorio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ramir
 */
public class HorarioSugeridoServicio {
     private final citaRepositorio citaRepo;

    public HorarioSugeridoServicio(citaRepositorio citaRepo) {
        this.citaRepo = citaRepo;
    }

 

    public List<String> obtenerHorariosSugeridos(String citaId, LocalDate fecha) {
        Cita cita = citaRepo.buscarPorId(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        String especialistaId = cita.getEspecialista().getId();

        List<String> horarios = new ArrayList<>();
        LocalTime[] base = {
                LocalTime.of(8,0),
                LocalTime.of(9,0),
                LocalTime.of(10,0),
                LocalTime.of(11,0),
                LocalTime.of(14,0),
                LocalTime.of(15,0),
                LocalTime.of(16,0)
        };

        for (LocalTime hora : base) {
            boolean ocupado = citaRepo.existeCitaEnHorario(especialistaId, fecha, hora);
            if (!ocupado) {
                horarios.add(hora.toString());
            }
        }

        return horarios;
    }
}
