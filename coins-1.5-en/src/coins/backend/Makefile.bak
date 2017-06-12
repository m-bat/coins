JAVACFLAGS = -g 



#Generated Java Sources
GENFILES=Op.java

# targets/MachineParamTable_sparc.java \
#	targets/MachineParamTable_x86.java

all: preprocess $(GENFILES)

Op.java: Op.pattern Op.names gendefs.pl
	cpp -C Op.pattern | perl ./gendefs.pl >Op.java

preprocess: Op.java
	cd tools && $(MAKE) all
	cd gen && $(MAKE) all
	if [ -d tmd ] && cd tmd ; then $(MAKE) all; $(MAKE) -f Makeparam; fi

clean:
	cd tools && $(MAKE) clean
	cd gen && $(MAKE) clean
	if [ -d tmd ] && cd tmd ; then $(MAKE) clean; fi

veryclean: clean
	cd tools && $(MAKE) veryclean
	cd gen && $(MAKE) clean
	rm -f $(GENFILES) *~
	if [ -d tmd ] && cd tmd ; then $(MAKE) cleanall; fi
