/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastuctura.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ramir
 */
public class SqlConexionBaseDatos {
    
    private static final String URL="jdbc:sqlite:piedrazul.bd";
    
    public static Connection SQLiteConexionBD()throws SQLException{
        return DriverManager.getConnection(URL);
    }
    
    public static void inicializar() {

        try (Connection conecta=SQLiteConexionBD();
            Statement stmt=conecta.createStatement();) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pacientes (
                      id TEXT PRIMARY KEY,
                        nombres TEXT NOT NULL,
                        apellidos TEXT NOT NULL,
                        telefono TEXT NOT NULL,
                        genero TEXT NOT NULL,
                        fecha_nacimiento TEXT NOT NULL
                )
            """);
            // TABLA ESPECIALISTAS
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS especialistas (
                    id TEXT PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    especialidad TEXT NOT NULL
                )
            """);
            
            // TABLA AGENDADOR
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS agendador (
                    login TEXT PRIMARY KEY,
                    fullName TEXT NOT NULL,
                    passwordHash TEXT NOT NULL,
                    passwordSalt TEXT NOT NULL
                )
            """);

            // TABLA CITAS
            stmt.executeUpdate("""
               CREATE TABLE IF NOT EXISTS citas (
                        id TEXT PRIMARY KEY,
                        paciente_id TEXT NOT NULL,
                        especialista_id TEXT NOT NULL,
                        fecha TEXT NOT NULL,
                        hora TEXT NOT NULL,
                        duracion_minutos INTEGER NOT NULL,
                        estado TEXT NOT NULL,
                        FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
                        FOREIGN KEY (especialista_id) REFERENCES especialistas(id)
                   
                )
            """);

            // INSERTAR ESPECIALISTAS DE PRUEBA
            stmt.executeUpdate("""
                INSERT OR IGNORE INTO especialistas (id, nombre, especialidad)
                VALUES
                ('ESP1', 'Ana Ruiz', 'Terapia'),
                ('ESP2', 'Carlos Gomez', 'Fisioterapia'),
                ('ESP3', 'Laura Medina', 'Psicologia')
            """);

            System.out.println("Base de datos inicializada correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
