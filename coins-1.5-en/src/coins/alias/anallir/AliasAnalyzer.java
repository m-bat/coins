/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

/*
 * Created on 2005/11/19
 */

import java.util.HashMap;
import java.util.Vector;

import coins.backend.Function;
import coins.backend.Module;
import coins.backend.Op;
import coins.backend.lir.LirBinOp;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.LirUnaOp;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;

/**
 * @author K_Yoshiba
 */
public class AliasAnalyzer {

    private AliasAnalysis analysis;

    private HashMap       typeEnv;

    public AliasAnalyzer() {
        analysis = new AliasAnalysis();
        typeEnv = new HashMap();
    }

    public void processStatement(LirNode node) {

        if (node.opCode == Op.SET) {
            LirNode lhs = node.kid(0);
            LirNode rhs = node.kid(1);
            ECR ecr1 = getVariable(lhs);

            // x = &y
            if (rhs instanceof LirSymRef) {
                ECR ecr2 = makeECR(rhs);
                analysis.assignAddr(ecr1, ecr2);

                // x = ...
            } else if (rhs.opCode == Op.MEM) {
                ECR ecr2 = getVariable(rhs);
                analysis.assign(ecr1, ecr2);

                // x = op(...)
            } else if ((rhs instanceof LirBinOp) || (rhs instanceof LirUnaOp)) {
                Vector operands = new Vector();
                getOperands(rhs, operands);
                int len = operands.size();

                if ((len == 1) && (operands.get(0) instanceof LirSymRef)) {
                    analysis.assignAddr(ecr1,
                            makeECR((LirNode) operands.get(0)));

                } else {
                    for (int i = 0; i < len; i++) {
                        ECR ecr2 = getVariable((LirNode) operands.get(i));
                        analysis.assign(ecr1, ecr2);
                    }
                }
            }

            // x = f(...)
        } else if (node.opCode == Op.CALL) {
            String funcName = ((LirSymRef) node.kid(0)).symbol.name;
            if (!funcName.endsWith("printf")) {
                ECR func = makeECR(node.kid(0));

                Vector args = new Vector();
                for (int i = 0; i < node.kid(1).nKids(); i++) {
                    ECR arg = null;

                    if ((node.kid(1).kid(i) instanceof LirBinOp)
                            || ((node.kid(1).kid(i) instanceof LirUnaOp) && (node
                                    .kid(1).kid(i).opCode != Op.MEM))) {
                        ECR tmpEcr = new ECR(null, new AlphaType());
                        Vector operands = new Vector();
                        getOperands(node.kid(1).kid(i), operands);
                        int len = operands.size();

                        if ((len == 1)
                                && (operands.get(0) instanceof LirSymRef)) {
                            analysis.assignAddr(tmpEcr,
                                    makeECR((LirNode) operands.get(0)));

                        } else {
                            for (int j = 0; j < len; j++) {
                                ECR ecr2 = getVariable((LirNode) operands
                                        .get(j));
                                analysis.assign(tmpEcr, ecr2);
                            }
                        }
                        arg = tmpEcr;
                    } else if ((node.kid(1).kid(i).opCode != Op.INTCONST)
                            && (node.kid(1).kid(i).opCode != Op.FLOATCONST)) {
                        arg = getVariable(node.kid(1).kid(i));
                    }
                    if (arg != null) {
                        args.add(arg);
                    }
                }

                LirNode outParm = node.kid(2);
                ECR retVal = null;
                if (outParm.nKids() != 0) {
                    retVal = getVariable(outParm.kid(0));
                }

                analysis.functionCall(func, args, retVal);
            }
        }
    }

    public void getOperands(LirNode node, Vector operands) {
        if (node instanceof LirBinOp) {
            if (node.kid(0) instanceof LirSymRef) {
                operands.clear();
                operands.add(node.kid(0));
                return;
            } else if (node.kid(0).opCode == Op.MEM) {
                operands.add(node.kid(0));
            } else {
                getOperands(node.kid(0), operands);
            }
            if (node.kid(1) instanceof LirSymRef) {
                operands.clear();
                operands.add(node.kid(1));
                return;
            } else if (node.kid(1).opCode == Op.MEM) {
                operands.add(node.kid(1));
            } else {
                getOperands(node.kid(1), operands);
            }

        } else if (node instanceof LirUnaOp) {
            if (node.kid(0) instanceof LirSymRef) {
                operands.clear();
                operands.add(node.kid(0)); // <-
                return;
            } else if (node.kid(0).opCode == Op.MEM) {
                operands.add(node.kid(0));
            } else {
                getOperands(node.kid(0), operands);
            }
        }
    }

