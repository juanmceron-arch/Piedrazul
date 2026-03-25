package dominio.repositorio;

import dominio.modelo.Agendador;
import infrastuctura.conexion.SqlConexionBaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SQLiteAgendadorRepositorio implements AgendadorRepositorio{
    
    
    @Override
    public boolean save(Agendador agendador) {
        String sql = "INSERT INTO Agendador (login, fullName, passwordHash, passwordSalt) VALUES (?, ?, ?, ?)";
        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, agendador.getLogin());
            ps.setString(2, agendador.getFullName());
            ps.setString(3, agendador.getPasswordHash());
            ps.setString(4, agendador.getPasswordSalt());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Agendador findByLogin(String login) {
        String sql = "SELECT * FROM Agendador WHERE login = ?";
        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Agendador(
                    rs.getString("login"),
                    rs.getString("fullName"),
                    rs.getString("passwordHash"),
                    rs.getString("passwordSalt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Agendador> list() {
        List<Agendador> agendadores = new ArrayList<>();
        String sql = "SELECT * FROM Agendador";
        try (Connection conn = SqlConexionBaseDatos.SQLiteConexionBD();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                agendadores.add(new Agendador(
                    rs.getString("login"),
                    rs.getString("fullName"),
                    rs.getString("passwordHash"),
                    rs.getString("passwordSalt") 
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agendadores;
    }
}
       