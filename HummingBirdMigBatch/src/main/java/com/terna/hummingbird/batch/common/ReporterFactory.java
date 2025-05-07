package com.terna.hummingbird.batch.common;

public class ReporterFactory {

	public static final Reporter getReporter(String info) {
		
		return new ReporterImpl(info);
		
	}
	
}
