package presentacion;

import dominio.modelo.Especialista;
import dominio.repositorio.AgendadorRepositorio;
import dominio.repositorio.SQLiteAgendadorRepositorio;
import dominio.repositorio.SQLiteEspecialistaRepositorio;
import dominio.repositorio.especialistaRepositorio;

import infrastuctura.conexion.SqlConexionBaseDatos;
import java.sql.SQLException;


import javax.swing.SwingUtilities;
import negocio.servicio.AgendadorService;

import presentacion.vista.StartFrame;

public class Main {

    public static void main(String[] args) throws SQLException {
        SqlConexionBaseDatos.inicializar();
        SqlConexionBaseDatos.SQLiteConexionBD();


        especialistaRepositorio especialistaRepository = new SQLiteEspecialistaRepositorio();

        AgendadorRepositorio agendadorRepo = new SQLiteAgendadorRepositorio();
        AgendadorService agendadorService = new AgendadorService(agendadorRepo);

        sembrarEspecialistas(especialistaRepository);

        SwingUtilities.invokeLater(() -> {
            StartFrame start = new StartFrame(agendadorService);
            start.setVisible(true);
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
