/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dominio.repositorio;

import dominio.modelo.Especialista;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ramir
 */
public interface especialistaRepositorio {
    
    Optional<Especialista> buscarPorId(String id);
    List<Especialista> listarTodos();
    void guardar(Especialista especialista);
 }
