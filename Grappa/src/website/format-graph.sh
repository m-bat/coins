#! /bin/ksh
DOT=dot
INFILE=/tmp/pgi.$$
OUTFILE=/tmp/pgo.$$
# set resource limits to protect web server
ulimit -c 0              # no core files
ulimit -t 10     # CPU seconds max
ulimit -v 500000 # 500 megs of virtual memory
ulimit -f 8192   # 8192 * 512 = 4meg files
function except {
code=$(kill -l $?)
/bin/rm -f $INFILE $OUTFILE
case $code in
EXIT|HUP|INT|QUIT|TERM)
	exit;;
*)
	print "Content-type: text/plain\n"
    	print -r - 'digraph error { n1 [shape=plaintext, label="Layout terminated. Server resource limits exceeded."]; }' | $DOT
        exit;;
esac
}
trap "except" XCPU XFSZ  SEGV ERR EXIT HUP INT QUIT TERM 
/bin/cat > $INFILE
if [ -s "$INFILE" ]
then
	$DOT < $INFILE > $OUTFILE 2>/dev/null
	if [ $? -eq 0 ]
	then
		set -- $(ls -l $OUTFILE)
		print "Content-type: text/plain\nContent-length: $5\n"
    		/bin/cat $OUTFILE
	else
		print "Content-type: text/plain\n"
    		print -r - 'digraph error { n1 [shape=plaintext, label="Your graph had an error in it!"]; }' | $DOT
	fi
else
	print "Content-type: text/plain\n"
	print -r - 'digraph newgraph { start [shape=ellipse, label="No Graph\nSupplied"]; }' | $DOT
fi
exit 0
