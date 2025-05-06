package com.enel.hrgdms.batch.modulo;

import com.enel.hrgdms.batch.common.Reporter;
import com.enel.hrgdms.batch.common.ReporterFactory;
import com.enel.hrgdms.batch.exception.BatchException;
import com.enel.hrgdms.batch.exception.ExitCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;


public class ModuloImportArrivo implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportArrivo.class);
	public static final String module_name = "ModuloImportArrivo";
	private Reporter reporter;
	private int num_rows = 0;
	private String csvPath = "lotti/DocumentiInArrivo2009RegistroTeaotna.csv";

	private List<String> payloads = new ArrayList<>();


	// Initialize
	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name);
		this.reporter = ReporterFactory.getReporter("Modulo " + module_name);
	}

	// preExecute
	@Override
	public void preExecute() throws BatchException {
		log.info("Esecuzione preExecute Modulo " + module_name);

		ObjectMapper objectMapper = new ObjectMapper();
		try (Scanner scanner = new Scanner(new File(csvPath))){
			log.info("Reading csv: " + csvPath);

			String headerLine = scanner.nextLine();
			String[] headers = headerLine.split(",", -1);

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] values = line.split(",", -1);

				Map<String, String> jsonMap = new LinkedHashMap<>();
				for (int i=0; i < headers.length && i < values.length; i++) {
					jsonMap.put(headers[i].trim(), values[i].trim());
				}

					String json = objectMapper.writeValueAsString(jsonMap);
					payloads.add(json);
					log.info("Payload: " + json);
			}

			scanner.close();
			num_rows = payloads.size();
			log.info("Totale payloads generati: " + num_rows);

		} catch (Exception e) {
			log.error("Errore durante la lettura/conversione del CSV", e);
			throw new BatchException(ExitCode.GENERIC_ERROR, e.getMessage());
		}
	}

	// execute
	@Override
	public void execute() throws BatchException {
		log.info("Esecuzione Modulo " + module_name);

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
	public Integer getTotalRows() {
		return null;
    }

	public List<String> getPayloads() {
		return payloads;
	}
}
