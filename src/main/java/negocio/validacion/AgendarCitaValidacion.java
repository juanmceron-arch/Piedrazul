/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.validacion;

import java.util.ArrayList;
import java.util.List;
import negocio.DTO.AgendarCitaPeticion;

/**
 *
 * @author ramir
 */
public class AgendarCitaValidacion {
    
    private boolean campoVacio(String valor){
        return valor ==null || valor.trim().isEmpty();
    }
    public List<String> validar(AgendarCitaPeticion peticionValida){
        List<String> errores=new ArrayList<>();
        
        if(campoVacio(peticionValida.pacienteId))errores.add("El ID del paciente es obligatorio");
        if(campoVacio(peticionValida.nombres)) errores.add("Los nombres son obligatorios");
        if(campoVacio(peticionValida.apellidos))errores.add("Los apellidos son obligatorios");
        if(campoVacio(peticionValida.telefono)) errores.add("El teléfono es obligatorio");
        if(campoVacio(peticionValida.genero)) errores.add("El género es obligatorio");
        if(peticionValida.fechaNacimiento==null) errores.add("La fecha de nacimiento es obligatoria");
        if(campoVacio(peticionValida.especialistaId)) errores.add("El especialista es obligatorio");
        if(peticionValida.fecha ==null) errores.add("La fecha de la cita es obligatoria");
        if(peticionValida.hora==null) errores.add("La hora de la cita es obligatoria");
        
        if(!campoVacio(peticionValida.telefono)&& !peticionValida.telefono.matches("\\d+{7,15}")) errores.add("El teléfono tiene formato inválido");

        if(peticionValida.duracionMinutos<=0) errores.add("La duración debe ser mayor a cero");

        return errores;
        
        
    }
}
