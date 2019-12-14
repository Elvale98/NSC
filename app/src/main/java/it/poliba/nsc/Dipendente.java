package it.poliba.nsc;

public class Dipendente {
    private String idDipendente;
    private String nome;
    private String cognome;
    private String email;
    private String password;

    public Dipendente() {}

    public Dipendente(String idDipendente, String nome, String cognome, String email, String password) {
        this.idDipendente = idDipendente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    public String getIdDipendente() {
        return idDipendente;
    }

    public void setIdDipendente(String idDipendente) {
        this.idDipendente = idDipendente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
