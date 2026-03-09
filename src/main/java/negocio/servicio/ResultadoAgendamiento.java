/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;

import dominio.modelo.Cita;
import java.util.List;

/**
 *
 * @author ramir
 */
public class ResultadoAgendamiento {
    
    private final boolean exitoso;
    private final String mensaje;
    private final List<String> errores;
    private final Cita cita;

    private ResultadoAgendamiento(boolean exitoso, String mensaje, List<String> errores, Cita cita) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.errores = errores;
        this.cita = cita;
    }
    
    public static ResultadoAgendamiento exitoso( String mensaje, Cita cita){
        return new ResultadoAgendamiento(true,mensaje,List.of(),cita);
    }
    
    public static ResultadoAgendamiento conErrores(List<String> errores){
        return new ResultadoAgendamiento(false, null,errores, null);
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<String> getErrores() {
        return errores;
    }

    public Cita getCita() {
        return cita;
    }
    
    
    
    
    
}
