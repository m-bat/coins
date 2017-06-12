#! /usr/bin/bash
#! /bin/bash

# 04/11/06 coins compiler verification script by using coins.driver.Driver
# for x86-cygwin

###########################################################
# configure
###########################################################

# relatively specification (current directory is <coins package>/classes)
LOGDIR="../testdriver-$(date +%y%m%d-%H%M%S)" # log dir
LOGDIRFORMAT="../testdriver-??????-??????"    # for search log dir
PROFILEDIR="$LOGDIR/profile"
JAVA="java -classpath ."

# absolutely specification
#LOGDIR="~/coins/coins-0.10.2/testdriver-$(date +%y%m%d-%H%M%S)"
#LOGDIRFORMAT="~/coins/coins-0.10.2/testdriver-??????-??????"
#JAVA="java -classpath ~/coins/coins-0.10.2/classes"

# compiler specification
##CLASS="coins.driver.Driver -S" # CoinsCC compiler
##CLASS="coins.driver.Driver -S -b x86-cygwin -coins:preprocessor='cpp -I/usr/include',assembler=as" # CoinsCC compiler
##CLASS="coins.driver.Driver -S -b x86-cygwin -coins:assembler=as,gprof" # CoinsCC compiler
CLASS="coins.driver.Driver -S -b x86-cygwin -coins:assembler=as,gprof,preprocessor='cpp -I/usr/include'" # CoinsCC compiler
##CLASS="coins.driver.Driver -S -b x86-cygwin -coins:hirOpt=loopexp/cf/cpf/dce/pre/gt,assembler=as" # CoinsCC compiler
GCC="gcc -pg -lm"                  # GNU C compiler

# -coins:preprocessor=/usr/local/bin/cpp # pre-processor
# -coins:assembler=/usr/local/bin/as     # assembler
# -coins:include=/usr/include            # include path

LOG=log.txt       # test log
ERRORS=errors.txt # list of test program which occurs error
LOGALL=
DONOTGCC=
VERBOSE=
declare -a SOURCES

###########################################################
# parse parameters
###########################################################

showUsage()
{
  echo '
usage: testdriver.sh [option ...] [filename ...]
option:
  -a     log all test results (default: error only)
  -s -S  do not assemble
  -v     show the process
  -h     show this information
filename:
  *.c      test program
  others   filename list of test programs
  nothing  re-test errors included most recent test log
' 1>&2
}

while getopts casSvh opt
do
  case $opt
  in
  a) # make all test results
    LOGALL='true'
    ;;
  s) # do not assemble
    DONOTGCC='true'
    ;;
  S) # do not assemble
    DONOTGCC='true'
    ;;
  v) # show process on the screen
    VERBOSE='true'
    ;;
  h) # show the usage
    showUsage
    exit 0
    ;;
  \?) # illegal option
    showUsage
    exit 1
    ;;
  esac
done
shift $(($OPTIND-1))

