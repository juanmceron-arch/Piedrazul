/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.vista;

import dominio.modelo.Especialista;
import infrastuctura.observer.Observador;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import negocio.DTO.AgendarCitaPeticion;
import presentacion.controlador.citaControlador;
import presentacion.modelo.citaModelo;

public class AgendarCitaForm extends JPanel implements Observador {

    private final citaControlador controller;
    private final citaModelo model;

    private JTextField txtPacienteId;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtTelefono;
    private JComboBox<String> cbGenero;
    private JTextField txtFechaNacimiento;
    private JComboBox<EspecialistaItem> cbEspecialista;
    private JTextField txtFechaCita;
    private JComboBox<String> cbHora;
    private JSpinner spDuracion;
    private JTextArea txtMensajes;

    public AgendarCitaForm(citaControlador controller, citaModelo model, List<Especialista> especialistas) {
        this.controller = controller;
        this.model = model;
        this.model.agregarObservador(this);

        setLayout(new BorderLayout());
        add(buildFormPanel(especialistas), BorderLayout.CENTER);
        add(buildMessagePanel(), BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel(List<Especialista> especialistas) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtPacienteId = new JTextField(20);
        txtNombres = new JTextField(20);
        txtApellidos = new JTextField(20);
        txtTelefono = new JTextField(20);
        cbGenero = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
        txtFechaNacimiento = new JTextField("2000-01-01", 20);

        cbEspecialista = new JComboBox<>();
        for (Especialista e : especialistas) {
            cbEspecialista.addItem(new EspecialistaItem(e.getId(), e.getNombre() + " - " + e.getEspecialidad()));
        }

        txtFechaCita = new JTextField(LocalDate.now().toString(), 20);
        cbHora = new JComboBox<>(new String[]{"08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00"});
        spDuracion = new JSpinner(new SpinnerNumberModel(60, 15, 180, 15));

        int row = 0;
        addRow(panel, gbc, row++, "ID Paciente:", txtPacienteId);
        addRow(panel, gbc, row++, "Nombres:", txtNombres);
        addRow(panel, gbc, row++, "Apellidos:", txtApellidos);
        addRow(panel, gbc, row++, "Teléfono:", txtTelefono);
        addRow(panel, gbc, row++, "Género:", cbGenero);
        addRow(panel, gbc, row++, "Fecha nacimiento:", txtFechaNacimiento);
        addRow(panel, gbc, row++, "Especialista:", cbEspecialista);
        addRow(panel, gbc, row++, "Fecha cita:", txtFechaCita);
        addRow(panel, gbc, row++, "Hora:", cbHora);
        addRow(panel, gbc, row++, "Duración:", spDuracion);

        JButton btnGuardar = new JButton("Agendar cita");
        btnGuardar.addActionListener(e -> agendar());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());

        JPanel buttons = new JPanel();
        buttons.add(btnGuardar);
        buttons.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(buttons, gbc);

        return panel;
    }

    private JScrollPane buildMessagePanel() {
        txtMensajes = new JTextArea(6, 40);
        txtMensajes.setEditable(false);
        return new JScrollPane(txtMensajes);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void agendar() {
         try {
        AgendarCitaPeticion request = new AgendarCitaPeticion();
        request.pacienteId = txtPacienteId.getText().trim();
        request.nombres = txtNombres.getText().trim();
        request.apellidos = txtApellidos.getText().trim();
        request.telefono = txtTelefono.getText().trim();
        request.genero = (String) cbGenero.getSelectedItem();
        request.fechaNacimiento = LocalDate.parse(txtFechaNacimiento.getText().trim());

        EspecialistaItem item = (EspecialistaItem) cbEspecialista.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay especialistas cargados",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        request.especialistaId = item.id;
        request.fecha = LocalDate.parse(txtFechaCita.getText().trim());
        request.hora = LocalTime.parse((String) cbHora.getSelectedItem());
        request.duracionMinutos = (Integer) spDuracion.getValue();

        controller.agendarCita(request);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error en el formulario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    }

    private void limpiar() {
        txtPacienteId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtFechaNacimiento.setText("2000-01-01");
        txtFechaCita.setText(LocalDate.now().toString());
        cbHora.setSelectedIndex(0);
        spDuracion.setValue(60);
        txtMensajes.setText("");
    }

    @Override
    public void actualizar() {
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

    private static class EspecialistaItem {

        private final String id;
        private final String label;

        public EspecialistaItem(String id, String label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