    public ECR getVariable(LirNode node) {

        // (FRAME x)
        if (node instanceof LirSymRef) {
            ECR tmpEcr = new ECR(null, new AlphaType());
            ECR ecr = makeECR(node);
            analysis.assignAddr(tmpEcr, ecr);
            return tmpEcr;

            // (MEM (FRAME x))
        } else if ((node.opCode == Op.MEM)
                && (node.kid(0) instanceof LirSymRef)) {
            ECR ecr = makeECR(node.kid(0));
            return ecr;

            // (MEM (MEM ... (FRAME x) ...))
        } else if ((node.opCode == Op.MEM) && (node.kid(0).opCode == Op.MEM)) {
            return getSimpleVariable(node);

            // (MEM (ADD (FRAME x) ... ))
        } else if ((node.opCode == Op.MEM) && (node.kid(0).opCode == Op.ADD)
                && (node.kid(0).kid(0) instanceof LirSymRef)) {
            return getArrayVariable1(node);

            // (MEM (ADD (MEM (ADD ... (FRAME x) ... ))))
        } else if ((node.opCode == Op.MEM) && (node.kid(0).opCode == Op.ADD)
                && (node.kid(0).kid(0).opCode == Op.MEM)) {
            return getArrayVariable1(node);

            // (MEM (ADD (ADD ... (FRAME x) ... )))
        } else if ((node.opCode == Op.MEM) && (node.kid(0).opCode == Op.ADD)
                && (node.kid(0).kid(0).opCode == Op.ADD)) {
            return getArrayVariable2(node);
        } else {
            return new ECR();
        }
    }

    // node = (MEM (MEM (MEM ... (FRAME x) ... )))
    public ECR getSimpleVariable(LirNode node) {
        if ((node.opCode == Op.MEM) && (node.kid(0) instanceof LirSymRef)) {
            ECR ecr = makeECR(node.kid(0));
            return ecr;
        } else {
            ECR ecr = getSimpleVariable(node.kid(0)); // <-
            ECR tau = ((AlphaType) ecr.getECR().getType()).getTau();
            if (tau.getType().isBottom()) {
                ECR tmpEcr = new ECR(null, new AlphaType());
                analysis.assignPtr(tmpEcr, ecr);
                return tmpEcr;
            }
            return tau;
        }
    }

    // node = (MEM (ADD (MEM (ADD ... (FRAME x) ... ))))
    // | (MEM (ADD (MEM (ADD ... (MEM (FRAME x) ... )))))
    public ECR getArrayVariable1(LirNode node) {
        if ((node.opCode == Op.MEM) && (node.kid(0).opCode == Op.ADD)
                && (node.kid(0).kid(0) instanceof LirSymRef)) {
            ECR ecr = makeECR(node.kid(0).kid(0));
            return ecr;
        } else if ((node.opCode == Op.MEM) && (node.kid(0).opCode == Op.ADD)
                && (node.kid(0).kid(0).opCode == Op.MEM)
                && (node.kid(0).kid(0).kid(0) instanceof LirSymRef)) {
            ECR ecr = makeECR(node.kid(0).kid(0).kid(0));
            ECR tau = ((AlphaType) ecr.getECR().getType()).getTau();
            if (tau.getType().isBottom()) {
                ECR tmpEcr = new ECR(null, new AlphaType());
                analysis.assignPtr(tmpEcr, ecr);
                return tmpEcr;
            }
            return tau;
        } else {
            ECR ecr = getArrayVariable1(node.kid(0).kid(0)); // <-
            ECR tau = ((AlphaType) ecr.getECR().getType()).getTau();
            if (tau.getType().isBottom()) {
                ECR tmpEcr = new ECR(null, new AlphaType());
                analysis.assignPtr(tmpEcr, ecr);
                return tmpEcr;
            }
            return tau;
        }
    }

