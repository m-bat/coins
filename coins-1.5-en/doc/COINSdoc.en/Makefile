HEADER = header.inc.en
FOOTER1 = footer1.inc.en
FOOTER2 = footer2.inc.en
CATTHEM = cat $(HEADER) $@.en $(FOOTER1) xxx $(FOOTER2) > $@
GENDATE = date >xxx
SORRY = cat $(HEADER) sorry.html.en $(FOOTER1) xxx $(FOOTER2) > $@

all: backend.html cc.html driver.html f77.html hiropt.html index.html\
	 lir.html para.html perf.html retarget.html simd.html\
	 ssa.html

backend.html: backend.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

cc.html: cc.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

driver.html: driver.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

f77.html: f77.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

hiropt.html: hiropt.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

index.html: index.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

infra.html: infra.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

lir.html: lir.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(SORRY)

para.html: para.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

perf.html: perf.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(SORRY)

retarget.html: retarget.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(SORRY)

simd.html: simd.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

ssa.html: ssa.html.en $(HEADER) $(FOOTER1) $(FOOTER2)
	$(GENDATE)
	$(CATTHEM)

