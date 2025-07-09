package com.terna.hummingbird.batch.modulo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.model.AclEntry;
import com.terna.hummingbird.batch.model.ResponseCreateDoc;
import com.terna.hummingbird.batch.util.BatchUtil;
import com.terna.hummingbird.batch.util.PayloadLoggerUtil;
import com.terna.hummingbird.batch.util.PropertyLoader;
import com.terna.hummingbird.batch.util.RestClient;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class ModuloImportAcl implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportAcl.class);
	public static final String module_name = "ModuloImportAcl";
	public String nome_lotto = "ELE_ACL";
	public String file_name = "";

	private Reporter reporter;
	private ObjectMapper objectMapper;
	private String csvPath = PropertyLoader.get("csv.path");

	private List<AclEntry> aclPayloads = new ArrayList<>();

	// Initialize
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

	// preExecute
	@Override
	public void preExecute() throws BatchException {
		log.info("Esecuzione preExecute Modulo " + module_name);

		log.info("Caricamento dati da " + " csv: " + file_name);
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
				try {
					String systemId = String.valueOf(line[0]);
					log.info("Processing systemId: " + systemId);

					AclEntry acl = new AclEntry();
					acl.setSystemID(BatchUtil.parseLong(line[0]));
					acl.setGroupID(BatchUtil.parseLong(line[2]));
					acl.setDescription(line[3]);
					aclPayloads.add(acl);
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

	// postExecute
	@Override
	public void postExecute() throws BatchException {
		log.info("Esecuzione postExecute Modulo " + module_name);
		for(AclEntry acl : aclPayloads) {
			try {
				log.info("Processing systemId: " + acl.getSystemID());
				log.info("json doc: " + objectMapper.writeValueAsString(acl));
				String jsonDoc = objectMapper.writeValueAsString(acl);
				String url = PropertyLoader.get("acl.url");
				ResponseCreateDoc response = RestClient.callCreateDocument(jsonDoc, url);
				log.info("DOC CREATED: " + objectMapper.writeValueAsString(response));
				reporter.addSuccess();
				PayloadLoggerUtil.logPayload(acl, module_name, true, null);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				PayloadLoggerUtil.logPayload(acl, module_name, false, e.getMessage());
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
