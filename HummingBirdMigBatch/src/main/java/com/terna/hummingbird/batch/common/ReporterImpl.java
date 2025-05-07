package com.terna.hummingbird.batch.common;

public class ReporterImpl implements Reporter {

	private String info;
	private int success;
	private long startTime;
	private long endTime;
	private Integer exitCode;
	
	public ReporterImpl(String moduloInfo) {
		this.success = 0;
		this.info = moduloInfo;
	}
	@Override
	public void addSuccess() {
		this.success++;
	}
	@Override
	public void setSuccess(int success) {
		
		this.success = success;
		
	}
	@Override
	public void endTime() {
		
		this.endTime = System.currentTimeMillis();
	}
	@Override
	public int getSuccess() {
		
		return this.success;
	}
	@Override
	public void startTime() {
		
		this.startTime = System.currentTimeMillis();
		
	}
	@Override
	public long getTime() {
		
		long time = this.endTime - this.startTime; 
		
		return time;
	}
	@Override
	public String getInfo() {
		
		return this.info;
	}
	@Override
	public Integer getExitCode() {
		return exitCode;
	}
	@Override
	public void setExitCode(Integer exitCode) {
		this.exitCode = exitCode;
	}
	
}
