/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.modelo.Especialista;
import dominio.repositorio.PacienteRepositorio;
import dominio.repositorio.SQLiteCitaRepositorio;
import dominio.repositorio.SQLiteEspecialistaRepositorio;
import dominio.repositorio.SQLitePacienteRepositorio;
import dominio.repositorio.citaRepositorio;
import dominio.repositorio.especialistaRepositorio;

import infrastuctura.conexion.SqlConexionBaseDatos;
import java.sql.SQLException;
import java.util.List;

import javax.swing.SwingUtilities;
import negocio.servicio.AgendarCitaServicio;
import negocio.servicio.CancelarCitaServicio;
import negocio.servicio.ConsultarCitaServicio;
import negocio.servicio.ExportarCitasServicio;
import negocio.servicio.HorarioSugeridoServicio;
import negocio.servicio.ReAgendarCitaServicio;
import negocio.validacion.AgendarCitaValidacion;
import presentacion.controlador.citaControlador;
import presentacion.modelo.citaModelo;
import presentacion.vista.AgendarCitaForm;
import presentacion.vista.ConsultarCitasForm;
import presentacion.vista.MainFrame;

public class Main {

    public static void main(String[] args) throws SQLException {
        SqlConexionBaseDatos.inicializar();
        SqlConexionBaseDatos.SQLiteConexionBD();

        PacienteRepositorio pacienteRepository = new SQLitePacienteRepositorio();
        especialistaRepositorio especialistaRepository = new SQLiteEspecialistaRepositorio();
        citaRepositorio citaRepository = new SQLiteCitaRepositorio();

        sembrarEspecialistas(especialistaRepository);

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

        SwingUtilities.invokeLater(() -> {
            AgendarCitaForm agendarForm = new AgendarCitaForm(controller, model, especialistas);
            ConsultarCitasForm consultarForm = new ConsultarCitasForm(controller, model);

            MainFrame frame = new MainFrame(agendarForm, consultarForm);
            frame.setVisible(true);
        });
    }

    private static void sembrarEspecialistas(especialistaRepositorio repository) {
        if (repository.listarTodos().isEmpty()) {
            repository.guardar(new Especialista("ESP1", "Ana Ruiz", "Terapia"));
            repository.guardar(new Especialista("ESP2", "Carlos Gómez", "Fisioterapia"));
            repository.guardar(new Especialista("ESP3", "Laura Medina", "Psicología"));
        }
    }
}
