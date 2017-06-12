/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

//##60 import coins.aflow.util.CoinsList;
import java.util.ArrayList; //##60
import java.util.Iterator; //##63

/**
   * List of <code>SetRefRepr</code>s that support <code>SetRefReprIterator</code>.
 */
//##60 public abstract class SetRefReprList extends CoinsList
public class SetRefReprList
  extends ArrayList //##60
{
  private BBlock fBBlock;
  private SubpFlow fSubpFlow;
  //60 FlowResults fResults;
  //##60 public Flow flow;
  protected ArrayList fReverseList = null; //##63

  private SetRefReprList()
  {
    super();
  }

  public SetRefReprList(BBlock pBBlock)
  {
    this();
    fBBlock = pBBlock;
    //##60 fResults = fBBlock.results();
    if (pBBlock != null) //##63
      fSubpFlow = pBBlock.getSubpFlow(); //##60
  }

  /**
   * Returns a <code>BBlock</code> object this <code>SetRefReprList</code> is associated with. If this <code>SetRefReprList</code> is not associated with a <code>BBlock</code> returns <code>null</code>.
   */
  public BBlock getBBlock()
  {
    return fBBlock;
  }

//##63 BEGIN
public Iterator
reverseIterator()
{
  if (fReverseList == null) {
    int lSize = this.size();
    fReverseList = new ArrayList(lSize);
    for (int lIndex = lSize-1; lIndex >= 0; lIndex--) {
      Object lItem = get(lIndex);
      fReverseList.add(lItem);
    }
  }
  return fReverseList.iterator();
}
//##63 END

public String
toString()
{
  StringBuffer lBuffer = new StringBuffer("SetRefReprList ");
    for (Iterator lIterator = this.iterator();
         lIterator.hasNext(); ) {
      SetRefRepr lItem = (SetRefRepr)lIterator.next();
      lBuffer.append(" " + lItem.toString());
    }
  return lBuffer.toString();
} // toString
}
