package presentacion.vista;

import dominio.modelo.Cita;
import dominio.modelo.Especialista;
import infrastuctura.observer.Observador;
import javax.swing.*;
import java.awt.*;
import presentacion.controlador.citaControlador;
import presentacion.modelo.citaModelo;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import negocio.DTO.ExportarCitas;
import negocio.DTO.ReAgendarCitas;

public class ConsultarCitasForm extends JPanel implements Observador {

    private final citaControlador controller;
    private final citaModelo model;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtFiltroPaciente;
    private JTextField txtFiltroEspecialista;
    private JTextArea txtMensajes;
    private List<Especialista> especialistas;

    public ConsultarCitasForm(citaControlador controller, citaModelo model, List<Especialista> especialistas) {
        this.controller = controller;
        this.model = model;
        this.model.agregarObservador(this);
        this.especialistas = especialistas;

        setLayout(new BorderLayout());
        add(buildFiltroPanel(), BorderLayout.NORTH);
        add(buildTablaPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        controller.cargarCitas();
    }

    private JPanel buildFiltroPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtFiltroPaciente = new JTextField(12);
        txtFiltroEspecialista = new JTextField(12);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e ->
                controller.filtrarCitas(txtFiltroPaciente.getText(), txtFiltroEspecialista.getText()));

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> controller.cargarCitas());

        panel.add(new JLabel("Paciente:"));
        panel.add(txtFiltroPaciente);
        panel.add(new JLabel("Especialista:"));
        panel.add(txtFiltroEspecialista);
        panel.add(btnBuscar);
        panel.add(btnRefrescar);

        return panel;
    }

    private JScrollPane buildTablaPanel() {
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Fecha", "Hora", "Paciente", "Especialista", "Estado"}, 0
        );
        table = new JTable(tableModel);

        return new JScrollPane(table);
    }

    private JPanel buildBottomPanel() {
        JPanel contenedor = new JPanel(new BorderLayout());

        JPanel botones = new JPanel();

        JButton btnDetalle = new JButton("Ver detalle");
        btnDetalle.addActionListener(e -> verDetalle());

        JButton btnCancelar = new JButton("Cancelar cita");
        btnCancelar.addActionListener(e -> cancelarCita());

        JButton btnExportar = new JButton("Exportar CSV");
        btnExportar.addActionListener(e -> exportarCsv());

        JButton btnReagendar = new JButton("Reagendar");
        btnReagendar.addActionListener(e -> reagendar());

        botones.add(btnDetalle);
        botones.add(btnCancelar);
        botones.add(btnExportar);
        botones.add(btnReagendar);

        txtMensajes = new JTextArea(5, 40);
        txtMensajes.setEditable(false);

        contenedor.add(botones, BorderLayout.NORTH);
        contenedor.add(new JScrollPane(txtMensajes), BorderLayout.CENTER);

        return contenedor;
    }

    private String obtenerCitaSeleccionadaId() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita");
            return null;
        }
        return tableModel.getValueAt(fila, 0).toString();
    }

    private void verDetalle() {
        String citaId = obtenerCitaSeleccionadaId();
        if (citaId == null) return;

        controller.cargarDetalleCita(citaId);

        Cita cita = model.getCitaSeleccionada();
        if (cita != null) {
            String detalle = """
                    ID: %s
                    Paciente: %s %s
                    Teléfono: %s
                    Especialista: %s
                    Fecha: %s
                    Hora: %s
                    Estado: %s
                    """.formatted(
                    cita.getId(),
                    cita.getPaciente().getNombre(),
                    cita.getPaciente().getApellido(),
                    cita.getPaciente().getTelefono(),
                    cita.getEspecialista().getNombre(),
                    cita.getFecha(),
                    cita.getHora(),
                    cita.getEstadoCita()
            );

            JOptionPane.showMessageDialog(this, detalle, "Detalle de cita", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelarCita() {
        String citaId = obtenerCitaSeleccionadaId();
        if (citaId == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea cancelar la cita seleccionada?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.cancelarCita(citaId);
        }
    }

    private void exportarCsv() {
        String fechaTexto = JOptionPane.showInputDialog(this, "Ingrese fecha (yyyy-MM-dd):");
        if (fechaTexto == null || fechaTexto.isBlank()) return;

        JComboBox<Especialista> combo = new JComboBox<>();
        for (Especialista esp : especialistas) {
            combo.addItem(esp);
        }

        int opcionCombo = JOptionPane.showConfirmDialog(
                this,
                combo,
                "Seleccione especialista",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcionCombo != JOptionPane.OK_OPTION) return;

        Especialista seleccionado = (Especialista) combo.getSelectedItem();
        String especialistaId = seleccionado.getId();

        JFileChooser fileChooser = new JFileChooser();
        int opcion = fileChooser.showSaveDialog(this);
        if (opcion != JFileChooser.APPROVE_OPTION) return;

        ExportarCitas request = new ExportarCitas();
        request.fecha = LocalDate.parse(fechaTexto);
        request.especialistaId = especialistaId;
        request.rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();

        controller.exportarCitas(request);
    }

    private void reagendar() {
        String citaId = obtenerCitaSeleccionadaId();
        if (citaId == null) return;

        String fechaTexto = JOptionPane.showInputDialog(this, "Nueva fecha (yyyy-MM-dd):");
        if (fechaTexto == null || fechaTexto.isBlank()) return;

        LocalDate fecha = LocalDate.parse(fechaTexto);

        controller.cargarHorariosSugeridos(citaId, fecha);

        List<String> sugeridos = model.getHorariosSugeridos();
        if (sugeridos == null || sugeridos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay horarios sugeridos");
            return;
        }

        Object seleccion = JOptionPane.showInputDialog(
                this,
                "Seleccione horario:",
                "Reagendar",
                JOptionPane.QUESTION_MESSAGE,
                null,
                sugeridos.toArray(),
                sugeridos.get(0)
        );

        if (seleccion == null) return;

        ReAgendarCitas request = new ReAgendarCitas();
        request.citaId = citaId;
        request.nuevaFecha = fecha;
        request.nuevaHora = LocalTime.parse(seleccion.toString());

        controller.reagendarCita(request);
    }

    @Override
    public void actualizar() {
        cargarTabla(model.getCitas());

        StringBuilder sb = new StringBuilder();

        if (model.getMensaje() != null && !model.getMensaje().isBlank()) {
            sb.append(model.getMensaje()).append("\n");
        }

        if (model.getErrores() != null && !model.getErrores().isEmpty()) {
            for (String error : model.getErrores()) {
                if (error != null && !error.isBlank()) {
                    sb.append("- ").append(error).append("\n");
                }
            }
        }

        txtMensajes.setText(sb.toString());
    }

    private void cargarTabla(List<Cita> citas) {
        tableModel.setRowCount(0);

        for (Cita cita : citas) {
            tableModel.addRow(new Object[]{
                    cita.getId(),
                    cita.getFecha(),
                    cita.getHora(),
                    cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                    cita.getEspecialista().getNombre(),
                    cita.getEstadoCita()
            });
        }
    }
}
