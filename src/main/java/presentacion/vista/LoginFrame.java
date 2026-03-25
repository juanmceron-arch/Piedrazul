package presentacion.vista;

import dominio.modelo.Agendador;
import dominio.modelo.Especialista;
import dominio.repositorio.PacienteRepositorio;
import dominio.repositorio.SQLiteCitaRepositorio;
import dominio.repositorio.SQLiteEspecialistaRepositorio;
import dominio.repositorio.SQLitePacienteRepositorio;
import dominio.repositorio.citaRepositorio;
import dominio.repositorio.especialistaRepositorio;
import infrastuctura.conexion.SqlConexionBaseDatos;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import negocio.servicio.AgendadorService;
import negocio.servicio.AgendarCitaServicio;
import negocio.servicio.CancelarCitaServicio;
import negocio.servicio.ConsultarCitaServicio;
import negocio.servicio.ExportarCitasServicio;
import negocio.servicio.HorarioSugeridoServicio;
import negocio.servicio.ReAgendarCitaServicio;
import negocio.validacion.AgendarCitaValidacion;
import presentacion.controlador.citaControlador;
import presentacion.modelo.citaModelo;

public class LoginFrame extends JFrame{
    
    private final AgendadorService service;
    private final JFrame previous;

    public LoginFrame(AgendadorService service, JFrame previous) {
        this.service = service;
        this.previous = previous;
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar sesión");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Iniciar sesión", JLabel.CENTER);
        title.setFont(UIStyles.FONT_TITLE);

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        JPanel form = new JPanel(new GridLayout(0, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(30, 60, 10, 60));

        form.add(new JLabel("Usuario:"));
        form.add(txtUser);

        form.add(new JLabel("Contraseña:"));
        form.add(txtPass);

        JButton btnBack = new JButton("Volver");
        JButton btnLogin = new JButton("Entrar");

        UIStyles.makeSecondaryButton(btnBack);
        UIStyles.makePrimaryButton(btnLogin);

        btnBack.addActionListener(e -> {
            dispose();
            previous.setVisible(true);
        });

        btnLogin.addActionListener(e -> {
            String login = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            Agendador agendador = service.login(login, pass);
            if (agendador != null) {

                try {
                    SqlConexionBaseDatos.inicializar();
                    SqlConexionBaseDatos.SQLiteConexionBD();

                    PacienteRepositorio pacienteRepository = new SQLitePacienteRepositorio();
                    especialistaRepositorio especialistaRepository = new SQLiteEspecialistaRepositorio();
                    citaRepositorio citaRepository = new SQLiteCitaRepositorio();

                    AgendarCitaServicio agendarCitaService = new AgendarCitaServicio(
                            citaRepository,
                            pacienteRepository,
                            especialistaRepository,
                            new AgendarCitaValidacion()
                    );

                    ConsultarCitaServicio consultarCitaService = new ConsultarCitaServicio(citaRepository);
                    CancelarCitaServicio cancelarCitaService = new CancelarCitaServicio(citaRepository);
                    ExportarCitasServicio exportarCitasService = new ExportarCitasServicio(citaRepository);
                    HorarioSugeridoServicio horarioSugeridoService = new HorarioSugeridoServicio(citaRepository);
                    ReAgendarCitaServicio reagendarCitaService = new ReAgendarCitaServicio(citaRepository);

                    citaModelo model = new citaModelo();

                    citaControlador controller = new citaControlador(
                            agendarCitaService,
                            consultarCitaService,
                            model,
                            cancelarCitaService,
                            exportarCitasService,
                            horarioSugeridoService,
                            reagendarCitaService
                    );

                    List<Especialista> especialistas = especialistaRepository.listarTodos();

                    AgendarCitaForm agendarForm = new AgendarCitaForm(controller, model, especialistas);
                    ConsultarCitasForm consultarForm = new ConsultarCitasForm(controller, model, especialistas);

                    MainFrame mainFrame = new MainFrame(agendarForm, consultarForm);
                    mainFrame.setVisible(true);

                    dispose();
                    previous.dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al iniciar sistema: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Credenciales incorrectas.",
                    "No se pudo iniciar sesión",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottom.add(btnBack);
        bottom.add(btnLogin);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        UIStyles.applyFont(getContentPane());
    }
}
