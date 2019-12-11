package it.poliba.nsc;

public class Operazione_timbratura {

    private String idOperazione;
    private String dataOperazione;
    private String oraOperazione;
    private String idDipendente;

    public Operazione_timbratura() {}

    public Operazione_timbratura(String ID_Operazione, String data_operazione, String ora_operazione, String idDipendente) {
        this.idOperazione = ID_Operazione;
        this.dataOperazione = data_operazione;
        this.oraOperazione = ora_operazione;
        this.idDipendente = idDipendente;
    }

    public String getIdOperazione() {
        return idOperazione;
    }

    public void setIdOperazione(String idOperazione) {
        this.idOperazione = idOperazione;
    }

    public String getDataOperazione() {
        return dataOperazione;
    }

    public void setDataOperazione(String dataOperazione) {
        this.dataOperazione = dataOperazione;
    }

    public String getOraOperazione() {
        return oraOperazione;
    }

    public void setOraOperazione(String oraOperazione) {
        this.oraOperazione = oraOperazione;
    }

    public String getIdDipendente() {
        return idDipendente;
    }

    public void setIdDipendente(String idDipendente) {
        this.idDipendente = idDipendente;
    }

}

