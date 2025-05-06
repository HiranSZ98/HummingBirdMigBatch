package com.enel.hrgdms.batch.createevent.bean;

import com.opencsv.bean.CsvBindByName;

public class CSVEvent {
	 @CsvBindByName(column = "EventName", required = true)
    private String eventName;

    @CsvBindByName(column = "DocId", required = true)
    private String docId;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

   
		
}