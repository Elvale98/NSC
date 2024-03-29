package it.poliba.nsc;

public class Dipendente {
    private String idDipendente;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String MAC_Address;

    public Dipendente() {}

    public Dipendente(String idDipendente, String nome, String cognome, String email, String password, String codUnivoco) {
        this.idDipendente = idDipendente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.MAC_Address = codUnivoco;
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

    public String getCodUnivoco() {
        return MAC_Address;
    }

    public void setCodUnivoco(String codUnivoco) {
        this.MAC_Address = codUnivoco;
    }
}
