package com.terna.hummingbird.batch.exception;

public class BatchException extends AbstractException {

	public BatchException(String message) {
		super(message);
		
	}
	public BatchException(Throwable cause) {
		super(cause);
	}
	public BatchException(int codice, String message, Throwable cause) {
		super(codice,message,cause);
	}
	public BatchException(int codice, String message) {
		super(codice, message);
		
	}
	public BatchException(String message, Throwable cause) {
		super(message, cause);
		
	}
	public BatchException(int codice, String message, Object[] params) {
		super(codice, message, params);
		
	}
	
}
