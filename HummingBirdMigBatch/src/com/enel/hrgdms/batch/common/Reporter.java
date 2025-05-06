package  com.enel.hrgdms.batch.common;

public interface Reporter {

	public String getInfo();
	public void startTime();
	public void endTime();
	public void addSuccess();
	public int getSuccess();
	public void setSuccess(int success);
	public long getTime();
	public Integer getExitCode();
	public void setExitCode(Integer exitCode);
}
