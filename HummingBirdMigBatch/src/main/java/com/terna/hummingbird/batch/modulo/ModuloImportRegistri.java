package com.terna.hummingbird.batch.modulo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.model.RegisterPayload;
import com.terna.hummingbird.batch.model.ResponseCreateDoc;
import com.terna.hummingbird.batch.util.BatchUtil;
import com.terna.hummingbird.batch.util.RestClient;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class ModuloImportRegistri implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportArrivo.class);
	public static final String module_name = "ModuloImportArrivo";
	public String nome_lotto = "ELE_REG";
	public String file_name = "";

	private Reporter reporter;
	private ObjectMapper objectMapper;
	private String csvPath = "C:\\RjcSoft\\NTTData\\Terna\\Estrazioni\\Lotti";
	//private String csvPath = "C:\\Projects\\terma\\esatrazioni\\Lotti";

	private List<RegisterPayload> registerPayloads = new ArrayList<>();

	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name + " lotto " + task.get(1));
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		reporter = ReporterFactory.getReporter("Modulo " + module_name);
		file_name = csvPath + "\\" + nome_lotto + "\\" + nome_lotto + ".csv";
		log.info(nome_lotto + " file name " + file_name);
	}

	@Override
	public void preExecute() throws Exception {
		log.info("Esecuzione preExecute Modulo " + module_name);

		log.info("Caricamento dati da " + "  csv: " + file_name);
	}

	@Override
	public void execute() throws Exception {
		log.info("Esecuzione Modulo " + module_name);

		try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
			String record;
			boolean isFirstLine = true;

			while ((record = br.readLine()) != null) {
				if(isFirstLine){
					isFirstLine = false;
					continue;
				}

				String[] line = record.split(",", -1);
				try {
					String systemId = String.valueOf(line[8]);
					log.info("Processing systemId: " + systemId);

					RegisterPayload reg = new RegisterPayload();
					reg.setCode(line[1]);
					reg.setDescription(line[2]);
					reg.setEmail(line[3]);
					reg.setSystemId(BatchUtil.parseInt(systemId));
					registerPayloads.add(reg);
				} catch (Exception e) {
					log.error("Elaborazione lettura non avvenuta");
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error("Lettura File fallita");
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void postExecute() throws BatchException {
		log.info("Esecuzione postExecute Modulo " + module_name);
		for(RegisterPayload reg : registerPayloads){
			try{
				log.info("Processing systemId: " + reg.getSystemId());
				log.info("json doc: " + objectMapper.writeValueAsString(reg));
				String jsonDoc = objectMapper.writeValueAsString(reg);
				String url = "https://archiviofe-a8cabjb7ggf8afbb.westeurope-01.azurewebsites.net/ArchivioMigration/api/v1/Register";
				ResponseCreateDoc response = RestClient.callCreateDocument(jsonDoc, url);
				log.info("DOC CREATED: " + objectMapper.writeValueAsString(response));
				reporter.addSuccess();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public Reporter getReporter() {
		return null;
	}

	@Override
	public int getRows() {
		return 0;
	}

	@Override
	public Integer getTotalRows() {
		return 0;
	}
}
