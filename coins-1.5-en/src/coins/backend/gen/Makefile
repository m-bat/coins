CLASSPATH = ../../../../classes
SOURCEPATH = ../../..
JAVA = java
JAVAFLAGS = -classpath $(CLASSPATH)

CPP = cpp

TMD2JAVAOBJS = \
	$(CLASSPATH)/coins/backend/tools/Tmd2Java.class \
	$(CLASSPATH)/coins/backend/tools/RegisterDescription2Java.class 


all: CodeGenerator_sparc.java CodeGenerator_x86.java \
	CodeGenerator_arm.java \
	CodeGenerator_thumb.java \
	CodeGenerator_x86simd.java \
	CodeGenerator_x86sse2.java \
	CodeGenerator_mips.java \
	CodeGenerator_ppc.java \
	CodeGenerator_sh4.java \
	CodeGenerator_mb.java \
	CodeGenerator_alpha.java \
	CodeGenerator_x86_64.java \
	CodeGenerator_sample.java


CodeGenerator_x86.java: $(TMD2JAVAOBJS) x86.tmd common.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto x86.tmd

CodeGenerator_x86_64.java: $(TMD2JAVAOBJS) x86_64.tmd common.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto x86_64.tmd

CodeGenerator_x86simd.java: $(TMD2JAVAOBJS) x86.tmd common.tmd x86simd.tmdpp ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto -DSIMD_TMD -t x86simd x86.tmd

CodeGenerator_x86sse2.java: $(TMD2JAVAOBJS) x86sse2.tmd common.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto x86sse2.tmd

CodeGenerator_sparc.java: $(TMD2JAVAOBJS) sparc.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto sparc.tmd

#CodeGenerator_x86.java: $(TMD2JAVAOBJS) x86.tmd common.tmd ../tools/tmd.java.proto
#	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
#		-p ../tools/tmd.java.proto x86.tmd

CodeGenerator_arm.java: $(TMD2JAVAOBJS) arm.tmd common.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto arm.tmd

CodeGenerator_thumb.java: $(TMD2JAVAOBJS) thumb.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto thumb.tmd

CodeGenerator_mips.java: $(TMD2JAVAOBJS) mips.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto mips.tmd

CodeGenerator_ppc.java: $(TMD2JAVAOBJS) ppc.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto ppc.tmd

CodeGenerator_sh4.java: $(TMD2JAVAOBJS) sh4.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto sh4.tmd

CodeGenerator_sample.java: $(TMD2JAVAOBJS) sample.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto sample.tmd

CodeGenerator_mb.java: $(TMD2JAVAOBJS) mb.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto mb.tmd

CodeGenerator_alpha.java: $(TMD2JAVAOBJS) alpha.tmd ../tools/tmd.java.proto
	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
		-p ../tools/tmd.java.proto alpha.tmd

#CodeGenerator_x86simd.java: $(TMD2JAVAOBJS) x86simd.tmd ../tools/tmd.java.proto
#	$(JAVA) $(JAVAFLAGS) coins.backend.tools.Tmd2Java \
#		-p ../tools/tmd.java.proto x86simd.tmd

#x86.tmd: x86.tmdpp
#	$(CPP) -C -P x86.tmdpp >x86.tmd
#
#x86simd.tmd: x86.tmdpp x86simd.tmdpp
#	$(CPP) -C -P -DSIMD_TMD x86.tmdpp >x86simd.tmd

clean:
	rm -f MachineParams_*.java CodeGenerator_*.java 

veryclean: clean

# Force removing targets
.DELETE_ON_ERROR:
