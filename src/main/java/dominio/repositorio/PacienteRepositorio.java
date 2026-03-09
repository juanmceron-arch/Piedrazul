/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dominio.repositorio;

import dominio.modelo.Paciente;
import java.util.Optional;

/**
 *
 * @author ramir
 */
public interface PacienteRepositorio {
    void guardar(Paciente paciente);
    Optional<Paciente> buscarPorId(String id);
}
