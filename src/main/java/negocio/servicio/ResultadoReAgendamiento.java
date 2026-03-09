/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.servicio;

import java.util.List;

/**
 *
 * @author ramir
 */
public class ResultadoReAgendamiento {
    private final boolean exitoso;
    private final String mensaje;
    private final List<String> errores;

    public ResultadoReAgendamiento(boolean exitoso, String mensaje, List<String> errores) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.errores = errores;
    }

 

    public static ResultadoReAgendamiento exitoso(String mensaje) {
        return new ResultadoReAgendamiento(true, mensaje, List.of());
    }

    public static ResultadoReAgendamiento conErrores(List<String> errores) {
        return new ResultadoReAgendamiento(false, null, errores);
    }

    public boolean isExitoso() { return exitoso; }
    public String getMensaje() { return mensaje; }
    public List<String> getErrores() { return errores; }
}
