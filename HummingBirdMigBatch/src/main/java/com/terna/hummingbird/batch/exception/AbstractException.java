package com.terna.hummingbird.batch.exception;

import java.text.MessageFormat;

/**
 * Classe che identifica una eccezione astratta
 * @author developer
 *
 */
public class AbstractException extends Exception {

	
	private static final long serialVersionUID = 451034359089029026L;

	private int codiceErrore;
	
	
	public int getCodiceErrore() {
		return codiceErrore;
	}

	public void setCodiceErrore(int codiceErrore) {
		this.codiceErrore = codiceErrore;
	}

	public AbstractException(int codice, String message) {
		super(message);
		this.codiceErrore = codice;
	}
	
	public AbstractException(int codice, String message, Throwable cause) {
		super(message,cause);
		this.codiceErrore = codice;
	}
	
	public AbstractException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public AbstractException(String message) {
		super(message);
		
	}

	public AbstractException(Throwable cause) {
		super(cause);
		
	}
	
	public  AbstractException(int codice, String message, Object[] params) {
		super(formatMessage(message, params));
		this.codiceErrore = codice;
	}
	
	private static String formatMessage(String message, Object[] params) {
        MessageFormat messageFormat = new MessageFormat(message);
        return messageFormat.format(params);
    }
	

}
