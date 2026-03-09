/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio.modelo;

import java.time.LocalDate;

/**
 *
 * @author ramir
 */
public class Paciente {
    private final String id;
    private final String nombre;
    private final String apellido;
    private final String telefono;
    private final String genero;
    private final LocalDate fechaNacimiento;

    public Paciente(String id, String nombre, String apellido, String telefono, String genero, LocalDate fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGenero() {
        return genero;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

 
   
    
}
