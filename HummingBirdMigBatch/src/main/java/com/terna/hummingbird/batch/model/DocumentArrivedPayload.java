package com.terna.hummingbird.batch.model;

import java.time.Instant;
import java.util.List;

public class DocumentArrivedPayload {
    private String idLotto;
    private FileToUpload fileToUpload;
    private String version;
    private int systemId;
    private int docNumber;
    private String docNameObject;
    private Instant creationDate;
    private String status;
    private String abstractText; // 'abstract' is a reserved word
    private String annullato;
    private String autAnnullamento;
    private int autAnnullamentoId;
    private Instant dataAnn;
    private String docType;
    private String author;
    private int authorId;
    private int numeroAllegati;
    private Instant dataProtocollo;
    private int numeroProtocollo;
    private String tipoProtocollo;
    private String codiceRegistro;
    private List<AclEntry> acl;
    private Instant dataProtocolloRicevuto;
    private String numeroProtocolloRicevuto;
    private List<Mittente> mittenti;

    public String getIdLotto() {
        return idLotto;
    }

    public void setIdLotto(String idLotto) {
        this.idLotto = idLotto;
    }

    public FileToUpload getFileToUpload() {
        return fileToUpload;
    }

    public void setFileToUpload(FileToUpload fileToUpload) {
        this.fileToUpload = fileToUpload;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public int getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(int docNumber) {
        this.docNumber = docNumber;
    }

    public String getDocNameObject() {
        return docNameObject;
    }

    public void setDocNameObject(String docNameObject) {
        this.docNameObject = docNameObject;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getAnnullato() {
        return annullato;
    }

    public void setAnnullato(String annullato) {
        this.annullato = annullato;
    }

    public String getAutAnnullamento() {
        return autAnnullamento;
    }

    public void setAutAnnullamento(String autAnnullamento) {
        this.autAnnullamento = autAnnullamento;
    }

    public int getAutAnnullamentoId() {
        return autAnnullamentoId;
    }

    public void setAutAnnullamentoId(int autAnnullamentoId) {
        this.autAnnullamentoId = autAnnullamentoId;
    }

    public Instant getDataAnn() {
        return dataAnn;
    }

    public void setDataAnn(Instant dataAnn) {
        this.dataAnn = dataAnn;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getNumeroAllegati() {
        return numeroAllegati;
    }

    public void setNumeroAllegati(int numeroAllegati) {
        this.numeroAllegati = numeroAllegati;
    }

    public Instant getDataProtocollo() {
        return dataProtocollo;
    }

    public void setDataProtocollo(Instant dataProtocollo) {
        this.dataProtocollo = dataProtocollo;
    }

    public int getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(int numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }

    public String getTipoProtocollo() {
        return tipoProtocollo;
    }

    public void setTipoProtocollo(String tipoProtocollo) {
        this.tipoProtocollo = tipoProtocollo;
    }

    public String getCodiceRegistro() {
        return codiceRegistro;
    }

    public void setCodiceRegistro(String codiceRegistro) {
        this.codiceRegistro = codiceRegistro;
    }

    public List<AclEntry> getAcl() {
        return acl;
    }

    public void setAcl(List<AclEntry> acl) {
        this.acl = acl;
    }

    public Instant getDataProtocolloRicevuto() {
        return dataProtocolloRicevuto;
    }

    public void setDataProtocolloRicevuto(Instant dataProtocolloRicevuto) {
        this.dataProtocolloRicevuto = dataProtocolloRicevuto;
    }

    public String getNumeroProtocolloRicevuto() {
        return numeroProtocolloRicevuto;
    }

    public void setNumeroProtocolloRicevuto(String numeroProtocolloRicevuto) {
        this.numeroProtocolloRicevuto = numeroProtocolloRicevuto;
    }

    public List<Mittente> getMittenti() {
        return mittenti;
    }

    public void setMittenti(List<Mittente> mittenti) {
        this.mittenti = mittenti;
    }
}
