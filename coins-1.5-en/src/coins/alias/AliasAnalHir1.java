/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasAnalHir1.java
 *
 * Created on July 15, 2003, 1:39 PM
 */

package coins.alias;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import coins.HirRoot;
import coins.IoRoot;
import coins.alias.util.BriggsSet;
import coins.alias.util.Scanner;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.SubpDefinition;
import coins.sym.Sym;
import coins.sym.StructType; //##94
import coins.sym.Type;
import coins.sym.Var;

/**
 * <p>Alias analysis level 1 implementation.</p>
   * <p>Alias analysis level 1 is a flow-insensitive intraprocedural analysis.</p>
 * <p>This class drives the alias analysis and also implements AliasAnal interface.</p>
 * <p>Two modes (Optimistic and Pessimistic), or options,
 * for the analysis are available, each of which uses different
 * assumptions about aliasing.</p>
 * <p>Handling of source languages other than C is not
 * supported in this version.</p>
 *
 * @author  hasegawa
 */
public class AliasAnalHir1
  implements AliasAnal
{
  protected static final int AREA_INCLUDES = 1, AREA_OVERLAPS = 2,
    MUST_ALIAS = AREA_INCLUDES | AREA_OVERLAPS, NOT_ALIAS = 4,
    MAY_ALIAS = MUST_ALIAS | NOT_ALIAS;

  protected static final String PREDEFINED = "aliaspredefined";

//    private SubpDefinition fSubpDef;

  /**
   * Set of predefined functions.
   */
  protected Set fPredefined;

  /**
   * Map that maps HIR nodes to tags.
   */
  Map fHIRToTag; // = new HashMap();

  /**
   * Map that maps malloc invocation nodes to corresponding tags.
   */
  Map fHIRTomallocTag; // = new HashMap();

  private Map fHIRToAliasGroup = new HashMap(); // map of HIR nodes
  // to the set of HIR nodes which may be aliased to the
  // given HIR node.

  private List fParentlessTags; // tags corresponding to named variables

  /**
   * Number of bit-allocated tags. This determines the length
   * of the <code>TagVector</code>.
   */
  int fTagBitCount;

  private int fBitPos; // bit position in TagVector

  /**
   * Array of <code>Tag</code>s that are assigned bits.
   */
  Tag fBitAssignedTags[];

  /**
   * The points-to graph represented as an array of
   * <code>TagVector</code>s.
   */
  TagVector fPointsTo[];

//    private MyExpId fNonAnchoredMyExpIds[];
  private TagVector fNonNullInitials;
  private TagVector fInitialAddressTakens;
  //	private TagVector fAddressTakens; // NO LONGER USED!

  /**
   * <code>TagVector</code> that corresponds to the current
   * frame (automatic variables that are in scope).
   */
  TagVector fCurFrame;

  /**
   * <code>TagVector</code> that corresponds to the static area.
   */
  TagVector fStatic;

  /**
   * <code>TagVector</code> that corresponds to the heap
   * (allocated) area.
   */
  TagVector fHeap;

  /**
   * <code>TagVector</code> that corresponds to the area
   * that is not included in <code>fCurFrame</code> or
   * <code>fStatic</code> or <code>fHeap</code>, and
   * includes frames from subprograms that called the
   * currrent subprogram, global variables that do not
   * appear in the current subprogram, and area allocated
   * by the subprograms that called the current subprogram.
   */
  TagVector fOther;

  /**
   * <code>TagVector</code> that corresponds to global variables.
   * This is a subset of <code>fStatic</code>.
   */
  TagVector fGlobals;

  /**
   * <code>TagVector</code> that corresponds to the area
   * visible from the outside of the current subprogram.
   * This is the complement of <code>fCurFrame</code>.
   */
  TagVector fExternPes;

  /**
   * <code>TagVector</code> that corresponds to the area
   * visible from the outside of the current subprogram.
   * This is the sum of <code>fGlobals</code> and <code>fOther</code>.
   */
  TagVector fExternOpt;

  private boolean fCurFrameAddressTaken;

  /**
   * The <code>HirRoot</code> object shared by every module
   * in the program.
   */
  public final HirRoot hirRoot;

  /**
   * The <code>IoRoot</code> object shared by every module
   * in the program.
   */
  public final IoRoot ioRoot;

  /**
   * Alias analysis option. This determines the set of
   * assumptions on which the alias analysis is based.
   *
   * @see #AliasAnalHir1(boolean, HirRoot)
   */
  final boolean fIsOptimistic;

  /**
   * Factory object used to create other objects.
   */
  final AliasFactory fFactory;

  /**
   * AliasUtil object used to access utility methods.
   */
  protected final AliasUtil fUtil;

  protected int fDbgLevel; //##62
  /**
   * Creates a new instance of <code>AliasAnalHir1</code>
   * that performs alias analysis based on some set of
   * assumptions about aliasing. Two sets of assumptions (
   * Pessimistic and Optimistic) are available, and
   * which one to use is determined from the command line,
   * and defaults to Pessimistic.
   *
   * @param pHirRoot the <code>HirRoot</code> object shared
   * by every module in the program.
   * @see #AliasAnalHir1(boolean, HirRoot)
   */
  public AliasAnalHir1(HirRoot pHirRoot)
  {
    this(pHirRoot.ioRoot.getCompileSpecification().
         getCoinsOptions().getArg("alias") != null
         ? pHirRoot.ioRoot.getCompileSpecification().getCoinsOptions().
         getArg("alias").equals("opt")
         : false, pHirRoot);
    fDbgLevel = pHirRoot.ioRoot.dbgAlias.getLevel(); //##62
  }

  /**
   * Creats a new instance of <code>AliasAnalHir1</code> that
   * performs alias analysis with the specified assumptions
   * (<code>pIsOptimistic</code>) about aliasing.
   * The difference between optimistic and pessimistic
   * assumptions are:
   * <ol>
   * <li>Whether vector (array) subscripts can get out of
   * the declared range for the vector (in which case
   * "storage promotion" is applied).</li>
   * <li>Whether <em>storage promotion</em> will be applied
   * to pointer arithmetic. If an arithmetic operation was
   * performed on a pointer that is pointing to an object
   * that is of a certain storage class, storage promotion
   *  may be applied, resulting in the result of the
   * operation possibly pointing to any objects in the
   * same storage class.</li>
   * <li>Whether inconsistent object access (access by an
   * object whose type differs from that of the stored object)
   * may happen</li>.
   * </ol>
   */
  public AliasAnalHir1(boolean pIsOptimistic, HirRoot pHirRoot)
  {
    hirRoot = pHirRoot;
    ioRoot = hirRoot.ioRoot;
    fFactory = new AliasFactory(hirRoot);
    fUtil = new AliasUtil(hirRoot.symRoot);
    fIsOptimistic = pIsOptimistic;
    fDbgLevel = ioRoot.getCompileSpecification().getTrace().getTraceLevel(CATEGORY_NAME); //##94
    dbg(1, "AliasAnalHir1", "optimistic=" + pIsOptimistic); //##56
    readPredefined(ioRoot);
  }

  private Set readPredefined(IoRoot pIo)
  {
    Set lPredefineds = new HashSet();
    /* //##71
    File lLibDir = pIo.getCompileSpecification().getCoinsOptions().getLibDir();
    File lPredefined = null;
    try {
      lPredefined = new File(lLibDir.getCanonicalPath() + File.separator +
        PREDEFINED);

      FileReader lReader = new FileReader(lPredefined);

      char lBuff[] = new char[72];

      int i = 0;
      while ((lBuff[i++] = (char)lReader.read()) != (char) - 1)
        if (lBuff[i - 1] != '\r' && lBuff[i - 1] != '\n')
          ; //System.out.println("lBuff[" + (i - 1) + "] = " + (int)lBuff[i - 1]);
        else {
          String lEntry = new String(lBuff, 0, i - 1).trim();
          if (lEntry.length() != 0)
            lPredefineds.add(lEntry);
          i = 0;
        }
      String lEntry0 = new String(lBuff, 0, i - 1).trim();
      if (lEntry0.length() != 0)
        lPredefineds.add(lEntry0);
        //	System.out.println("Predefineds = " + lPredefineds);

    }
    catch (Exception e) {
      dbg(2, "AliasAnal",
          "Cannot find list of predefined functions (\"" + lPredefined
          + "\"). \n" + "No predefined functions are assumed."); //##57
    }
    */ //##71
    //##71 BEGIN
    lPredefineds.addAll(
      hirRoot.symRoot.sourceLanguage.functionsWithoutSideEffect);
    dbg(2, "AliasAnal",
        "functionsWithoutSideEffect" + lPredefineds);
    //##71 END
    return fPredefined = lPredefineds;

  }

  /**
   * <p>Performs alias analysis for the given SubpDefinition
   * argument so that may/mustAlias methods for nodes
   * contained in the SubpDefinition are ready.</p>
   * <p>This method may be called only once in the lifetime
   * of this analysis object; for analyses of different
   * SubpDefinitions, the AliasAnalHir1 object has to be
   * newly created.</p>
   *
   * @param pSubpDef the <code>SubpDefinition</code> instance
   * nodes contained in which are to be analyzed for aliasing.
   */
  public void prepareForAliasAnalHir(SubpDefinition pSubpDef)
  {
    dbg(2, "prepareForAliasAnalHir1", pSubpDef.getSubpSym().getName()); //##56
    //##94 BEGIN
    if (fDbgLevel > 3) {
      pSubpDef.printHir("HIR for AliasAnal ");
    }
    //##94 END
    init();
    prepareTags(pSubpDef);
    constructPointsToGraph(pSubpDef);
    buildAliasGroups(pSubpDef);
  }

  private void init()
  {
//        fHIRToTag = new HashMap(50);
//        fHIRTomallocTag = new HashMap(5);
//        fTagBitCount = 0;
//        fBitPos = -1;
//        fCurFrameAddressTaken = false;
  }

  /**
   * Instantiates tags and TagVectors, builds the tree structure
   * between the tags and assigns TagVector bits to these tags.
   *
   * @param pSubpDef <code>SubpDefinition</code> instance
   * to analyze.
   */
  void prepareTags(SubpDefinition pSubpDef)
  {
//        fSubpDef = pSubpDef;
    MyExpIdAssigner lAssigner = fFactory.myExpIdAssigner(pSubpDef);
    MyExpId lMyExpIdTable[] = lAssigner.assign();
    if (ioRoot.getCompileSpecification().getTrace().shouldTrace("Alias", 5))
      for (int i = 0; i < lMyExpIdTable.length; i++)
        ioRoot.printOut.println("lMyExpIdTable[" + i + "] = " + lMyExpIdTable[i]);

    TagTreeBuilder lBuilder = fFactory.tagTreeBuilder(pSubpDef, lMyExpIdTable,
      fIsOptimistic);
    fHIRToTag = lBuilder.process();
    fHIRTomallocTag = lBuilder.fHIRTomallocTag;
    fParentlessTags = lBuilder.fParentlessTags;
    fTagBitCount = lBuilder.fTagBitCount;
    fNonNullInitials = fFactory.tagVector(fTagBitCount);
    fInitialAddressTakens = fFactory.tagVector(fTagBitCount);
    fCurFrame = fFactory.tagVector(fTagBitCount);
    fStatic = fFactory.tagVector(fTagBitCount);
    fHeap = fFactory.tagVector(fTagBitCount);
    fOther = fFactory.tagVector(fTagBitCount);
    fGlobals = fFactory.tagVector(fTagBitCount);
    fExternPes = fFactory.tagVector(fTagBitCount);
    fExternOpt = fFactory.tagVector(fTagBitCount);
    fPointsTo = new TagVector[fTagBitCount];
    for (int i = 0; i < fTagBitCount; i++)
      fPointsTo[i] = fFactory.tagVector(fTagBitCount);
    assignBits();
    for (int i = 0; i < fTagBitCount; i++) {
      if (fNonNullInitials.isSet(i)) {
        fPointsTo[i].vectorOr(fInitialAddressTakens, fPointsTo[i]);
        //##58 dbg(5, "fPointsTo", Arrays.asList(fPointsTo));
        if (fDbgLevel > 3) //##62
          dbg(5, "fPointsTo " + i, Arrays.asList(fPointsTo)); //##58
      }
    }

    if (fDbgLevel > 3) //##62
      dbg(5, "Bit-assigned tags ", Arrays.asList(fBitAssignedTags));
  }

  private void assignBits()
  {
    List lOther = new ArrayList();
    fBitAssignedTags = new Tag[fTagBitCount];
    Tag lTag;
    fBitPos = -1;
    fCurFrameAddressTaken = false;

    if (fDbgLevel > 3) //##62
      dbg(5, "fParentlessTags: ", fParentlessTags);
    for (Iterator lIt = fParentlessTags.iterator(); lIt.hasNext(); ) {
      lTag = (Tag)lIt.next();
      switch (lTag.fStorageClass) {
        case Tag.STO_CUR_FRAME:
        case Tag.STO_STATIC:
          setVectForCurFrameStatic(lTag);
          break;
        case Tag.STO_HEAP:
          setVectForHeap(lTag);
          break;
        case Tag.STO_OTHER:
        case Tag.STO_ROOT:
          lOther.add(lTag);
          break;
        default:
          throw new AliasError("Unexpected.");
      }
    }
    for (Iterator lItExt = lOther.iterator(); lItExt.hasNext(); ) {
      lTag = (Tag)lItExt.next();
      if (lTag.fMyExpId == null)
        setVectForOther(lTag);
      else
        setVectForUndecay(lTag,
          ((Tag)fHIRToTag.get(lTag.fMyExpId.fHIR.getChild1())).fBitPos);
    }
    fOther.vectorOr(fStatic, fExternPes);
    fExternPes.vectorOr(fHeap, fExternPes);
    fOther.vectorOr(fGlobals, fExternOpt);
    fInitialAddressTakens.setBit(fTagBitCount - 1); // The last bit represents an unspecified external region
  }

  /**
   * This method will be called only for unique tags.
   *
   */
  private TagVector setVectForCurFrameStatic(Tag pTag)
  {
    Var lVar = (Var)pTag.fMyExpId.getHir().getSym();
    pTag.fTagVect = fFactory.tagVector(fTagBitCount);
    pTag.fTagVect.setBit(++fBitPos);
    if (lVar != null) {
      if (lVar.getSymKind() == Sym.KIND_PARAM) {
        if (fDbgLevel > 3) //##62
          dbg(5, "Parameter", pTag);
        fNonNullInitials.setBit(fBitPos);
      }
      //            if ( lVar.getStorageClass() == Var.VAR_STATIC )
      if (lVar.getStorageClass() == Var.VAR_STATIC) {
        if (lVar.isGlobal()) {
          fGlobals.setBit(fBitPos);
          fInitialAddressTakens.setBit(fBitPos);
          fNonNullInitials.setBit(fBitPos);
        }
        if (!fIsOptimistic || lVar.getFlag(Sym.FLAG_ADDRESS_TAKEN)) {
          fInitialAddressTakens.setBit(fBitPos);
          fNonNullInitials.setBit(fBitPos);
          //					fAddressTakens.setBit(fBitPos);
        }
      }
      else if (lVar.getStorageClass() == Var.VAR_AUTO &&
               lVar.getFlag(Sym.FLAG_ADDRESS_TAKEN)) {
        //				fAddressTakens.setBit(fBitPos);
        fCurFrameAddressTaken = true;
      }
      if (lVar.getStorageClass() == Var.VAR_AUTO)
        fCurFrame.setBit(fBitPos);
      else
        fStatic.setBit(fBitPos);
    }
    else {
      int lParentBitPos = pTag.fParent.fBitPos;
      if (fNonNullInitials.isSet(lParentBitPos))
        fNonNullInitials.setBit(fBitPos);
      if (fInitialAddressTakens.isSet(lParentBitPos))
        fInitialAddressTakens.setBit(fBitPos);
        //			if (fAddressTakens.isSet(lParentBitPos))
        //				fAddressTakens.setBit(fBitPos);
      if (fCurFrame.isSet(lParentBitPos))
        fCurFrame.setBit(fBitPos);
      if (fStatic.isSet(lParentBitPos))
        fStatic.setBit(fBitPos);
      if (fGlobals.isSet(lParentBitPos))
        fGlobals.setBit(fBitPos);
    }
    pTag.fBitPos = fBitPos;
    fBitAssignedTags[fBitPos] = pTag;
    List lPromotedChildren = new ArrayList();
    for (Iterator lIt = pTag.fChildren.iterator(); lIt.hasNext(); ) {
      Tag lChild = (Tag)lIt.next();
      if (lChild.fIsUnique) {
        if (pTag.fKind == Tag.KIND_UNION) // Handling of union
          processUnionChildren(pTag);
        else
          pTag.fTagVect.vectorOr(setVectForCurFrameStatic((Tag)lChild),
            pTag.fTagVect);
      }
      else
        lPromotedChildren.add(lChild);
    }
    for (Iterator lIt0 = lPromotedChildren.iterator(); lIt0.hasNext(); ) {
      Tag lTag = (Tag)lIt0.next();
      processNonUniqueChildren(lTag, pTag.fTagVect);
    }

    return pTag.fTagVect;
  }

  private TagVector setVectForHeap(Tag pTag)
  {
    pTag.fTagVect = fFactory.tagVector(fTagBitCount);
    pTag.fTagVect.setBit(++fBitPos);
    pTag.fBitPos = fBitPos;
    fBitAssignedTags[fBitPos] = pTag;
    //		fAddressTakens.setBit(fBitPos);
    fHeap.setBit(fBitPos);
    return pTag.fTagVect;
  }

  /**
   * This method is for area referred to by undecayed pointers.
   * The assumption for undecayed pointers are:
   * <ol>
   * <li>Initially, the undecayed pointer's (array) elements
   * may be pointing to somewhere else.</li>
   * <li>Otherwise, the undecayed pointer behaves the same way
   * as the array variable with automatic storage. For example,
   *  If its address is not taken within the subprogram
   * under consideration, it will not be pointed to by anything,
   * provided the optimistic option for alias analysis has
   * been specified. The storage class for the area is static
   * or heap, so for pesimistic option, it may share areas
   * with other static/heap lvalues, e.g. static array element
   * with non-constant subscript.</li>
   * </ol>
   * The vector <code>fExtern</code> will be OR'ed with
   * <code>fCurFrame</code> and <code>fStatic</code> later.
   */
  private TagVector setVectForUndecay(Tag pTag, int pPointerBitPos)
  {
    pTag.fTagVect = fFactory.tagVector(fTagBitCount);
    pTag.fTagVect.setBit(++fBitPos);
    fNonNullInitials.setBit(fBitPos);
    pTag.fBitPos = fBitPos;
    fBitAssignedTags[fBitPos] = pTag;
    List lPromotedChildren = new ArrayList();
    if (true)
      throw new AliasError("How should I handle the storage class for undecay?");
    //	fExtern.setBit(fBitPos);

    for (Iterator lIt = pTag.fChildren.iterator(); lIt.hasNext(); ) {
      Tag lChild = (Tag)lIt.next();
      if (lChild.fIsUnique) {
        if (pTag.fKind == Tag.KIND_UNION) // Handling of union
          processUnionChildren(pTag);
        else
          pTag.fTagVect.vectorOr(setVectForUndecay((Tag)lChild, pPointerBitPos),
            pTag.fTagVect);
      }
      else
        lPromotedChildren.add(lChild);
    }
    for (Iterator lIt0 = lPromotedChildren.iterator(); lIt0.hasNext(); ) {
      Tag lTag = (Tag)lIt0.next();
      processNonUniqueChildren(lTag, pTag.fTagVect);
    }

    fPointsTo[pPointerBitPos].setBit(fBitPos);

    return pTag.fTagVect;
  }

  private TagVector setVectForOther(Tag pTag)
  {
    pTag.fTagVect = fFactory.tagVector(fTagBitCount);
    pTag.fTagVect.setBit(++fBitPos);
    fNonNullInitials.setBit(fBitPos);
    pTag.fBitPos = fBitPos;
    fBitAssignedTags[fBitPos] = pTag;
    fOther.setBit(fBitPos);
    return pTag.fTagVect;
  }

  /**
   * Copies the parent tags' TagVector (pParentVect) to all
   * its descendants. pTag is one of such descendants.
   *
   * @pTag non-unique child.
   */
  private void processNonUniqueChildren(Tag pTag, TagVector pParentVect)
  {
    pTag.fTagVect = fFactory.tagVector(fTagBitCount);
    pParentVect.vectorCopy(pTag.fTagVect);
    //		pTag.fTagVect.fIsDefinite = false;
    for (Iterator lIt = pTag.fChildren.iterator(); lIt.hasNext(); ) {
      Tag lTag = (Tag)lIt.next();
      processNonUniqueChildren(lTag, pParentVect);
    }
  }

  private void processUnionChildren(Tag pTag)
  {
    for (Iterator lIt = pTag.fChildren.iterator(); lIt.hasNext(); ) {
      Tag lTag = (Tag)lIt.next();
      lTag.fTagVect = fFactory.tagVector(fTagBitCount);
      pTag.fTagVect.vectorCopy(lTag.fTagVect);
      processUnionChildren(lTag);
    }
  }

  /**
   * Constructs points-to graph.
   * The points-to graph is implemented as a bit matrix (array
   * of bit vectors).
   * On the way, "non-anchored tags" (tags that involve
   * pointer dereferencing) are assigned TagVectors.
   * (Anchored tags should already have been assigned TagVectors.)
   *
   * @return such a bit matrix as array of TagVectors
   */
  private TagVector[] constructPointsToGraph(SubpDefinition pSubpDef)
  {
    ConstructPointsToGraph lConstructPointsToGraph = new ConstructPointsToGraph(this,
      hirRoot);
    fPointsTo = lConstructPointsToGraph.makePointsToGraph(pSubpDef);
    return fPointsTo;
  }

  private Map buildAliasGroups(SubpDefinition pSubpDef)
  {
    AliasGroup lAliasGroups[] = new AliasGroup[fTagBitCount];
    AliasGroup lAliasGroup;

    for (int i = 0; i < lAliasGroups.length; i++)
      lAliasGroups[i] = fFactory.aliasGroup();
    Exp lExp, lExp0;
    Type lType, lType0;
    Tag lTag, lTag0;
    Map.Entry lEntry;
    Map lTagToAliasGroups = new HashMap();
    HIR lHIR;

    for (Iterator lIt = fHIRToTag.entrySet().iterator(); lIt.hasNext(); ) {
      lEntry = (Map.Entry)lIt.next();
      lExp = (Exp)lEntry.getKey();
      lTag = (Tag)lEntry.getValue();
      //        System.out.println("tag vect " + lTag.fTagVect);
      BriggsSet lBriggsSet = lTag.fTagVect.toBriggsSet();
      Scanner lScanner;
      int lBitPos;
      for (lScanner = lBriggsSet.scanner(); lScanner.hasNext(); ) {
        lBitPos = lScanner.next();
        lAliasGroups[lBitPos].add(lExp);
//                else
//                    lScanner.delete();
      }
      lTagToAliasGroups.put(lTag, lBriggsSet);
    }
    for (Iterator lIt = fHIRToTag.entrySet().iterator(); lIt.hasNext(); ) {
      lAliasGroup = fFactory.aliasGroup();
      lEntry = (Map.Entry)lIt.next();
      lExp = (Exp)lEntry.getKey();
      if (fDbgLevel > 3) //##62
        dbg(5, "Building AliasGroup for " + lExp + " ...", "");
      lTag = (Tag)lEntry.getValue();
      BriggsSet lBriggsSet = (BriggsSet)lTagToAliasGroups.get(lTag);
      int lBitPos;
      for (Scanner lScanner = lBriggsSet.scanner(); lScanner.hasNext(); ) {
        lBitPos = lScanner.next();
        for (Iterator lAGIt = lAliasGroups[lBitPos].iterator(); lAGIt.hasNext(); ) {
          lExp0 = (Exp)lAGIt.next();
          lType = lTag.getFlag(Tag.HAS_UNION_ANCESTOR) ?
            lTag.fUnionAncestor.fType : lExp.getType();
          lTag0 = (Tag)fHIRToTag.get(lExp0);
          lType0 = lTag0.getFlag(Tag.HAS_UNION_ANCESTOR) ?
            lTag0.fUnionAncestor.fType : lExp0.getType();

          if (!fIsOptimistic || fUtil.mayAlias(lType, lType0))
            lAliasGroup.add(lExp0);
        }
      }
      fHIRToAliasGroup.put(lExp, lAliasGroup);
      //		dbg(3, "AliasGroup for " + lExp + ": ", lAliasGroup.sort());
    }

    if (ioRoot.getCompileSpecification().getTrace().shouldTrace(CATEGORY_NAME,
      5)) {
      for (HirIterator lIt = hirRoot.hir.hirIterator(pSubpDef.getHirBody());
           lIt.hasNext(); ) {
        lHIR = lIt.next();
        if (fHIRToTag.containsKey(lHIR)) {
          lAliasGroup = (AliasGroup)fHIRToAliasGroup.get(lHIR);
          ioRoot.printOut.println("AliasGroup for " + lHIR + ": " +
            lAliasGroup.sort());
        }
      }
    }

    return fHIRToAliasGroup;
  }

  /**
   * Returns one of the codes (MUST_ALIAS, MAY_ALIAS, NOT_ALIAS).
   */
  protected int areAliased(Exp pExp, Exp pExp0)
  {
    if (!fHIRToTag.containsKey(pExp))
      throw new IllegalArgumentException(pExp + " not lvalue.");
    if (!fHIRToTag.containsKey(pExp0))
      throw new IllegalArgumentException(pExp0 + " not lvalue.");
    if (getAliasGroupFor(pExp).contains(pExp0))
      return areAliased0(pExp, pExp0);
    return NOT_ALIAS;
  }

  private int areAliased0(Exp pExp, Exp pExp0)
  {
    TagVector lTagVect1 = fFactory.tagVector(fTagBitCount);
    //       dbg(5, "areAliased", "Alias relation of " + pExp + " and " + pExp0);
    Tag lTag = (Tag)fHIRToTag.get(pExp);
    Tag lTag0 = (Tag)fHIRToTag.get(pExp0);

    TagVector lTagVect = lTag.fTagVect;
    TagVector lTagVect0 = lTag0.fTagVect;

    lTagVect.vectorAnd(lTagVect0, lTagVect1);
    if (!lTagVect1.isZero()) {
      if (lTagVect.vectorEqual(lTagVect1) && lTag0.fIsUnique ||
          lTagVect0.vectorEqual(lTagVect1) && lTag.fIsUnique)
        return MUST_ALIAS;
//            else if (fIsOptimistic && !fUtil.mayAlias(pExp.getType(), pExp0.getType()))
//                return NOT_ALIAS;
      else
        return MAY_ALIAS;
    }
    throw new AliasError("Unexpected.");
  }

  /**
   * Returns the set of lvalue nodes the specified argument may
   * be aliased to. Each <code>AliasGroup</code> object for
   * different argument (<code>pExp</code>) is distinct from
   * one another, so it can be safely modified without
   * affecting others.
   *
   * @param pExp the lvalue node to check for aliasing.
   * @return the set of lvalue nodes the specified argument
   * may be aliased to.
   * @exception IllegalArgumentException if either of the
   * arguments is not lvalue.
   */
  public AliasGroup getAliasGroupFor(Exp pExp)
  {
    if (isLvalue(pExp))
      return (AliasGroup)fHIRToAliasGroup.get(pExp);
    throw new IllegalArgumentException(pExp + " not lvalue.");
  }

  /**
   * Prints out alias pairs in <code>IoRoot.printOut</code>
   * object. For debugging.
   *
   * @param pSubpDef the <code>SubpDefinition</code> object
   * aliasing relation between nodes contained in which are
   * to be printed.
   */
  public void printAliasPairs(SubpDefinition pSubpDef)
  {
    HIR lHIR;
    List lHIRList = new ArrayList();
    HIR lHIRs[];

    for (HirIterator lIt = hirRoot.hir.hirIterator(pSubpDef.getHirBody());
         lIt.hasNext(); ) {
      lHIR = (HIR)lIt.next();
      lHIRList.add(lHIR);
    }
    lHIRs = (HIR[])lHIRList.toArray(new HIR[0]);

    ioRoot.printOut.println("---Alias Pairs--- " +
      pSubpDef.getSubpSym().getName()); //##27
    ioRoot.printOut.println(
      "Legend (D: Definitely aliased, P: Possibly aliased)");
    ioRoot.printOut.println("");
    for (int i = 0; i < lHIRs.length; i++) {
      Tag lTag = (Tag)fHIRToTag.get(lHIRs[i]);
      if (lTag != null)
        for (int j = i + 1; j < lHIRs.length; j++) {
          Tag lTag0 = (Tag)fHIRToTag.get(lHIRs[j]);
          if (lTag0 != null) {
            if (!lHIRs[i].isSameAs(lHIRs[j])) //##27
              switch (areAliased((Exp)lHIRs[i], (Exp)lHIRs[j])) {
                case MUST_ALIAS:
                  ioRoot.printOut.println("(D: " + lHIRs[i] + ", " + lHIRs[j] +
                    ")");
                  break;
                case MAY_ALIAS:
                  ioRoot.printOut.println("(P: " + lHIRs[i] + ", " + lHIRs[j] +
                    ")");
                  break;
              }
          }
        }
    }
    ioRoot.printOut.println();
  }

  /**
   * Prints out alias pairs in <code>IoRoot.printOut</code>
   * object in somewhat more detailed format than in printAliasPairs.
   *
   * @param pSubpDef the <code>SubpDefinition</code> object
   * aliasing relation between nodes contained in which are
   *  to be printed.
   */
  public void printAliasPairsDetail(SubpDefinition pSubpDef)
  {
    HIR lHIR;
    List lHIRList = new ArrayList();
    HIR lHIRs[];

    for (HirIterator lIt = hirRoot.hir.hirIterator(pSubpDef.getHirBody());
         lIt.hasNext(); ) {
      lHIR = lIt.next();
      lHIRList.add(lHIR);
    }
    lHIRs = (HIR[])lHIRList.toArray(new HIR[0]);

    ioRoot.printOut.println("---Alias Pairs---");
    for (int i = 0; i < lHIRs.length; i++) {
      Tag lTag = (Tag)fHIRToTag.get(lHIRs[i]);
      if (lTag != null)
        for (int j = i + 1; j < lHIRs.length; j++) {
          Tag lTag0 = (Tag)fHIRToTag.get(lHIRs[j]);
          if (lTag0 != null) {
            try {
              if (!lHIRs[i].isSameAs(lHIRs[j])) //##27
                switch (areAliased((Exp)lHIRs[i], (Exp)lHIRs[j])) {
                  case MUST_ALIAS:
                    ioRoot.printOut.println(fUtil.toString(lHIRs[i]) + " and " +
                      fUtil.toString(lHIRs[j]) + " are definitely aliased.");
                    break;
                  case MAY_ALIAS:
                    ioRoot.printOut.println(fUtil.toString(lHIRs[i]) + " and " +
                      fUtil.toString(lHIRs[j]) + " are possibly aliased.");
                    break;
                }
            }
            catch (IllegalArgumentException e) {
              throw e;
            }
          }
        }
    }
    ioRoot.printOut.println();
  }

  /**
   * Returns true if the two arguments may refer to the
   * overlapping area in memory. A CONTENTS node with
   * uninitialized pointer value operand (undefined
   * behavior in C) does not represent an object that
   * inhabits the memory space, so it is not aliased even to itself.
   *
   * @exception IllegalArgumentException if either of the
   * arguments is not lvalue.
   */
  public boolean mayAlias(Exp pExp, Exp pExp0)
  {
    return (areAliased(pExp, pExp0) & MUST_ALIAS) != 0;
  }

  /**
   * Returns true if the two arguments definitely refer to
   * the overlapping area in memory.
   *
   * @exception IllegalArgumentException if either of the
   * arguments is not lvalue.
   */
  public boolean mustAlias(Exp pExp, Exp pExp0)
  {
    return (areAliased(pExp, pExp0) & NOT_ALIAS) == 0;
  }

  /**
   * Returns true if the specified argument is lvalue.
   * As is the case for the query methods (mayAlias etc.)
   * that is specified in the AliasAnal interface,
   * this method also has to be called after the
   * prepareForAliasAnalHir method for the corresponding
   * SubpDefinition object has been called.
   *
   * @return true if the specified argument is lvalue.
   */
  public boolean isLvalue(Exp pExp)
  {
    //##94 return fHIRToTag.containsKey(pExp);
    //##94 BEGIN
    if (fHIRToTag.containsKey(pExp))
      return true;
    if (pExp == null)
      return false;
    // If pExp represents aggregate type then return true.
    Type lType = pExp.getType();
    if (pExp.getOperator() == HIR.OP_QUAL)
      lType = ((HIR)pExp.getChild1()).getType();
    int lTypeKind = lType.getTypeKind();
    if ((lTypeKind == Type.KIND_VECTOR)||
        (lTypeKind == Type.KIND_STRUCT)||
        (lTypeKind == Type.KIND_UNION))
       return true;
     return false;
     //##94 END
  }

  /**
   * Prints out the <code>String</code> representation of
   * the <code>pBody</code> object headed by <code>pHeader</code>
   * if the debug level for this alias analysis category is
   * greater than or equal to <code>pLevel</code>.
   *
   * @param pLevel the debug level required to actually print
   *  the debug information.
   * @param pHeader header for the information
   * @param pBody body of the information
   * @see coins.Debug#printObject(int, String, Object)
   */
  public void dbg(int pLevel, String pHeader, Object pBody)
  {
    String lStr = pBody == null ? "null" : pBody.toString();
    //##94 if (ioRoot.getCompileSpecification().getTrace().getTraceLevel(CATEGORY_NAME) >=
    if (fDbgLevel >= //##94
    pLevel) {
      ioRoot.printOut.print(" " + pHeader + " " + lStr);
      ioRoot.printOut.println();
    }
  }
}
