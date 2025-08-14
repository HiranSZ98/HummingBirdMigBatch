package com.terna.hummingbird.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseCreateDoc {

    @JsonProperty("IdOperazione")
    private String idOperazione;

    @JsonProperty("Errore")
    private Errore errore;

    @JsonProperty("Successo")
    private boolean successo;

    public String getIdOperazione() {
        return idOperazione;
    }

    public void setIdOperazione(String idOperazione) {
        this.idOperazione = idOperazione;
    }

    public Errore getErrore() {
        return errore;
    }

    public void setErrore(Errore errore) {
        this.errore = errore;
    }

    public boolean isSuccesso() {
        return successo;
    }

    public void setSuccesso(boolean successo) {
        this.successo = successo;
    }
}
