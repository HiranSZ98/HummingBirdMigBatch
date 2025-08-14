package com.terna.hummingbird.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Errore {

    @JsonProperty("Codice")
    private String codice;
    @JsonProperty("Descrizione")
    private String descrizione;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
