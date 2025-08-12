package com.terna.hummingbird.batch.modulo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.conf.BatchConfig;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.model.PersonPayload;
import com.terna.hummingbird.batch.model.ResponseCreateDoc;
import com.terna.hummingbird.batch.util.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ModuloImportPeople implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportPeople.class);
	public static final String module_name = "ModuloImportPeople";
	public String nome_lotto = "ELE_PER";
	public String file_name = "";
	public String url_sent = "";
	public String path_file_ok;
	public String path_file_ko;
	private Reporter reporter;
	private ObjectMapper objectMapper;
	private String csvRootPath = BatchConfig.getCsvRootPath();

	private Map<String, String> okMap = new HashMap<>();
	private List<PersonPayload> personPayloads = new ArrayList<>();

	// Initialize
	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name + " lotto " + task.get(1));
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		url_sent = BatchConfig.getPersonUrl();
		reporter = ReporterFactory.getReporter("Modulo " + module_name);
		file_name = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".csv";
		log.info(nome_lotto + " file name " + file_name);
		path_file_ok = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".ok";
		path_file_ko = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".ko";
	}

	// preExecute
	@Override
	public void preExecute() throws Exception {
		log.info("Esecuzione preExecute Modulo " + module_name);

		FileUtils.prepareLogFiles(path_file_ok, path_file_ko);

		log.info("Caricamento dati da " + "  csv: " + file_name);

		try {

			Files.deleteIfExists(Paths.get(path_file_ko));
			okMap = FileUtils.loadFileToMap(path_file_ok);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	// execute
	@Override
	public void execute() throws BatchException {
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
				String systemId = "";
				try {
					systemId = String.valueOf(line[0]);
					log.info("Processing systemId: " + systemId);

					if (okMap.containsKey(systemId)) {
						continue;
					}
					PersonPayload per = new PersonPayload();
					per.setUserID(line[1]);
					per.setFullName(line[2]);
					per.setSystemId(BatchUtil.parseInt(systemId));
					per.setEmail(line[5]);
					personPayloads.add(per);
				} catch (Exception e) {
					FileUtils.appendKo(path_file_ko, systemId + ";" + record);
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	// postExecute
	@Override
	public void postExecute() throws BatchException {
		log.info("Esecuzione postExecute Modulo " + module_name);
		for(PersonPayload per : personPayloads){
			try{
				log.info("Processing systemId: " + per.getSystemId());
				log.info("json doc: " + objectMapper.writeValueAsString(per));
				String jsonDoc = objectMapper.writeValueAsString(per);
				ResponseCreateDoc response = RestClient.callCreateDocument(jsonDoc, url_sent);
				log.info("DOC CREATED: " + objectMapper.writeValueAsString(response));
				reporter.addSuccess();
				FileUtils.appendOk(path_file_ok, String.valueOf(per.getSystemId()));
			} catch (Exception e) {
				FileUtils.appendKo(path_file_ko, String.valueOf(per.getSystemId()));
				log.error(e.getMessage(), e);
			}
		}
	}

	// Public method
	@Override
	public Reporter getReporter() {
		return this.reporter;
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
