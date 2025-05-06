#!/bin/ksh
################################################################################
# HRGDMS - Aggiornamento ACL per documenti relativi a utenti cessati           #
#                                                                              #
# Sintassi di lancio:                                                          #
# ./HRGDMSUpdateRetiree.ksh TIPOELAB <DATAULTIMAMODIFICA> <TIMESTAMP>          #
#                                                                              #
# PARAMETRI DI INPUT                                                           #
# ------------------                                                           #
# TIPOELAB:  (Obbligatorio) Deve essere BATCH_UPDATE_RETIREE                   #
# DATAULTIMAMODIFICA:  (Opzionale) Data di taglio per il recupero degli utenti #
#						cessati. Se non indicata viene presa dalla tabella     #
#						HGP_USER_UPDATE_RETIREE_LOG. Il formato della data di  #
#						taglio è indicato nel file                             #
#						HRGDMSUpdateRetiree.properties						   #
# TIMESTAMP: (Opzionale) Se indicato specifica un timestamp da inserire nel    #
#            nome del file di log. Se non indicato viene presa la data di      #
#            sistema per comporre un timestamp nel formato Daammgg_Thhmmss     #
#                                                                              #
# ELENCO RETURN CODE                                                           #
# ------------------                                                           #
#   0 : Fine normale.                                                          #
# 100 : Parametro TIPOELAB errato (diverso da BATCH_UPDATE_RETIREE)            #
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
DATAULTIMAMODIFICA=$2
TIMESTAMP=$3
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
echo "Data di ultima modifica = ${DATAULTIMAMODIFICA}"
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

if [[ ${TIPOELAB} != 'BATCH_UPDATE_RETIREE' ]] ; then
   echo "ERRORE: Allo script e' stato fornito un parametro tipo elaborazione errato! (${TIPOELAB})"
   echo "        Il parametro tipo elaborazione deve essere BATCH_UPDATE_RETIREE."
   echo "        Sintassi di lancio: ./HRGDMSUpdateRetiree TIPOELAB <DATAULTIMAMODIFICA> <TIMESTAMP>"
   RC=100
else 
	############################
	### Avvio programma JAVA ###
	############################

	${JAVA_HOME}/bin/java -Xms256m -Xmx1024m -DHRGDMSUpdateRetiree.properties=/appl/HRGDMS/eCarpe-config/HRGDMSUpdateRetiree.properties -Dlog4j.configuration=log4j.${NOMESCRIPT}.properties com.enel.hrgdms.batch.main.BatchStarter ${TIPOELAB} ${DATAULTIMAMODIFICA}
	RC=$?
fi

echo "================================================================================================"
LogElapsed

if (( ${RC} == 0 ))
then
   echo "Elaborazione terminata correttamente - RC =" ${RC}
else
   echo "ERRORE - Elaborazione terminata in modo anomalo - RC =" ${RC}
fi

exit ${RC}
