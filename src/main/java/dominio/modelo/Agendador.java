package dominio.modelo;

public class Agendador {
    
    private String login;
    private String fullName;
    private String passwordHash;
    private String passwordSalt;

    public Agendador(String login, String fullName, String passwordHash, String passwordSalt) {
        this.login = login;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    public Agendador() {
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }  
   
    public String getPasswordSalt() {
        return passwordSalt; 
    }
    
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt; 
    }

    @Override
    public String toString() {
        return login + " | " + fullName;
    }
    
}
