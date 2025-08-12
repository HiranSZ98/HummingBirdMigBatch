package com.terna.hummingbird.batch.modulo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.conf.BatchConfig;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.model.RegisterPayload;
import com.terna.hummingbird.batch.model.ResponseCreateDoc;
import com.terna.hummingbird.batch.util.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class ModuloImportRegistri implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportArrivo.class);
	public static final String module_name = "ModuloImportArrivo";
	public String nome_lotto = "ELE_REG";
	public String file_name = "";
	public String url_sent = "";
	public String path_file_ok;
	public String path_file_ko;
	private Reporter reporter;
	private ObjectMapper objectMapper;
	private String csvRootPath = BatchConfig.getCsvRootPath();

	private Map<String, String> okMap = new HashMap<>();
	private List<RegisterPayload> registerPayloads = new ArrayList<>();

	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name + " lotto " + task.get(1));
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		url_sent = BatchConfig.getRegisterUrl();
		reporter = ReporterFactory.getReporter("Modulo " + module_name);
		file_name = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".csv";
		log.info(nome_lotto + " file name " + file_name);
		path_file_ok = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".ok";
		path_file_ko = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".ko";
	}

	@Override
	public void preExecute() throws Exception {
		log.info("Esecuzione preExecute Modulo " + module_name);

		FileUtils.prepareLogFiles(path_file_ok, path_file_ko);

		log.info("Caricamento dati da " + "  csv: " + file_name);

		try{

			Files.deleteIfExists(Paths.get(path_file_ko));
			okMap = FileUtils.loadFileToMap(path_file_ok);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
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

					if (okMap.containsKey(systemId)) {
						continue;
					}
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
				ResponseCreateDoc response = RestClient.callCreateDocument(jsonDoc, url_sent);
				log.info("DOC CREATED: " + objectMapper.writeValueAsString(response));
				reporter.addSuccess();
				FileUtils.appendOk(path_file_ok, String.valueOf(reg.getSystemId()));
			} catch (Exception e) {
				FileUtils.appendKo(path_file_ko, String.valueOf(reg.getSystemId()));
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
