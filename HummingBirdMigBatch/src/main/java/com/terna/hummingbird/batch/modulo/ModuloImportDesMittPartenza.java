package com.terna.hummingbird.batch.modulo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.exception.ExitCode;
import com.terna.hummingbird.batch.model.DestMitt;
import com.terna.hummingbird.batch.model.ResponseCreateDoc;
import com.terna.hummingbird.batch.util.BatchUtil;
import com.terna.hummingbird.batch.util.PayloadLoggerUtil;
import com.terna.hummingbird.batch.util.PropertyLoader;
import com.terna.hummingbird.batch.util.RestClient;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public class ModuloImportDesMittPartenza implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportDesMittPartenza.class);
	public static final String module_name = "ModuloImportDesMittPartenza";
	public String nome_lotto = "ELE_DEST_MITT";
	public String file_name = "";

	private Reporter reporter;
	private ObjectMapper objectMapper = new ObjectMapper();
	private String csvPath = PropertyLoader.get("csv.path");

	private List<DestMitt> payloads = new ArrayList<>();

	// Initialize
	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name + " lotto " + task.get(1));



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
					String systemId = String.valueOf(line[15]);
					log.info("Processing systemId: " + systemId);

					DestMitt des = new DestMitt();
					des.setSystemID(BatchUtil.parseLong(line[15]));
					des.setCode(line[2]);
					des.setDescription(line[5]);
					des.setCap(line[0]);
					des.setCitta(line[1]);
					des.setCodiceFiscale(line[3]);
					des.setCognomeSoggetto(line[4]);
					des.setEmail(line[6]);
					des.setFax(line[7]);
					des.setIndirizzo(line[8]);
					des.setMittCodFis(line[9]);
					des.setNazione(line[10]);
					des.setNomeSoggetto(line[11]);
					des.setNote(line[12]);
					des.setProvincia(line[13]);
					des.setRegistro(line[14]);
					des.setTelefono(line[16]);
					des.setTipo(line[17]);
					payloads.add(des);
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
		for (DestMitt des : payloads) {
			try {
				log.info("Processing systemId: " + des.getSystemID());
				log.info("json doc: " + objectMapper.writeValueAsString(des));
				String jsonDoc = objectMapper.writeValueAsString(des);
				String url = PropertyLoader.get("mittdes.url");
				ResponseCreateDoc response = RestClient.callCreateDocument(jsonDoc, url);
				log.info("DOC CREATED: " + objectMapper.writeValueAsString(response));
				reporter.addSuccess();
				PayloadLoggerUtil.logPayload(des, module_name, true, null);
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
				PayloadLoggerUtil.logPayload(des, module_name, false, e.getMessage());
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
