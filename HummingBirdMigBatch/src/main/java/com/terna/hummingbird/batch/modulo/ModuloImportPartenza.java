package com.terna.hummingbird.batch.modulo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.conf.BatchConfig;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.model.*;
import com.terna.hummingbird.batch.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModuloImportPartenza implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportPartenza.class);
	public static final String module_name = "ModuloImportPartenza";
	public String nome_lotto = "";
	public String file_name = "";
	public String dm_file_name = "";
	public String acl_file_name = "";
	public String url_sent = "";
	public String path_file_ok;
	public String path_file_ko;
	private Reporter reporter;
	private ObjectMapper objectMapper;
	private int num_rows = 0;
	private String csvRootPath = BatchConfig.getCsvRootPath();

	private Map<String, String> okMap = new HashMap<String, String>();
	private Map<String, List<AclEntry>> aclMap = new HashMap<String, List<AclEntry>>();
	private Map<String, List<DestMitt>> dmMap = new HashMap<String, List<DestMitt>>();
	private List<DocumentSentPayload> documents = new ArrayList<>();

	// Initialize
	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name + " lotto " + task.get(1));
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		url_sent = BatchConfig.getDocumentSentUrl();
		reporter = ReporterFactory.getReporter("Modulo " + module_name);
		nome_lotto = task.get(1);
		file_name = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".csv";
		log.info(nome_lotto + " file name " + file_name);
		path_file_ok = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".ok";
		path_file_ko = csvRootPath + "\\" + nome_lotto + "\\" + nome_lotto + ".ko";
		dm_file_name = csvRootPath + "\\" + nome_lotto + "\\" + "DM_" + nome_lotto + ".csv";
		acl_file_name = csvRootPath + "\\" + nome_lotto + "\\" + "ACL_" + nome_lotto + ".csv";
	}

	// preExecute
	@Override
	public void preExecute() throws Exception {
		log.info("Esecuzione preExecute Modulo " + module_name);

		FileUtils.prepareLogFiles(path_file_ok, path_file_ko);

		log.info("Caricamento dati da " +
				"\n csv: " + file_name + " +, " +
				"\n dm: " + dm_file_name +  ", " +
				"\n acl: " + acl_file_name);
		try {

			Files.deleteIfExists(Paths.get(path_file_ko));
			okMap = FileUtils.loadFileToMap(path_file_ok);
			aclMap = BatchUtil.loadACL(acl_file_name);
			log.info("Caricamento dati da " +  " acl: " + dm_file_name + " effettuato con successo.");
			dmMap = BatchUtil.loadDM(dm_file_name);
			log.info("Caricamento dati da " +  " sm: " + dm_file_name + " effettuato con successo.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	// execute
	@Override
	public void execute() throws Exception {
		log.info("Esecuzione Modulo " + module_name);
		/*
		\\GSWVADOC2\docs1\EPROCS26\A695540\20090303\05934471.pdf --> 0-1-2
		DOCNUMBER --> 5
		593497
		DOCNAME (ghetto) not empty
		DATA_PROTOCOLLO
		3/4/2009
		NUMERO_PROTOCOLLO not empty
		CODICE_REGISTRO not empty
		*/

		try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
			String record;
			boolean isFirstLine = true;

			while ((record = br.readLine()) != null) {
				if (isFirstLine) {
					isFirstLine = false; // Skip header
					continue;
				}
				// Split line into fields
				String[] line = record.split(",", -1); // -1 to keep empty strings
				String docNumber = "";
				try {
					docNumber = BatchUtil.checkLineValue(line[5], BatchUtil.REGEX_NUMERIC);
					if (okMap.containsKey(docNumber)) {
						continue; //skip already processed.
					}
					String systemId = line[4];
					log.info("Processing docNumber: " + docNumber);

					DocumentSentPayload doc = new DocumentSentPayload();
					doc.setIdLotto(nome_lotto);

					String filePath = line[0] + line[1] + line[2];
					doc.setFileToUpload(BatchUtil.toFileToUpload(BatchUtil.checkLineValue(filePath, BatchUtil.REGEX_FILE_PATH)));

					doc.setVersion(line[3]);
					doc.setSystemId(systemId);
					doc.setDocNumber(docNumber);
					doc.setDocNameObject(BatchUtil.checkLineValue(line[6], BatchUtil.REGEX_NOT_EMPTY_STRING));
					doc.setCreationDate(BatchUtil.convertDate(line[7]));
					doc.setStatus(line[9]);

					doc.setAbstractText(line[10]);
					doc.setAnnullato(line[11]);
					doc.setAutAnnullamento(line[12]);
					//doc.setAutAnnullamentoId(parseLong(line[13]));
					//doc.setDataAnn(convertDate(line[14]));
					doc.setDocType(line[15]);
					doc.setAuthor(line[16]);
					doc.setAuthorId(BatchUtil.parseLong(line[17]));
					doc.setNumeroAllegati(BatchUtil.parseInt(line[18]));
					doc.setDataProtocollo(BatchUtil.convertDate(BatchUtil.checkLineValue(line[19], BatchUtil.REGEX_NOT_EMPTY_STRING)));
					doc.setNumeroProtocollo(BatchUtil.checkLineValue(line[20], BatchUtil.REGEX_NOT_EMPTY_STRING));
					doc.setTipoProtocollo(line[21]);
					doc.setCodiceRegistro(BatchUtil.checkLineValue(line[22], BatchUtil.REGEX_NOT_EMPTY_STRING));

					Instant dataSpedizione = StringUtils.isEmpty(line[23])? null : BatchUtil.convertDate(line[23]);
					doc.setDataSpedizione(dataSpedizione);
					String tipoSpedizione = StringUtils.isEmpty(line[24])? "N/D":line[24];
					doc.setTipoSpedizione(tipoSpedizione);

					doc.setAcl(aclMap.getOrDefault(String.valueOf(docNumber), new ArrayList<>()));
					doc.setDestinatari(dmMap.getOrDefault(String.valueOf(docNumber), new ArrayList<>()));

					documents.add(doc);

				} catch (Exception e) {
					FileUtils.appendKo(path_file_ko, docNumber + ";" + record);
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
		for (DocumentSentPayload doc:documents) {

			try {
				log.info("Processing docNumber: " + doc.getDocNumber());
				//log.info("json doc: " +  objectMapper.writeValueAsString(doc));
				String jsonDoc = objectMapper.writeValueAsString(doc);
				//ResponseCreateDoc response = RestClient.callCreateDocument(jsonDoc, url_sent);
				//log.info("DOC CREATED: " +  objectMapper.writeValueAsString(response));
				reporter.addSuccess();
				FileUtils.appendOk(path_file_ok, doc.getDocNumber());
			} catch (Exception e) {
				FileUtils.appendKo(path_file_ko, doc.getDocNumber());
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
		return num_rows;
	}

	@Override
	public Integer getTotalRows() {
		return null;
    }
}
