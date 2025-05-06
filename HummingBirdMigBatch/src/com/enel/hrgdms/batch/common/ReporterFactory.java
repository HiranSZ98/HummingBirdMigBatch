package  com.enel.hrgdms.batch.common;

public class ReporterFactory {

	public static final Reporter getReporter(String info) {
		
		return new ReporterImpl(info);
		
	}
	
}
