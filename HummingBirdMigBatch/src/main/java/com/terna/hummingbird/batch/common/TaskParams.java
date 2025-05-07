package com.terna.hummingbird.batch.common;

public class TaskParams
{
	public static final int TIPO_OPERAZIONE = 0;  
    //BATCH_EXPORT
	/*
	 * BATCH_EXPORT 
	 * \tmp\ 
	 */
	public static final int BATCH_EXPORT_ROOT_WORK_DIR = 1;    
	public static final int BATCH_IMPORT_LOTTO_ID_LOTTO = 1; 
	
    //BATCH_EXPORT
	/*
	 * BATCH_ACTION_SECURITY 
	 * \tmp\CFA.json
	 */  
	public static final int BATCH_ACTION_SECURITY_CFG_FILE = 1;
	
	
    //BATCH_CREATE_EVENT
	/*
	 * BATCH_CREATE_EVENT 
	 * \tmp\Event_Creation.txt
	 */  
	public static final int BATCH_CREATE_EVENT_INPUT_FILE = 1; 
	
    //BATCH_CREATE_REST_EVENT
	/*
	 * BATCH_CREATE_REST_EVENT 
	 * \tmp\Event_Rest.txt
	 */  
	public static final int BATCH_CREATE_REST_EVENT_INPUT_FILE = 1;
	
    //BATCH_UPDATE_PROP_EVENT
	/*
	 * BATCH_UPDATE_PROP_EVENT 
	 * \tmp\Prp.csv
	 */  
	public static final int BATCH_UPDATE_PROP_INPUT_FILE = 1;
	
	
    //BATCH_SYNCRO_SAP_USER_HGP
	/*
	 * BATCH_SYNCRO_SAP_USER_HGP 
	 * \tmp\SAP_FILENET_YYYYMMDD.csv
	 */  
	public static final int BATCH_SAP_USER_HGP_INPUT_FILE = 1;
	
	
    //BATCH_SYNCRO_SAP_SERVICENOW
	/*
	 * BATCH_SYNCRO_SAP_SERVICENOW 
	 * \tmp\
	 */  
	public static final int BATCH_SYNCRO_SAP_SERVICENOW_INPUT_DIR = 1;
	
	
	
	 //BATCH_QUEUE_MONITOR
	/*
	 * BATCH_QUEUE_MONITOR 
	 * JMS_REST_EVENT_QUEUE_ERROR_JNDI
	 * c:\\temp\\resteventerror.xls
	 * 
	 */  
	public static final int JMS_QUEUE_ID = 1;
	public static final int BATCH_QUEUE_MONITOR_REPORT = 2;
	
	
	 //BATCH_UPLOAD_DOC
	/*
	 * BATCH_UPLOAD_DOC 
	 * 
	 * 
	 */  
	public static final int  BATCH_UPLOAD_DOC_INPUT_CSV = 1;
	
	 //BATCH_UPDATE_RETIREE		
	public static final int BATCH_UPDATE_RETIREE_LAST_EXEC_DATE = 1;

	//BATCH_REFRESH_CACHE	
	public static final int BATCH_REFRESH_CACHE_LDIF_SCRIPT_PATH = 1;
	
//	//BATCH_XML_IMPORT_CONVERTER
//	public static final int BATCH_XML_IMPORT_CONVERTER_DIR_INPUT = 1;
	
}
	