    // node = (MEM (ADD (ADD ... (FRAME x) ... )))
    // | (MEM (ADD (ADD ... (MEM (FRAME x) ... ))))
    public ECR getArrayVariable2(LirNode node) {
        LirNode i = node.kid(0);
        while (i.opCode == Op.ADD) {
            i = i.kid(0);
        }
        if (i.opCode == Op.MEM) {
            ECR ecr = makeECR(i.kid(0));
            ECR tau = ((AlphaType) ecr.getECR().getType()).getTau();
            if (tau.getType().isBottom()) {
                ECR tmpEcr = new ECR(null, new AlphaType());
                analysis.assignPtr(tmpEcr, ecr);
                return tmpEcr;
            }
            return tau;
        }
        return makeECR(i);
    }

    // -----------------------------------------------------------------

    public AliasInformation analyze(Module module) {
         //System.out.println("yeahhhhh");
        BiList symList = new BiList();
        BiList globalSyms = module.globalSymtab.symbols();
        for (BiLink l = globalSyms.first(); !l.atEnd(); l = l.next()) {
            symList.add(l.elem());
        }
        for (BiLink l = module.elements.first(); !l.atEnd(); l = l.next()) {
            if (l.elem() instanceof Function) {
                Function func = (Function) l.elem();

                BiList localSyms = func.localSymtab.symbols();
                for (BiLink m = localSyms.first(); !m.atEnd(); m = m.next()) {
                    symList.add(m.elem());
                }

                // System.out.println("processing : " + func);
                processFuncDef(func);

                BiList instList = func.lirList();
                for (BiLink p = instList.first(); !p.atEnd(); p = p.next()) {
                    LirNode node = (LirNode) p.elem();
                    if (node.opCode == Op.CALL) {
                        if (node.kid(0) instanceof LirSymRef) {
                            String funcName = ((LirSymRef) node.kid(0)).symbol.name; // ClassCastException

                            if (funcName.equals("malloc")) {
                                p = p.next();
                                LirNode nextNode = (LirNode) p.elem();
                                AliasNode aNode = createANode(nextNode);
                                ECR x = getECR(aNode);
                                analysis.assignAlloc(x);
                                p = p.next();
                                node = (LirNode) p.elem();
                            }

                        }
                    }
                    // processStatement(node);
                    procStm(node);
                }
            }
        }
        // System.out.println("end");

        AliasInformation info = new AliasInformation();

        for (BiLink p = symList.first(); !p.atEnd(); p = p.next()) {
            Symbol sym = (Symbol) p.elem();
            Vector set = new Vector();
            ECR ecr = (ECR) typeEnv.get(sym);
            if (ecr != null) {
                //ecr.printPointsToSet();
                set = ecr.getPointsToSet();
                // } else {
                // new ECR(sym, new AlphaType()).printPointsToSet();
                // set = new Vector();
            }
            //print getPointtoSet
//            	{
//                    System.out.print(sym + " 's getPointsToSet : ");
//                	int len = set.size();
//					for(int i = 0; i < len; i++) {
//						System.out.print(set.get(i) + "  ");
//					}
//					System.out.println("  ");
//                }
            info.add(sym, set);
        }

        return info;
    }

    public void procStm(LirNode node) {
        if (node.opCode == Op.SET) {
            // System.out.println(node);

            LirNode lhs = node.kid(0);
            LirNode rhs = node.kid(1);

            AliasNode aNode = createANode(lhs);
            ECR lECR = getECR(aNode);

            Vector exps = new Vector();
            getSubExps(rhs, exps);
            filter(exps);

            for (int i = 0; i < exps.size(); i++) {
                AliasNode nodei = (AliasNode) exps.get(i);
                if (nodei.isAddr()) {
                    analysis.assignAddr(lECR, makeECR(nodei.node));
                } else {
                    analysis.assign(lECR, getECR(nodei));
                }
            }

        } else if (node.opCode == Op.CALL) {
            // System.out.println(node);

            LirNode func = node.kid(0);
            LirNode args = node.kid(1);
            LirNode result = node.kid(2);

            int numArgs = args.nKids();
            int numResult = result.nKids();

            Vector argsECR = new Vector();
            ECR resultECR = null;

            if (func instanceof LirSymRef
                    && !(((LirSymRef) func).symbol.name).endsWith("printf")) {

                ECR f;
                if (func instanceof LirSymRef) {
                    f = makeECR(func);
                } else {
                    f = getECR(createANode(func));
                }

                for (int i = 0; i < numArgs; i++) {
                    ECR tmp = new ECR(null, new AlphaType());
                    LirNode argi = args.kid(i);

                    Vector exps = new Vector();
                    getSubExps(argi, exps);
                    filter(exps);

                    for (int j = 0; j < exps.size(); j++) {
                        AliasNode nodej = (AliasNode) exps.get(j);
                        if (nodej.isAddr()) {
                            analysis.assignAddr(tmp, makeECR(nodej.node));
                        } else {
                            analysis.assign(tmp, getECR(nodej));
                        }
                    }

                    argsECR.add(tmp);
                }

                if (numResult == 1) {
                    AliasNode aNode = createANode(result.kid(0));
                    resultECR = getECR(aNode);
                }

                analysis.functionCall(f, argsECR, resultECR);
            }
        }
    }

