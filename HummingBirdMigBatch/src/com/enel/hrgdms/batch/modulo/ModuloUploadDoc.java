package com.enel.hrgdms.batch.modulo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import com.enel.hrgdms.batch.common.Reporter;
import com.enel.hrgdms.batch.common.ReporterFactory;
import com.enel.hrgdms.batch.common.TaskParams;
import com.enel.hrgdms.batch.exception.BatchException;
import com.enel.hrgdms.batch.exception.ExitCode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ModuloUploadDoc implements Modulo {

	private static final Logger log = Logger.getLogger(ModuloUploadDoc.class);
	public static final String module_name = "ModuloUploadDoc";
	private Reporter reporter;
	private int num_rows = 0;
	private String csvPath = null;
	private ObjectMapper mapper = new ObjectMapper();

	

	// Initialize
	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name);
		this.reporter = ReporterFactory.getReporter("Modulo " + module_name);
		try {
			csvPath = task.get(TaskParams.BATCH_UPLOAD_DOC_INPUT_CSV);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BatchException(ExitCode.GENERIC_ERROR, e.getMessage());
		}
	}

	// preExecute
	@Override
	public void preExecute() throws BatchException {
		log.info("Esecuzione preExecute Modulo " + module_name);
		try {         
			Scanner scanner = new Scanner(new File(csvPath));
			log.info("Reading csv:  " + csvPath);
			scanner.nextLine(); //salto la prima riga
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				log.info(line);
			}
		    scanner.close();
		    num_rows = 200;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BatchException(ExitCode.GENERIC_ERROR, e.getMessage());
		}
	}

	// execute
	@Override
	public void execute() throws BatchException {
		log.info("Esecuzione Modulo " + module_name);
		//for (JSONRequest jsonReq : requests) {
			try {
				//int responseCode = RestApiManager.putDocContentMultipart(jsonReq);
				int responseCode = 200;
				if (responseCode == 200) reporter.addSuccess();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		//}
	}

	// postExecute
	@Override
	public void postExecute() throws BatchException {
		log.info("Esecuzione postExecute Modulo " + module_name);
		try {
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}

	// Public method
	@Override
	public Reporter getReporter() {
		return this.reporter;
	}

	@Override
	public int getRows() {
		return num_rows;
	}
	
	@Override
	public Integer getTotalRows() {;
		return null;
    }
	


	
}
