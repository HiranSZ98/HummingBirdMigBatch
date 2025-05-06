package com.enel.hrgdms.batch.modulo;


import com.enel.hrgdms.batch.common.Reporter;
import com.enel.hrgdms.batch.exception.BatchException;

import java.util.Map;

public interface Modulo {
	public void inizialize(Map<Integer,String> task) throws BatchException;
	public void preExecute() throws BatchException;
	public void execute() throws BatchException;
	public void postExecute() throws BatchException;	
	public Reporter getReporter();
	public int getRows();
	public Integer getTotalRows();
	
}
