CLASSPATH = ../../../../classes
SOURCEPATH = ../../..
JAVAC = javac
JAVACFLAGS = -deprecation -g -encoding SJIS -sourcepath $(SOURCEPATH) -classpath $(CLASSPATH) -d $(CLASSPATH)
JAVA = java
JAVAFLAGS = -classpath $(CLASSPATH)

TMD2JAVAOBJ = $(CLASSPATH)/coins/backend/tools/Tmd2Java.class


all: $(TMD2JAVAOBJ)

$(TMD2JAVAOBJ): Tmd2Java.java RegisterDescription2Java.java ../Op.java
	$(JAVAC) $(JAVACFLAGS) Tmd2Java.java

clean:
	rm -f $(TMD2JAVAOBJ) *.class *~

test: all
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java -x sparc.tmd >hoge
	mv CodeGenerator_sparc.java CodeGenerator_sparc1.java 
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java -x -DTESTMAC sparc.tmd >hoge2
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java -x -DTESTMAC=value sparc.tmd >hoge3

veryclean: clean
