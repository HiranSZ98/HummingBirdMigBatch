#!/bin/ksh
################################################################################
# HRGDMS - Archivia offline i docuemnti ECARPE, imposta il periodo di 
#           riferimento su ServiceNow
#                                                                              
# Sintassi di lancio:                                                          
# ./HRGDMSArchiveDoc.ksh TIPOELAB -period -report [-force] [-status]              
#                                                                              #
# PARAMETRI DI INPUT                                                           #
# ------------------                                                           #
# TIPOELAB:  (Obbligatorio) Deve essere BATCH_ARCHIVE_DOC_SET_PERIOD   oppure
#										BATCH_ARCHIVE_DOC_GET_PERIOD   oppure
#                                        BATCH_ARCHIVE_DOC_MOVE_OFFLINE oppure 
#                                        BATCH_ARCHIVE_DOC_MOVE_ONLINE           
#####################################################################################
#
#  -period:  (Obbligatorio) specifica il periodo di archiviazione 
#                           esempio -period 201604  
#  -report:  (Obbligatorio) specifica il file report prodotto in fase di archiviazione
#  				                 esempio -report /appl/HRGDMS/out/archive_doc/moveoffline.csv
#  -status:  (Opzionale) forza l'esecuzione
#            BATCH_ARCHIVE_DOC_SET_PERIOD: -status 0   (0 terminato, 1 da lavorare, 2 in lavorazione) 
#  -ignoresnstatus:  (Opzionale) forza l'esecuzione
#  					BATCH_ARCHIVE_DOC_MOVE_OFFLINE Effettua il controllo sulla differenza periodo ServiceNow 
#            ma NON sullo stato 
#  -force:		(Opzionale) forza l'esecuzione
#  					BATCH_ARCHIVE_DOC_MOVE_OFFLINE: Non effettua nessun controllo su ServiceNow.	
#  					BATCH_ARCHIVE_DOC_MOVE_ONLINE: Non effettua nessun controllo su ServiceNow.
#  					                     
# TIMESTAMP: (Opzionale) Se indicato specifica un timestamp da inserire nel    #
#            nome del file di log. Se non indicato viene presa la data di      #
#            sistema per comporre un timestamp nel formato Daammgg_Thhmmss     #
#                                                                              #
# ELENCO RETURN CODE                                                           #
# ------------------                                                           #
#   0 : Fine normale.                                                          #
# 100 : Parametro TIPOELAB errato (diverso da BATCH_ARCHIVE_DOC_SET_PERIOD o
#																	 diverso da BATCH_ARCHIVE_DOC_MOVE_OFFLINE o
#																	 diverso da BATCH_ARCHIVE_DOC_MOVE_ONLINE)     #
#                                                                              #
# ELENCO RETURN CODE                                                           #
# ------------------                                                           #
#   0 : Fine normale.                                                          #
#                                                                              #
# Storico versioni                                                             #
# 01.00 19/02/2019 : Sala Fabio - Versione iniziale.                           #
################################################################################
VERSIONE="01.00 del 19/02/2019"
#########################
# Definizione variabili #
#########################

typeset TIPOELAB=$1
NOMESCRIPT=${0##*/}
NOMESCRIPT=${NOMESCRIPT%%.*}

CDATE=`date '+%y%m%d'`
CTIME=`date '+%H%M%S'`

######################################
# Assegnazione variabili di ambiente #
######################################
. ./HRGDMS-setenv.ksh

TIMESTAMP=${TIMESTAMP:=D${CDATE}_T${CTIME}}
if [[ ${TIPOELAB} == "." ]] ; then
   LOGFILE=${DIR_LOG}/${NOMESCRIPT}_${TIMESTAMP}.log
else
   LOGFILE=${DIR_LOG}/${NOMESCRIPT}_${TIPOELAB}_${TIMESTAMP}.log
fi

LOGFILE=${DIR_LOG}/${NOMESCRIPT}_${TIPOELAB}_${TIMESTAMP}.log

############################
# Include librerie esterne #
############################
. ./INCLUDE-LogLib.ksh

#########################
# Esecuzione principale #
#########################

echo "================================================================================================"
echo "Script_________________ = ${0##*/}"
echo "Versione_______________ = ${VERSIONE}"
echo "Tipo elaborazione______ = ${TIPOELAB}"
echo "Timestamp______________ = ${TIMESTAMP}"
echo "Nome del file di log___ = ${LOGFILE}"
echo "CLASSPATH______________ = ${CLASSPATH}"
echo "================================================================================================"
echo "Server:" `hostname`
echo "Username:" `ps -p $$ -o user=`
echo "Comando invocato: $0 $@"
echo "Directory corrente:" `pwd`
echo "PID:" $$
echo "================================================================================================"


################################
### Verifica parametri esterni #
################################

#if [[ ${TIPOELAB} != 'BATCH_SYNCRO_SEC_SERVICENOW' ]] ; then
#   echo "ERRORE: Allo script e' stato fornito un parametro tipo elaborazione errato! (${TIPOELAB})"
#   echo "        Il parametro tipo elaborazione deve essere BATCH_SYNCRO_SEC_SERVICENOW."
#   echo "        Sintassi di lancio: ./HRGDMSArchiveDoc TIPOELAB CSV_FILE <TIMESTAMP>"
#   RC=100
#else 
	############################
	### Avvio programma JAVA ###
	############################

	${JAVA_HOME}/bin/java -Xms256m -Xmx1024m -DHRGDMSArchiveDoc.properties=/appl/HRGDMS/eCarpe-config/HRGDMSArchiveDoc.properties -Dlog4j.configuration=log4j.${NOMESCRIPT}.properties com.enel.hrgdms.batch.main.BatchStarter ${TIPOELAB} ${2} ${3} ${4} ${5} ${6} ${7} ${8} ${9}
	RC=$?
#fi

echo "================================================================================================"
LogElapsed

if (( ${RC} == 0 ))
then
   echo "Elaborazione terminata correttamente - RC =" ${RC}
else
   echo "ERRORE - Elaborazione terminata in modo anomalo - RC =" ${RC}
fi

exit ${RC}
