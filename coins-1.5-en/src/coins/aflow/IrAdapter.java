/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * IrAdatper.java
 *
 * Created on July 22, 2002, 2:42 PM
 */
package coins.aflow;

import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;


/**
 *
 * @author  hasegawa
 */
class IrAdapter implements IR {
    /** Creates new IrAdatper */
    public IrAdapter() {
    }

    public void replaceSource2(IR pOperand) {
    }

    public IR buildNode(int pOperator) {
        throw new UnsupportedOperationException();
    }

    public IR getChild1() {
        throw new UnsupportedOperationException();
    }

    public void replaceSource1(IR pOperand) {
    }

    public void replaceOperator(int pOperator) {
    }

    public void setChild2(IR p2) {
    }

    public String getIndentSpace(int pIndent) {
        throw new UnsupportedOperationException();
    }

    public void replaceResultVar(IR pOperand) {
    }

    public void setChild1(IR p1) {
    }

    public Sym getSym() {
        throw new UnsupportedOperationException();
    }

    public IR getSourceNode(int pNumber) {
        throw new UnsupportedOperationException();
    }

    public void setParent(IR pParent) {
    }

    public IR getSourceNode2() {
        throw new UnsupportedOperationException();
    }

    public IR getSourceNode1() {
        throw new UnsupportedOperationException();
    }

    public IR buildNode(int pOperator, IR pSource1) {
        throw new UnsupportedOperationException();
    }

    public IR buildNode(int pOperator, IR pSource1, IR pSource2) {
        throw new UnsupportedOperationException();
    }

    public int getChildCount() {
        throw new UnsupportedOperationException();
    }

    public int getOperator() {
        throw new UnsupportedOperationException();
    }

    public void print(int pIndent) {
    }

    public void replaceSource(int pNumber, IR pOperand) {
    }

    public String getIrName() {
        throw new UnsupportedOperationException();
    }

    public Sym getResultVar() {
        throw new UnsupportedOperationException();
    }

    public IR getInfNode() {
        throw new UnsupportedOperationException();
    }

    public IR buildSymNode(Sym pSym) {
        throw new UnsupportedOperationException();
    }

    public void setIndex(int pIndex) {
    }

    public void attachInf(IR pNode) {
    }

    public IR getParent() {
        throw new UnsupportedOperationException();
    }

    public void print(int pIndent, boolean pDetail) {
    }

    public void replaceThisNode(IR pNewNode) {
    }

    public void setChild(int pNumber, IR pIr) {
    }

    public int getIndex() {
        throw new UnsupportedOperationException();
    }

    public IR getChild(int pNumber) {
        throw new UnsupportedOperationException();
    }

    public IR getChild2() {
        throw new UnsupportedOperationException();
    }

    public void setInfList(String pInfIdInterned, IrList pInfList) {
        throw new UnsupportedOperationException();
    }
     //##16

    public void setInfString(String pInfIdInterned, String pInfString) //##16
     {
        throw new UnsupportedOperationException();
    }

    public void addInf(String pInfIdInterned, Object pInfObject) {
        throw new UnsupportedOperationException();
    }

    public IrList getInfList() {
        throw new UnsupportedOperationException();
    }

    public IrList getInfList(String pString) {
        throw new UnsupportedOperationException();
    }

    public String getInfString() {
        throw new UnsupportedOperationException();
    }

    public String getInfString(String pString) {
        throw new UnsupportedOperationException();
    }

    public Object getInf(String pInfIdInterned) {
        throw new UnsupportedOperationException();
    }

    public void removeInf(String pInfIdInterned) {
        throw new UnsupportedOperationException();
    }

    public FlowAnalSym getFlowAnalSym() {
        throw new UnsupportedOperationException();
    }

    public Sym getResultOperand() {
        throw new UnsupportedOperationException();
    }

    public void replaceResultOperand(IR pOperand) {
        throw new UnsupportedOperationException();
    }

    public void setWork(Object pObject) {
        throw new UnsupportedOperationException();
    }

    public Object getWork() {
        throw new UnsupportedOperationException();
    }

    public String toStringShort() //##60
    {
      return toString();
    }

}
