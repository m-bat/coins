GOALS = Array Assign Conv Decl Exp GotoLabel HirC If Jis KandR \
	LangSpec Loop Mix1 Opt OptPara Pointer PrePro Print Sample1 \
	Scope Simple1 Sizeof String Struct SubpCall Switch \
	TpFront Type WhiteBox \
	TestFnami TestMri TestNakata TestSassa TestSsa \
	TestBuna TestBuna/array TestBuna/func TestBuna/loop \
	TestBuna/pointer TestBuna/structure TestBuna/switch TestBuna/test0 \
	TestBuna/test1 TestBuna/test_temp

test:
	./testcoins2.sh -v `for i in $(GOALS); do echo $$i/*.c; done`

MOREGOALS = unsupported/TestFnami

moretest:
	./testcoins2.sh -v `for i in $(MOREGOALS); do echo $$i/*.c; done`

disp:
	@echo ./testcoins2.sh -v `for i in $(GOALS); do echo $$i/*.c; done`

prep:
	for i in $(GOALS); do ./testprepare.sh $$i/*.c; done

