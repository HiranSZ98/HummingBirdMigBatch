package com.terna.hummingbird.batch.createevent.bean;

import com.opencsv.bean.CsvBindByName;

public class CSVProp {
	@CsvBindByName(column = "DocId", required = true)
    private String docId;

    @CsvBindByName(column = "Valore", required = true)
    private String valore;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

   
		
}