if [ -z "$1" ]
then # search most recent error list if $1 is null
  LOGDIRLIST=($(echo $LOGDIRFORMAT))
  set ${LOGDIRLIST[${#LOGDIRLIST[*]}-1]}/$ERRORS
fi

###########################################################
# search test programs
###########################################################

for i
do
  #echo "# $i" #DEBUG
  if [ -d $i ]
  then
    echo $i is directory. 1>&2
    exit 1
  elif [ -f $i ]
  then
    if [ -z ${i##*.c} ]
    then # $i is .c file
      SOURCES[${#SOURCES[*]}]=$i
    else # $i is list of .c file
      for j in $(awk '{print $1}' $i)
      do
        #echo "# $j" #DEBUG
        SOURCES[${#SOURCES[*]}]=$j
      done
    fi
  else
    echo $i is not exist. 1>&2
    exit 1
  fi
done
if [ ${#SOURCES[*]} == 0 ] # is there any test programs?
then
  echo there is no test program. 1>&2
  exit 1
fi

###########################################################
# log header
###########################################################

logMessage() # $1=message
{
  echo -e -n $* >>$LOGDIR/$LOG
  if [ -n "$VERBOSE" ]
  then
    echo -e -n $*
  fi
}

if ! mkdir -p $LOGDIR
then
  echo cannot create the log directory.
  exit 1
fi
if ! mkdir -p $PROFILEDIR
then
  echo cannot create the profile directory.
  exit 1
fi
logMessage "
Started\t: $(date '+%y/%m/%d %H:%M:%S')\n
\n\
Java\t: $JAVA\n\
Class\t: $CLASS\n\
Gcc\t: $GCC\n\
Verbose\t: $VERBOSE\n\
DoNotAs\t: $DONOTGCC\n\
LogAll\t: $LOGALL\n\
LogDir\t: $LOGDIR\n\
LogFmt\t: $LOGDIRFORMAT\n\
CurDir\t: $(pwd)\n\
Sources\t: ${#SOURCES[*]} files\n\
\n"

###########################################################
# test
###########################################################

declare -i convfailure=0;
declare -i transfailure=0;
declare -i execfailure=0;
declare -i runfailure=0;
declare -i testsuccess=0;
declare -i total=0;

CCCLOG=$LOGDIR/$$coinscc.log # CoinsCC log
CCCOUT=$LOGDIR/$$coinscc.s   # CoinsCC output
GCCLOG=$LOGDIR/$$gcc.log     # gcc log
GCCOUT=$LOGDIR/$$gcc.out     # gcc output
STDOUT=$LOGDIR/$$stdout.txt  # test program stdout
STDERR=$LOGDIR/$$stderr.txt  # test program stderr
EXITST=$LOGDIR/$$exitst.txt  # test program exit status

#logMessage "\
#CCCLOG\t: $CCCLOG\n\
#CCCOUT\t: $CCCOUT\n\
#GCCLOG\t: $GCCLOG\n\
#GCCOUT\t: $GCCOUT\n\
#STDOUT\t: $STDOUT\n\
#STDERR\t: $STDERR\n\
#EXITST\t: $EXITST\n\
#SOURCES\t: ${SOURCES[*]}\n\
#\n" #DEBUG

###########################################################

logError() # $1=message $2=test program file name
{
  echo -e -n "$2\t#$1" >>$LOGDIR/$ERRORS
}
logResult() # $1=message $2=test program file name $3...=log file name
{
  logMessage "$1"

  # create error log file name for each test program
  N1=; N2=; N3=;
  for i in ${2//\// }; do N1=$N2; N2=$N3; N3=$i; done
  ERRORLOG=$LOGDIR/$N1-$N2-${N3%.c}.txt

  # output error log heaher
  date '+# Date   %y/%m/%d %H:%M:%S' >$ERRORLOG
  echo "# Source $2" >>$ERRORLOG
  echo -e -n "# Error $1" >>$ERRORLOG
  echo >>$ERRORLOG
  shift 2

  # output contents
  for i
  do
    echo "---- ${i##$LOGDIR/$$} ----" >>$ERRORLOG
    cat $i >>$ERRORLOG
    echo >>$ERRORLOG
  done
}
coinsError() # $1=test program file name
{
  if diff ${1%.c}.ans - &>/dev/null <<ENDOFDOC
testprepare.sh: THIS TEST WILL CAUSE COMPILE ERROR
ENDOFDOC
  then
    succeeded $1
  else
    let convfailure=convfailure+1
    logError  ' Compile error\n' $1
    logResult ' Compile error\n' $1 $CCCLOG $CCCOUT
  fi
}
gccError()
{
  let transfailure=transfailure+1
  logError  ' Assemble error\n' $1
  logResult ' Assemble error\n' $1 $GCCLOG $CCCLOG $CCCOUT
}
runtimeError()
{
  if diff ${1%.c}.ans - &>/dev/null <<ENDOFDOC
testprepare.sh: THIS TEST WILL CAUSE RUNTIME ERROR
ENDOFDOC
  then
    succeeded $1
  else
    let execfailure=execfailure+1
    logError  ' Runtime error\n' $1
    logResult ' Runtime error\n' $1 $STDOUT $STDERR $GCCLOG $CCCLOG $CCCOUT
  fi
}
illegalResult()
{
  let runfailure=runfailure+1
  logError  ' Illegal result\n' $1
  logResult ' Illegal result\n' $1 $STDOUT $STDERR $GCCLOG $CCCLOG $CCCOUT
}
succeeded()
{
  let testsuccess=testsuccess+1;
  if [ -n "$LOGALL" ]
  then
    logResult ' Ok\n' $1 $STDOUT $STDERR $GCCLOG $CCCLOG $CCCOUT
  else
    logMessage 'OK\n'
  fi
}
profile()
{
  N1=; N2=; N3=;
  for i in ${1//\// }; do N1=$N2; N2=$N3; N3=$i; done
  gprof $2 > $PROFILEDIR/$N1-$N2-${N3%.c}.txt
}
for i in ${SOURCES[*]}
do
  let total=total+1
  logMessage "$(date +%H:%M:%S) $i\t"
  logMessage "DRV>"
  ##if ! $JAVA $CLASS -o $CCCOUT $i &>$CCCLOG
  if ! bash -c "$JAVA $CLASS -o $CCCOUT $i &>$CCCLOG"  ##
  then
    coinsError $i
    continue
  fi
  if [ -n "$DONOTGCC" ]
  then
    succeeded $1
  fi
  logMessage 'GCC>'
  if ! $GCC -o $GCCOUT $CCCOUT &>$GCCLOG
  then
    gccError $i
    continue
  fi
  chmod 700 $GCCOUT
  logMessage 'RUN>'
  if [ -s ${i%.c}.cmd ]
  then
    bash ${i%.c}.cmd $GCCOUT >$STDOUT 2>$STDERR
  else
    $GCCOUT >$STDOUT 2>$STDERR
  fi
  if [ ! -s $STDOUT ] && [ -s ${i%.c}.ans ]
  then
    runtimeError $i
    continue
  fi
  logMessage 'CHK>'
  if diff $STDOUT ${i%.c}.ans &>/dev/null
  then
    if [ ! -s $STDERR ] && [ ! -s ${i%.c}.err ]
    then
      succeeded $i
      profile $i $GCCOUT
      continue
    fi
    if [ -s $STDERR ] && [ -s ${i%.c}.err ]
    then
      if diff $STDERR ${i%.c}.err &>/dev/null
      then
        succeeded $i
        profile $i $GCCOUT
        continue
      fi
    fi
  fi
  illegalResult $i
done
rm -f $CCCLOG $CCCOUT $GCCLOG $GCCOUT $STDOUT $STDERR

###########################################################
# log result
###########################################################

logMessage "\n\
Compile error\t: $convfailure\t$((100*convfailure/total))%\n\
Assemble error\t: $transfailure\t$((100*transfailure/total))%\n\
Runtime error\t: $execfailure\t$((100*execfailure/total))%\n\
Illegal result\t: $runfailure\t$((100*runfailure/total))%\n\
Succeeded\t: $testsuccess\t$((100*testsuccess/total))%\n\
Total\t\t: $total\n\
\n\
Finished\t: $(date '+%y/%m/%d %H:%M:%S')\n"
