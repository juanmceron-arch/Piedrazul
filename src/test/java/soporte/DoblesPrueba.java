/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package soporte;
import dominio.modelo.Cita;
import dominio.modelo.Especialista;
import dominio.modelo.Paciente;
import dominio.repositorio.citaRepositorio;
import dominio.repositorio.especialistaRepositorio;
import dominio.repositorio.PacienteRepositorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author ramir
 */

public class DoblesPrueba {

    public static class FakeCitaRepository implements citaRepositorio {
        private final Map<String, Cita> storage = new HashMap<>();

        @Override
        public void guardarCita(Cita cita) {
            storage.put(cita.getId(), cita);
        }

        @Override
        public void actualizarCita(Cita cita) {
            storage.put(cita.getId(), cita);
        }

        @Override
        public Optional<Cita> buscarPorId(String id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public List<Cita> listarTodasCitas() {
            return storage.values().stream()
                    .sorted(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora))
                    .collect(Collectors.toList());
        }

        @Override
        public List<Cita> buscarPorFechaYEspecialista(LocalDate fecha, String especialistaId) {
            return storage.values().stream()
                    .filter(c -> c.getFecha().equals(fecha))
                    .filter(c -> c.getEspecialista().getId().equals(especialistaId))
                    .sorted(Comparator.comparing(Cita::getHora))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean existeCitaEnHorario(String especialistaId, LocalDate fecha, LocalTime hora) {
            return storage.values().stream()
                    .anyMatch(c ->
                            c.getEspecialista().getId().equals(especialistaId)
                            && c.getFecha().equals(fecha)
                            && c.getHora().equals(hora)
                            && !"CANCELADA".equals(c.getEstadoCita().name())
                    );
        }
    }

    public static class FakePacienteRepository implements PacienteRepositorio {
        private final Map<String, Paciente> storage = new HashMap<>();

        @Override
        public void guardar(Paciente paciente) {
            storage.put(paciente.getId(), paciente);
        }

        @Override
        public Optional<Paciente> buscarPorId(String id) {
            return Optional.ofNullable(storage.get(id));
        }

        public int totalGuardados() {
            return storage.size();
        }
    }

    public static class FakeEspecialistaRepository implements especialistaRepositorio {
        private final Map<String, Especialista> storage = new HashMap<>();

        @Override
        public Optional<Especialista> buscarPorId(String id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public List<Especialista> listarTodos() {
            return new ArrayList<>(storage.values());
        }

        @Override
        public void guardar(Especialista especialista) {
            storage.put(especialista.getId(), especialista);
        }
    }
}
