/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindEGen.java
 *
 * Created on 2002/06/07, 14:38
 */
package coins.aflow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 *
 * @author  hasegawa
 */
public abstract class FindEGen extends FlowAdapter {
public FindEGen(FlowResults pResults)
{
  super(pResults);
}

/**
 * Finds EGen vectors for all the <code>BBlock</code>s in <code>pSubpFlow</code>.
 */
public void find(SubpFlow pSubpFlow)
{
  for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();)
       find((BBlock) lIt.next());
}

/**
 * Finds the EGen vector for <code>pBBlock</code>.
 */
public void find(BBlock pBBlock)
{
  SetRefReprList lBBlockSetRefReprList = (SetRefReprList)fResults.get(
    "BBlockSetRefReprs",
    pBBlock);
  SetRefRepr lSetRefRepr;
  Set lEGenSet = new HashSet();
  Set lEKillSet = new HashSet(); //##56
  ExpVector lEGen;
  flow.dbg(3, "\nFindEGen.find " + pBBlock); //##56
  flow.dbg(6, "BBlockSetRefReprList " + lBBlockSetRefReprList); //##57 //##56
  if (lBBlockSetRefReprList != null) { //##53
    for (Iterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.iterator();
         lBBlockSetRefReprIterator.hasNext(); ) {
      lSetRefRepr = (SetRefRepr)lBBlockSetRefReprIterator.next();
      //##56 BEGIN
      // lSetRefRepr may include items corresponding to all subexpressions
      // of a source program expression such as qualifying expression.
      // If its subexpression is already killed, then the subexpression
      // should be removed from EGen.
      addEGenExpId(lEGenSet, lEKillSet, lSetRefRepr);
      //##56 END
      //##56 addEGen(lEGenSet, lSetRefRepr);

      // System.out.println("EGenSet after removal " + lEGenSet);
    }
  }

  lEGen = ExpVectorImpl.forSet(lEGenSet, pBBlock.getSubpFlow());

  //                System.out.println("EGen " + lEGen);
  register(pBBlock, lEGen);
}

    protected abstract void addEGen(Set pEGenSet, SetRefRepr pSetRefRepr);
    protected abstract void addEGenExpId(Set pEGenSet,
                                         Set pEKillSet, SetRefRepr pSetRefRepr);

    protected abstract void register(BBlock pBBlock, ExpVector pEGen);
}
