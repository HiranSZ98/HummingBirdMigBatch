package com.enel.hrgdms.batch.report.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GdprCsv {
	
	private Integer dataElaborazione;  //YYYYMM
	private String personNumber;
	private String personnelNumber;
	private String userId;
	private List<String> fiscalId = new ArrayList<String>();
	private String country;
	private Date dataCessazione;
	private String numeroDipendente;
	private String matricolaDipendente;
	private String processType;  //03 OnBorading else Employee
	public Integer getDataElaborazione() {
		return dataElaborazione;
	}
	public void setDataElaborazione(Integer dataElaborazione) {
		this.dataElaborazione = dataElaborazione;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getPersonnelNumber() {
		return personnelNumber;
	}
	public void setPersonnelNumber(String personnelNumber) {
		this.personnelNumber = personnelNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getFiscalId() {
		return fiscalId;
	}
	public void setFiscalId(List<String> fiscalId) {
		this.fiscalId = fiscalId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(Date dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getNumeroDipendente() {
		return numeroDipendente;
	}
	public void setNumeroDipendente(String numeroDipendente) {
		this.numeroDipendente = numeroDipendente;
	}
	public String getMatricolaDipendente() {
		return matricolaDipendente;
	}
	public void setMatricolaDipendente(String matricolaDipendente) {
		this.matricolaDipendente = matricolaDipendente;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((personNumber == null) ? 0 : personNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GdprCsv other = (GdprCsv) obj;
		if (personNumber == null) {
			if (other.personNumber != null)
				return false;
		} else if (!personNumber.equals(other.personNumber))
			return false;
		return true;
	}
	

}


