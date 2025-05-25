package com.terna.hummingbird.batch.model;

import java.time.Instant;
import java.util.List;

public class DocumentSentPayload {
    private String idLotto;
    private FileToUpload fileToUpload;
    private String version;
    private long systemId;
    private long docNumber;
    private String docNameObject;
    private Instant creationDate;
    private String status;
    private String abstractText;
    private String annullato;
    private String autAnnullamento;
    private int autAnnullamentoId;
    private Instant dataAnn;
    private String docType;
    private String author;
    private long authorId;
    private int numeroAllegati;
    private Instant dataProtocollo;
    private long numeroProtocollo;
    private String tipoProtocollo;
    private String codiceRegistro;
    private List<AclEntry> acl;
    private Instant dataSpedizione;
    private String tipoSpedizione;
    private List<DestMitt> destinatari;

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

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public long getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(long docNumber) {
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

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
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

    public long getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(long numeroProtocollo) {
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

    public Instant getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(Instant dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public String getTipoSpedizione() {
        return tipoSpedizione;
    }

    public void setTipoSpedizione(String tipoSpedizione) {
        this.tipoSpedizione = tipoSpedizione;
    }

    public List<DestMitt> getDestinatari() {
        return destinatari;
    }

    public void setDestinatari(List<DestMitt> destinatari) {
        this.destinatari = destinatari;
    }
}
