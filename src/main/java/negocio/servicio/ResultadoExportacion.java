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
public class ResultadoExportacion {
    private final boolean exitoso;
    private final String mensaje;
    private final List<String> errores;

    private ResultadoExportacion(boolean exitoso, String mensaje, List<String> errores) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public static ResultadoExportacion exitoso(String mensaje) {
        return new ResultadoExportacion(true, mensaje, List.of());
    }

    public static ResultadoExportacion conErrores(List<String> errores) {
        return new ResultadoExportacion(false, null, errores);
    }

    public boolean isExitoso() { return exitoso; }
    public String getMensaje() { return mensaje; }
    public List<String> getErrores() { return errores; }
}