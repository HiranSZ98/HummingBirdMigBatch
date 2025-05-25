package com.terna.hummingbird.batch.modulo;

import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.exception.ExitCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terna.hummingbird.batch.model.DocumentArrivedPayload;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;


public class ModuloImportArrivo implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportArrivo.class);
	public static final String module_name = "ModuloImportArrivo";
	public String nome_lotto_prefix = "DOC_A_";
	public String nome_lotto_postfix = "";
	public String nome_lotto = "";
	public String file_name = "";
	private Reporter reporter;
	private int num_rows = 0;
	//private String csvPath = "C:\\RjcSoft\\NTTData\\Terma\\Estrazioni\\Lotti";
	private String csvPath = "C:\\Projects\\terma\\esatrazioni\\Lotti";

	private List<DocumentArrivedPayload> documentPayLoads = new ArrayList<>();

	// Initialize
	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name + " lotto " + task.get(1));
		reporter = ReporterFactory.getReporter("Modulo " + module_name);
		nome_lotto_postfix = task.get(1);
		nome_lotto = nome_lotto_prefix + nome_lotto_postfix;
		file_name = csvPath + "\\" + nome_lotto + "\\" + nome_lotto + ".csv";
		log.info(nome_lotto + " file name " + file_name);
	}

	// preExecute
	@Override
	public void preExecute() throws BatchException {
		log.info("Esecuzione preExecute Modulo " + module_name);

		ObjectMapper objectMapper = new ObjectMapper();
		try (Scanner scanner = new Scanner(new File(file_name))){
			log.info("Reading csv: " + file_name);

			String headerLine = scanner.nextLine();
			String[] headers = headerLine.split(",", -1);

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] values = line.split(",", -1);

				Map<String, String> jsonMap = new LinkedHashMap<>();
				for (int i=0; i < headers.length && i < values.length; i++) {
					jsonMap.put(headers[i].trim(), values[i].trim());
				}

			}

			num_rows = documentPayLoads.size();
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
}
