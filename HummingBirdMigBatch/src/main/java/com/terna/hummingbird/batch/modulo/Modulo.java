package com.terna.hummingbird.batch.modulo;


import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.exception.BatchException;

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
