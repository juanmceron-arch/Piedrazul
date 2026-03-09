package presentacion.modelo;

import dominio.modelo.Cita;
import infrastuctura.observer.Observador;
import infrastuctura.observer.Sujeto;
import java.util.ArrayList;
import java.util.List;

public class citaModelo implements Sujeto {

    private final List<Observador> observadores = new ArrayList<>();
    private List<Cita> citas = new ArrayList<>();
    private Cita citaSeleccionada;
    private String mensaje;
    private List<String> errores = new ArrayList<>();
    private List<String> horariosSugeridos = new ArrayList<>();

    public List<Cita> getCitas() { return citas; }
    public Cita getCitaSeleccionada() { return citaSeleccionada; }
    public String getMensaje() { return mensaje; }
    public List<String> getErrores() { return errores; }
    public List<String> getHorariosSugeridos() { return horariosSugeridos; }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
        notificar();
    }

    public void setCitaSeleccionada(Cita citaSeleccionada) {
        this.citaSeleccionada = citaSeleccionada;
        notificar();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
        this.errores = new ArrayList<>();
        notificar();
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
        this.mensaje = null;
        notificar();
    }

    public void setHorariosSugeridos(List<String> horariosSugeridos) {
        this.horariosSugeridos = horariosSugeridos;
        notificar();
    }

    @Override
    public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    @Override
    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar() {
        for (Observador observador : observadores) {
            observador.actualizar();
        }
    }
}
