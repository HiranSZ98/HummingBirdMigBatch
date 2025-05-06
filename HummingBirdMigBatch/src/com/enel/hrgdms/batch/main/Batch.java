package com.enel.hrgdms.batch.main;

import com.enel.hrgdms.batch.common.Reporter;
import com.enel.hrgdms.batch.common.TaskParams;
import com.enel.hrgdms.batch.exception.BatchException;
import com.enel.hrgdms.batch.exception.ExitCode;
import com.enel.hrgdms.batch.modulo.Modulo;
import com.enel.hrgdms.batch.modulo.ModuloFactory;
import com.enel.hrgdms.batch.util.BatchUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import org.apache.log4j.Logger;

public class Batch {

	private static Logger log = Logger.getLogger(Batch.class);

	public void execute(Map<Integer,String> task) throws BatchException {

		String tipoOperazione = task.get(TaskParams.TIPO_OPERAZIONE);
		log.debug("Tipo operazione: " + tipoOperazione);
		if (!BatchUtil.checkString(tipoOperazione)) { 
			log.error("Tipo operazione non valido");
			throw new IllegalArgumentException("Parametro tipo operazione non valido");
		}
		Modulo modulo = ModuloFactory.getInstance().getModulo(tipoOperazione);
		NumberFormat numberFormat = new DecimalFormat("0000000000");
		try {
			/////////////////////////ESECUZIONE AZIONE///////////////////////////////
			modulo.inizialize(task);
			modulo.preExecute();
			modulo.execute();
			modulo.postExecute();
			Reporter reporter = modulo.getReporter();
			/////////////////////////ESECUZIONE AZIONE///////////////////////////////
			log.info("Operazione per "+ reporter.getInfo()+" completata, inizio verifica esito.");
			if(modulo.getRows() != 0 && reporter.getExitCode() == null){ // per BATCHARCHIVEDOCS formattazione numero con 0000000039
//				if (modulo.getTotalRows() != null) 
//					log.info("Numero di record da elaborare = " + numberFormat.format(modulo.getTotalRows()));
//				log.info("Numero di record elaborati = " + numberFormat.format(modulo.getRows()));
//				log.info("Numero di record elaborati con successo = " + numberFormat.format(reporter.getSuccess()));
//				if (reporter.getExitCode() != null) {
//					log.info("Operazione completata con esito = " + reporter.getExitCode() + " (se >= 5 errore bloccante).");
//					System.exit(reporter.getExitCode());
//				}
//				if(modulo.getRows() != reporter.getSuccess()) System.exit(ExitCode.RET_CODE_ERROR);
				if(modulo.getRows() != reporter.getSuccess()) reporter.setExitCode(ExitCode.RET_CODE_ERROR);
			} else {
//				log.info("Nessun documento da processare.");
//				if (modulo.getTotalRows() != null) 
//					log.info("Numero di record da elaborare = " + numberFormat.format(modulo.getTotalRows()));
//				log.info("Numero di record elaborati = " + numberFormat.format(modulo.getRows()));
//				log.info("Numero di record elaborati con successo = " + numberFormat.format(reporter.getSuccess()));
			}
			if (modulo.getTotalRows() != null) 
				log.info("Numero di record da elaborare :........... " + numberFormat.format(modulo.getTotalRows()));
			
			log.info("Numero di record elaborati :.............. " + numberFormat.format(modulo.getRows()));
			log.info("Numero di record elaborati con successo :. " + numberFormat.format(reporter.getSuccess()));
			if (reporter.getExitCode() != null) {
				log.info("Operazione completata con esito = " + reporter.getExitCode());
				System.exit(reporter.getExitCode());
			}
		}catch (BatchException e) {
			Reporter reporter = modulo.getReporter();
			log.error("Operazione " + reporter.getInfo() + " interrotta.");
			if (modulo.getTotalRows() != null) 
				log.error("Numero di record da elaborare = " + numberFormat.format(modulo.getTotalRows()));
			log.error("Numero di record elaborati prima dell'interruzione= " + numberFormat.format(modulo.getRows()));
			log.error("Numero di record elaborati con successo prima dell'interruzione= "+numberFormat.format(reporter.getSuccess()));
			throw e;
		}catch (Throwable e) {
			Reporter reporter = modulo.getReporter();
			log.error("Operazione "+reporter.getInfo()+" interrotta.",e);
			if (modulo.getTotalRows() != null) 
				log.info("Numero di record da elaborare = " + numberFormat.format(modulo.getTotalRows()));
			log.error("Numero di record elaborati prima dell'interruzione= " + numberFormat.format(modulo.getRows()));
			log.error("Numero di record elaborati con successo prima dell'interruzione= "+numberFormat.format(reporter.getSuccess()));
			throw new BatchException(ExitCode.GENERIC_ERROR, e.getMessage(),e);
		}
	}
}
