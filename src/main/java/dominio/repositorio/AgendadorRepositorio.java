package dominio.repositorio;
 
import dominio.modelo.Agendador;
import java.util.List;

public interface AgendadorRepositorio {
    boolean save(Agendador agendador);
    Agendador findByLogin(String login);
    List<Agendador> list();
    
}
