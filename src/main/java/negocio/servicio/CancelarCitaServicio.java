/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;

import dominio.modelo.Cita;
import dominio.repositorio.citaRepositorio;

/**
 *
 * @author ramir
 */

public class CancelarCitaServicio {

    private final citaRepositorio citaRepo;

    public CancelarCitaServicio(citaRepositorio citaRepository) {
        this.citaRepo = citaRepository;
    }

    public String cancelar(String citaId) {
        Cita cita = citaRepo.buscarPorId(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        cita.cancelar();
        citaRepo.actualizarCita(cita);

        return "Cita cancelada exitosamente";
    }
}