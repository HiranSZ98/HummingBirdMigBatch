#!/bin/ksh
################################################################################
# eCarpe - Caching abilitazioni per utenti superuser con profilo autorizzativo #
#          comprendenete più di 2000 gruppi. Serve ad accelerare l'accesso     #
#          all'applicazione per gli utenti abilitati a molti gruppi LDAP.      #
#                                                                              #
# Sintassi di lancio:                                                          #
# ./HRGDMSRefreshCache.ksh ISTANZA LDIFFILEPATH <TIMESTAMP>                    #
#                                                                              #
# PARAMETRI DI INPUT                                                           #
# ------------------                                                           #
# ISTANZA:   Indica su quale delle 3 istanze wsl bisogna rinfrescare la cache. #
#            In generale e' sufficiente aggiornare una sola istanza perche' le #
#            altre vengono allineate in modo automatico dal cluster wls.       #
#            Il parametro puo' assumere il valore I1, I2 o I3.                 #
# LDIFFILEPATH:   Indica il path completo del file ldif di abilitazione 	   #
#            prodotto in output dal batch in caso di utenti non autorizzati.   #
# TIMESTAMP: (Opzionale) Se indicato specifica un timestamp da inserire nel    #
#            nome del file di log. Se non indicato viene presa la data di      #
#            sistema per comporre un timestamp nel formato Daammgg_Thhmmss     #
#                                                                              #
# ELENCO RETURN CODE                                                           #
# ------------------                                                           #
#   0 : Fine normale.                                                          #
# 100 : Parametro TIPOELAB errato, non e' ne I1 ne I2.                         #
# 120 : Errore nella copia del file properties specifico per l'elaborazione    #
#       richiesta.                                                             #
#                                                                              #
# Storico versioni                                                             #
# 01.00 04/02/2014 : Sala Fabio - Versione iniziale.                           #
################################################################################
VERSIONE="01.00 del 04/02/2014"
#########################
# Definizione variabili #
#########################

typeset -u ISTANZA=$1
LDIFFILEPATH=$2
TIMESTAMP=$3
NOMESCRIPT=${0##*/}
NOMESCRIPT=${NOMESCRIPT%%.*}

CDATE=`date '+%y%m%d'`
CTIME=`date '+%H%M%S'`

# Assegnazione variabili di ambiente
. ./HRGDMS-setenv.ksh

TIMESTAMP=${TIMESTAMP:=D${CDATE}_T${CTIME}}
LOGFILE=${DIR_LOG}/${NOMESCRIPT}_${ISTANZA}_${TIMESTAMP}.log

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
echo "Path del file LDIF_____ = ${LDIFFILEPATH}"
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

if [[ ${ISTANZA} != I1 && ${ISTANZA} != I2 ]] ; then
   echo "ERRORE: Allo script e' stato fornito un parametro tipo elaborazione errato!"
   echo "        Il parametro ISTANZA deve essere I1 oppure I2"
   echo "        Sintassi di lancio: ./HRGDMSRefreshCache.ksh ISTANZA LDIFFILEPATH <TIMESTAMP>"
   RC=100
else
   # Dal momento che il nome del file properties e' fisso, per renderlo dinamico, si procede a farne una
   # copia in una directory dedicata e diversa a seconda dell'istanza wls che si deve trattare e si modifica
   # di conseguenza la classpath
   DIR_CONFIG=${DIR_APPL}/eCarpe-config/${NOMESCRIPT}${ISTANZA}
   echo "Directory file properties dedicata:" ${DIR_CONFIG}

   if [[ ! -e ${DIR_CONFIG} ]] ; then
      mkdir ${DIR_CONFIG}
   fi

   cp ${DIR_APPL}/eCarpe-config/${NOMESCRIPT}${ISTANZA}.properties ${DIR_CONFIG}/HRGDMSRefreshCache.properties
   RC=$?

   if (( ${RC} == 0 )) ; then
      echo "Copia file HRGDMSRefreshCache${ISTANZA}.properties in directory dedicata... OK"
      CLASSPATH=${DIR_CONFIG}:${CLASSPATH}
      echo "Nuova classpath:" ${CLASSPATH}
      echo "================================================================================================"

      ############################
      ### Avvio programma JAVA ###
      ############################
      ${JAVA_HOME}/bin/java -Dlog4j.configuration=log4j.${NOMESCRIPT}${ISTANZA}.properties com.enel.hrgdms.batch.main.BatchStarter BATCH_REFRESH_CACHE ${2}
      RC=$?

   else
      echo "ERRORE: Copia file properties HRGDMSRefreshCache${ISTANZA}.properties fallita!"
      echo "        RC=${RC}"
   fi
fi

echo "================================================================================================"
LogElapsed

#if (( ${RC} == 0 ))
if (( ${RC} == 0 || ${RC} == 22 ))
then
   echo "Elaborazione terminata correttamente - RC =" ${RC}
   RC=0
else
   echo "ERRORE - Elaborazione terminata in modo anomalo - RC =" ${RC}
fi

exit ${RC}
