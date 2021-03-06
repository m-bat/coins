#! /bin/bash
#  /usr/local/bin/bash

OUT=./$$.out

for SRC
do
  CMD=${SRC%.?}.cmd
  ANS=${SRC%.?}.ans
  ERR=${SRC%.?}.err

  # compile
  if ! gcc -lm -o $OUT $SRC
  then
    echo 'COMPILE ERROR: '$SRC
    echo 'testprepare.sh: THIS TEST WILL CAUSE COMPILE ERROR' >$ANS
    continue
  fi
  chmod u+x $OUT

  # run
  if [ -s $CMD ]
  then
    bash $CMD $OUT 1>$ANS 2>$ERR
  else
    $OUT 1>$ANS 2>$ERR
  fi
  if [ ! -s $ANS ]
  then
    echo 'RUNTIME ERROR: '$SRC
    echo 'testprepare.sh: THIS TEST WILL CAUSE RUNTIME ERROR' >$ANS
    rm -f $ERR
    continue
  fi

  # ok
  echo 'OK           : '$SRC
  if [ ! -s $ERR ]
  then
    rm -f $ERR
  fi
done

rm -f $OUT
