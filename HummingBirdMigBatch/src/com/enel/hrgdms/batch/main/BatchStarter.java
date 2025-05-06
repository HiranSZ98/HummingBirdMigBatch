package com.enel.hrgdms.batch.main;

import com.enel.hrgdms.batch.conf.Constants;
import com.enel.hrgdms.batch.exception.BatchException;
import com.enel.hrgdms.batch.exception.ExitCode;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Batch main class.
 * 
 * @author developer
 *
 */

public class BatchStarter {
	private static Logger log = Logger.getLogger(BatchStarter.class);

	public static void main(String[] args) throws Exception{
		int numArgs = args.length;
		if (numArgs > 0) {
			// -->> Version
			if (Constants.OPTION_VERSION.equalsIgnoreCase(args[0])) {
				log.info(" Versione " + Constants.PROGRAM_VERSION);
				System.out.println("\n" + Constants.PROGRAM_NAME + " Versione " + Constants.PROGRAM_VERSION + "\n");
				System.exit(ExitCode.RESULT_OK);
			}
			log.info(Constants.PROGRAM_NAME + " " + Constants.PROGRAM_VERSION);
			log.debug("Inizializzazione operazione");
			Batch batch = new Batch();
			try {
				batch.execute(BatchStarter.createParamMap(args));
				System.exit(ExitCode.RESULT_OK);
			} catch (BatchException e) {
				log.error("Esecuzione fallita: " + e.getMessage());
				log.info("Operazione completata con esito = " + e.getCodiceErrore() + " (se >= 5 errore bloccante).");
				System.exit(e.getCodiceErrore());
			} catch (Throwable e) {
				log.error("Esecuzione fallita: " + e.getMessage(), e);
				log.info("Operazione completata con esito = " + ExitCode.RET_CODE_ERROR + " (se >= 5 errore bloccante).");
				System.exit(ExitCode.RET_CODE_ERROR);
			}
		} else {
			log.info("Specificare il tipo di operazione da eseguire");
			log.info(BatchStarter.help());
		}
	}

	/**
	 * Metodo che ritorna una string di aiuto
	 * 
	 * @return {@link String}
	 */
	private static String help() {
		StringBuffer bf = new StringBuffer();
		bf.append("\n\n Usage: ");
		bf.append("\n ");
		bf.append("\n params : ");
		return bf.toString();
	}

	private static Map<Integer, String> createParamMap(String[] args) {
		Map<Integer, String> task = new HashMap<Integer, String>();
		for (int i = 0; i < args.length; i++) {
			task.put(i, args[i]);
		}
		return task;
	}

}
