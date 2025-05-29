package com.terna.hummingbird.batch.modulo;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terna.hummingbird.batch.common.Reporter;
import com.terna.hummingbird.batch.common.ReporterFactory;
import com.terna.hummingbird.batch.conf.BatchConfig;
import com.terna.hummingbird.batch.exception.BatchException;
import com.terna.hummingbird.batch.exception.ExitCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terna.hummingbird.batch.model.*;
import com.terna.hummingbird.batch.util.BatchUtil;
import com.terna.hummingbird.batch.util.RestClient;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;


public class ModuloImportArrivo implements Modulo {

	private static Logger log = Logger.getLogger(ModuloImportArrivo.class);
	public static final String module_name = "ModuloImportArrivo";
	public String nome_lotto_prefix = "DOC_A_";
	public String nome_lotto_postfix = "";
	public String nome_lotto = "";
	public String file_name = "";
	public String dm_file_name = "";
	public String acl_file_name = "";

	private Reporter reporter;
	private ObjectMapper objectMapper;
	private int num_rows = 0;
	private String csvPath = "C:\\RjcSoft\\NTTData\\Terna\\Estrazioni\\Lotti\\";
	//private String csvPath = "C:\\Projects\\terma\\esatrazioni\\Lotti";

	private Map<String, List<AclEntry>> aclMap = new HashMap<>();
	private Map<String, List<DestMitt>> dmMap = new HashMap<>();
	private List<DocumentArrivedPayload> documentPayLoads = new ArrayList<>();

	// Initialize
	@Override
	public void inizialize(Map<Integer, String> task) throws BatchException {
		log.info("Esecuzione inizialize Modulo " + module_name + " lotto " + task.get(1));
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		reporter = ReporterFactory.getReporter("Modulo " + module_name);
		nome_lotto_postfix = task.get(1);
		nome_lotto = nome_lotto_prefix + nome_lotto_postfix;
		file_name = csvPath + "\\" + nome_lotto + "\\" + nome_lotto + ".csv";
		log.info(nome_lotto + " file name " + file_name);
		dm_file_name = csvPath + "\\" + nome_lotto + "\\" + "DM_" + nome_lotto + ".csv";
		acl_file_name = csvPath + "\\" + nome_lotto + "\\" + "ACL_" + nome_lotto + ".csv";
	}

	// preExecute
	@Override
	public void preExecute() throws Exception {
		log.info("Esecuzione preExecute Modulo " + module_name);

		log.info("Caricamento dati da " +
				"\n csv: " + file_name + " +, " +
				"\n dm: " + dm_file_name + ", " +
				"\n acl: " + acl_file_name);
		try {
			aclMap = BatchUtil.loadACL(acl_file_name);
			log.info("Caricamento dati da " + " acl: " + dm_file_name + " effettuato con successo.");
			dmMap = BatchUtil.loadDM(dm_file_name);
			log.info("Caricamento dati da " + " sm: " + dm_file_name + " effettuato con successo.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	// execute
	@Override
	public void execute() throws Exception {
		log.info("Esecuzione Modulo " + module_name);

		try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
			String record;
			boolean isFirstLine = true;

			while ((record = br.readLine()) != null) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}

				String[] line = record.split(",", -1);
				try {
					String docNumber = String.valueOf(line[5]);
					log.info("Processing docNumber: " + docNumber);

					DocumentArrivedPayload doc = new DocumentArrivedPayload();
					doc.setIdLotto(nome_lotto);

					String filePath = line[0] + line[1] + line[2];
					FileToUpload fileBean = new FileToUpload();
					fileBean.setFilePath(filePath);
					doc.setFileToUpload(fileBean);

					doc.setVersion(line[3]);
					doc.setSystemId(line[4]);
					doc.setDocNumber(line[5]);
					doc.setDocNameObject(line[6]);
					doc.setCreationDate(BatchUtil.convertDate(line[7]));
					doc.setStatus(line[9]);
					doc.setAbstractText(line[10]);
					doc.setAnnullato(line[11]);
					doc.setAutAnnullamento(line[12]);
					doc.setAutAnnullamentoId(BatchUtil.parseInt(line[13]));
					doc.setDataAnn(BatchUtil.parseSafeInstant(line[14]));
					doc.setDocType(line[15]);
					doc.setAuthor(line[16]);
					doc.setAuthorId(BatchUtil.parseLong(line[17]));
					doc.setNumeroAllegati(BatchUtil.parseInt(line[18]));
					doc.setDataProtocollo(BatchUtil.convertDate(line[19]));
					doc.setNumeroProtocollo(line[20]);
					doc.setTipoProtocollo(line[21]);
					doc.setCodiceRegistro(line[22]);
					doc.setDataProtocolloRicevuto(BatchUtil.convertDate(line[23]));
					doc.setNumeroProtocolloRicevuto(line[24]);
					doc.setAcl(aclMap.getOrDefault(String.valueOf(docNumber), new ArrayList<>()));
					doc.setMittenti(dmMap.getOrDefault(String.valueOf(docNumber), new ArrayList<>()));
					documentPayLoads.add(doc);
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
		for (DocumentArrivedPayload doc:documentPayLoads) {
			try {
				log.info("Processing docNumber: " + doc.getDocNumber());
				log.info("json doc: " + objectMapper.writeValueAsString(doc));
				doc.setFileToUpload(BatchUtil.toFileToUpload(doc.getFileToUpload().getFilePath()));
				String jsonDoc = objectMapper.writeValueAsString(doc);
				String url = "https://archiviomigrationappnew-ctfmejg6c8cxgmcd.westeurope-01.azurewebsites.net/api/v1.0/ArchivioMigration/CreateDocumentArrived";
				ResponseCreateDoc response = RestClient.callCreateDocument(jsonDoc, url);
				log.info("DOC CREATED: " +  objectMapper.writeValueAsString(response));
				reporter.addSuccess();
			} catch (Exception e) {
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
