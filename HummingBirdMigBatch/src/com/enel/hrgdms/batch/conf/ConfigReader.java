package com.enel.hrgdms.batch.conf;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * Gestisce il recupero delle propriet� applicative. Prima prova il recupero da
 * file di properties esterno, in caso di errore (mancanza del file o eccezione)
 * procede con la valorizzazione delle propriet� in modo standard (variabili di
 * context-param ed env-entry)
 */
public class ConfigReader {



	private static final String FILE_PROPERTIES = "HRGDMSImport.properties";


	private static Logger log = Logger.getLogger("com.enel.hrgdms.batch.conf");






	


}
