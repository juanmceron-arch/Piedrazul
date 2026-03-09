/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;
import dominio.modelo.Cita;
import dominio.repositorio.citaRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author ramir
 */


public class ConsultarCitaServicio {

    private final citaRepositorio citaRepo;

    public ConsultarCitaServicio(citaRepositorio citaRepo) {
        this.citaRepo = citaRepo;
    }

  
    public List<Cita> consultarTodas() {
        return citaRepo.listarTodasCitas();
    }
    
     public Optional<Cita> buscarPorId(String id) {
        return citaRepo.buscarPorId(id);
    }

    public List<Cita> filtrar(String paciente, String especialista) {
        return citaRepo.listarTodasCitas().stream()
                .filter(c -> paciente == null || paciente.isBlank()
                        || (c.getPaciente().getNombre() + " " + c.getPaciente().getApellido())
                        .toLowerCase().contains(paciente.toLowerCase()))
                .filter(c -> especialista == null || especialista.isBlank()
                        || c.getEspecialista().getNombre().toLowerCase().contains(especialista.toLowerCase()))
                .collect(Collectors.toList());
    }
}