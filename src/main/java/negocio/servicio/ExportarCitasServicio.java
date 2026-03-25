package negocio.servicio;
import dominio.modelo.Cita;
import dominio.repositorio.citaRepositorio;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import negocio.DTO.ExportarCitas;

public class ExportarCitasServicio {

    private final citaRepositorio citaRepo;

    public ExportarCitasServicio(citaRepositorio citaRepo) {
        this.citaRepo = citaRepo;
    }

    public ResultadoExportacion exportar(ExportarCitas request) {
        if (request.fecha == null) {
            return ResultadoExportacion.conErrores(List.of("La fecha es obligatoria"));
        }

        if (request.especialistaId == null || request.especialistaId.isBlank()) {
            return ResultadoExportacion.conErrores(List.of("El terapista es obligatorio"));
        }

        List<Cita> citas = citaRepo.buscarPorFechaYEspecialista(request.fecha, request.especialistaId);

        if (citas.isEmpty()) {
            return ResultadoExportacion.conErrores(List.of("No hay citas para la fecha y terapista seleccionados"));
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(request.rutaArchivo))) {
            writer.println("id,fecha,hora,paciente,especialista,estado");

            for (Cita cita : citas) {
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                        cita.getId(),
                        cita.getFecha(),
                        cita.getHora(),
                        cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                        cita.getEspecialista().getNombre(),
                        cita.getEstadoCita());
            }

            return ResultadoExportacion.exitoso("Citas exportadas exitosamente");
        } catch (Exception e) {
            return ResultadoExportacion.conErrores(List.of("Error exportando CSV: " + e.getMessage()));
        }
    }
}
