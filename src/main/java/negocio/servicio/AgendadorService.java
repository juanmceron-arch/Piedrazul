package negocio.servicio;

import dominio.modelo.Agendador;
import dominio.repositorio.AgendadorRepositorio;
import negocio.validacion.ContraseñaValidacion;
import negocio.validacion.HasherValidacion;

public class AgendadorService {
    private final AgendadorRepositorio repository;
    
    public AgendadorService(AgendadorRepositorio repository) {
        this.repository = repository;
    }

    public boolean register(String login, String fullName, String passwordPlain) {
        if (login == null || login.isBlank()) return false;
        if (fullName == null || fullName.isBlank()) return false;

        if (!ContraseñaValidacion.isValid(passwordPlain)) return false;

        if (repository.findByLogin(login) != null) return false;

        String salt = HasherValidacion.newSaltBase64();
        String hash = HasherValidacion.hashBase64(passwordPlain.toCharArray(), salt);

        Agendador agendador = new Agendador(login.trim(), fullName.trim(), hash, salt);
        return repository.save(agendador);
    }

    public Agendador login(String login, String password) {
        Agendador agendador = repository.findByLogin(login);
        if (agendador == null) return null;

        boolean ok = HasherValidacion.verify(password, agendador.getPasswordSalt(), agendador.getPasswordHash());
        return ok ? agendador : null;
    }
}
