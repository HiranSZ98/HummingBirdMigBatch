package com.enel.hrgdms.batch.modulo;

import com.enel.hrgdms.batch.conf.Constants;
import com.enel.hrgdms.batch.exception.BatchException;
import com.enel.hrgdms.batch.exception.ExitCode;
import com.enel.hrgdms.batch.main.BatchStarter;

import org.apache.log4j.Logger;

public class ModuloFactory {

	private static ModuloFactory instance;
	private static Logger log = Logger.getLogger(BatchStarter.class);

	private ModuloFactory() {
	}

	public static ModuloFactory getInstance() {
		if (instance==null) {
			instance = new ModuloFactory();
		}
		return instance;
	}

	public Modulo getModulo(String tipo) throws BatchException
	{
		log.info("***** Parametri in ingresso *****");
		log.info("Tipo operazione --->" + tipo + "<---");
		switch (tipo) {
			case Constants.BATCH_IMPORT_GROUP:
				return new ModuloImportGroup();
			case Constants.BATCH_IMPORT_PEOPLE:
				return new ModuloImportPeople();
			case Constants.BATCH_IMPORT_REGISTRI:
				return new ModuloImportRegistri();
			case Constants.BATCH_IMPORT_MITTENTI:
				return new ModuloImportMittenti();
			case Constants.BATCH_IMPORT_ARRIVO:
				return new ModuloImportArrivo();
			case Constants.BATCH_IMPORT_PARTENZA:
				return new ModuloImportPartenza();
			case Constants.BATCH_IMPORT_DESMITTPARTENZA:
				return new ModuloImportDesMittPartenza();
			case Constants.BATCH_IMPORT_ACL:
				return new ModuloImportAcl();
			default:
				log.error("Tipo operazione non riconosciuto " + tipo);
				throw new BatchException(ExitCode.ERRORE_TIPO_OPERAZIONE,
						"Tipo operazione non riconosciuta: " + tipo,
						new Object[]{tipo});
		}
	}
}