    public ECR getECR(AliasNode node) {
        if (node.isMem() && node.child.isAddr()) {
            ECR ecr = makeECR(node.child.node);
            return ecr;
        } else {
            ECR tmpECR1 = new ECR(null, new AlphaType());
            ECR tmpECR2 = getECR(node.child);
            analysis.assignPtr(tmpECR1, tmpECR2);
            return tmpECR1;
        }
    }

    public ECR makeECR(LirNode node) {
        Symbol sym = ((LirSymRef) node).symbol; // <-ClassCastException
        ECR ecr = (ECR) typeEnv.get(sym);
        if (ecr == null) {
            ecr = new ECR(sym, new AlphaType());
            typeEnv.put(sym, ecr);
        }
        return ecr;
    }

    public void getSubExps(LirNode node, Vector exps) {
        if (node.opCode == Op.MEM || isAddr(node)) {
            exps.add(createANode(node));
        } else {
            int n = node.nKids();
            for (int i = 0; i < n; i++) {
                getSubExps(node.kid(i), exps);
            }
        }
    }

    // true if the LirNode is (FRMAE ...) or (STATIC ...)
    public boolean isAddr(LirNode node) {
        return node.opCode == Op.FRAME || node.opCode == Op.STATIC;
    }

    // p = &a (+/-) c; -> p = &a;
    public void filter(Vector exps) {
        for (int i = 0; i < exps.size(); i++) {
            AliasNode nodei = (AliasNode) exps.get(i);
            if (nodei.isAddr()) {
                exps.clear();
                exps.add(nodei);
                return;
            }
        }
    }

    public AliasNode createANode(LirNode node) {
        if (isAddr(node)) {
            return new AliasNode((LirSymRef) node);
        } else if (node.opCode == Op.MEM) {
        	//System.out.println("Analyze.createAnode1 : " + node);
            return new AliasNode(createANode(node.kid(0)));
        } else {
        	//System.out.println("Analyze.createAnode2 : " + node);
        	//System.out.println("Analyze.createAnode2.kid : " + node.kid(0));
            return createANode(node.kid(0));
        }
    }

    public void printMemExp(LirNode node) {
        if (isAddr(node)) {
            System.out.println(" FRAME/STATIC "
                    + ((LirSymRef) node).symbol.name);
        } else if (node.opCode == Op.MEM) {
            System.out.print(" MEM ");
            printMemExp(node.kid(0));
        } else {
            printMemExp(node.kid(0));
        }
    }

    public void processFuncDef(Function func) {
        LirNode prologue = func.origPrologue;
        LirNode epilogue = func.origEpilogue;
        int lenArgs = prologue.nKids();

        Vector args = new Vector();

        for (int i = 1; i < lenArgs; i++) {
            ECR ecr = makeECR(prologue.kid(i).kid(0));
            args.add(ecr);
        }

        ECR retVal = null;
        if (epilogue.nKids() >= 2) {
            retVal = makeECR(epilogue.kid(1).kid(0));
        }

        ECR f = (ECR) typeEnv.get(func.symbol);
        if (f == null) {
            f = new ECR(func.symbol, new AlphaType());
            typeEnv.put(func.symbol, f);
        }

        analysis.functionDefinition(f, args, retVal);
    }

}
