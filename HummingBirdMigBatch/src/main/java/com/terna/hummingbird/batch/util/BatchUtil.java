package com.terna.hummingbird.batch.util;

import com.opencsv.CSVReader;
import com.terna.hummingbird.batch.model.AclEntry;
import com.terna.hummingbird.batch.model.DestMitt;
import com.terna.hummingbird.batch.model.FileToUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class BatchUtil {

	private static Logger log = Logger.getLogger(BatchUtil.class);
	private static final Properties properties = new Properties();

	public static boolean checkString(String s) {
		if (s != null && !s.isEmpty())
			return true;
		return false;
	}


	public static Map<String, List<AclEntry>> loadACL(String file) throws Exception {
		Map<String, List<AclEntry>> aclMap = new HashMap<>();
		try (CSVReader reader = new CSVReader(new FileReader(file))) {
			reader.readNext(); // skip header
			String[] line;
			while ((line = reader.readNext()) != null) {
				long systemId = Long.parseLong(line[0]);
				long docNumber = Long.parseLong(line[1]);
				long aclId = Long.parseLong(line[2]);
				String aclName = line[3];

				AclEntry acl = new AclEntry();
				acl.setSystemID(systemId);
				acl.setGroupID(aclId);
				acl.setDescription(aclName);
				String key = String.valueOf(docNumber).trim();
				aclMap.computeIfAbsent(key, k -> new ArrayList<>()).add(acl);
			}
		}
		return aclMap;
	}


	public static Map<String, List<DestMitt>> loadDM(String file) throws Exception {
		Map<String, List<DestMitt>> dmMap = new HashMap<>();
		try (CSVReader reader = new CSVReader(new FileReader(file))) {
			reader.readNext(); // skip header
			String[] line;
			while ((line = reader.readNext()) != null) {
				long systemId = line[0].isEmpty() ? 0 : Long.parseLong(line[0]);
				long docNumber = line[1].isEmpty() ? 0 : Long.parseLong(line[1]);
				long mittenteSystemId = line[2].isEmpty() ? 0 : Long.parseLong(line[2]);
				String code = line[3];
				String desc = line[4];

				DestMitt dm = new DestMitt();
				dm.setSystemID(mittenteSystemId);
				dm.setCode(code);
				dm.setDescription(desc);

				String key = String.valueOf(docNumber);
				dmMap.computeIfAbsent(key, k -> new ArrayList<>()).add(dm);
			}
		}
		return dmMap;
	}

	public static Instant convertDate(String dateStr) {
		Instant result = null;
		if (!StringUtils.isEmpty(dateStr)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
			LocalDate localDate = LocalDate.parse(dateStr, formatter);
			result = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
		}
		return result;
	}

	public static int parseInt(String s) {
		try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
	}

	public static long parseLong(String s) {
		try { return Long.parseLong(s); } catch (Exception e) { return 0; }
	}

	public static Instant parseSafeInstant(String value) {
		if (value != null && !value.isBlank()) {
			try {
				return Instant.parse(value.trim());
			} catch (DateTimeParseException e) {
			}
		}
		return null;
	}

	public static FileToUpload toFileToUpload(String filePath) {
		FileToUpload fileBean = null;
		File file = new File(filePath);
		if (!file.exists())
				file = new File("C:\\temp\\sample_01.pdf");
		try {
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			String base64Content = Base64.getEncoder().encodeToString(fileBytes);
			fileBean = new FileToUpload();
			fileBean.setContentBase64(base64Content);
			fileBean.setFileHash(sha256(file));
			fileBean.setFileSize(fileBytes.length);
			fileBean.setNome(file.getName());
			fileBean.setContentType(Files.probeContentType(file.toPath())); // returns "application/pdf"
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return fileBean;
	}

	public static String sha256(File file) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		try (InputStream fis = new FileInputStream(file)) {
			byte[] buffer = new byte[8192];
			int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, bytesRead);
			}
		}

		// Convert the byte array to hex
		byte[] hashBytes = digest.digest();
		StringBuilder sb = new StringBuilder();

		for (byte b : hashBytes) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}
}
