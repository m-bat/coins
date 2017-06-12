/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot; //##74
import coins.ir.hir.HIR; //##74
import coins.lparallel.LoopPara; //##74
import coins.driver.CompileSpecification;
import coins.snapshot.SnapShot;
import java.io.IOException; //##74

/**
 * The driver of the MDF module.
 * This class divides the input HIR into several macro tasks and construct
 * the macro flow graph. And also, it makes the dynamic scheduler.
 **/
public class MdfDriver{
  /** The current HirRoot **/
  final HirRoot hirRoot;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** True if the MDF module generates XML file **/
  private final boolean makeShot;
  /** The XML file generator **/
  private final SnapShot snap;

  /**
   * Constructor:
   * @param hRoot The current HirRoot
   * @param iRoot The current IoRoot
   * @param spc The current compiler specification
   **/
  public MdfDriver(HirRoot hRoot,IoRoot iRoot,CompileSpecification spc){
    hirRoot=hRoot;
    env=new MdfEnvironment(iRoot,spc);

    makeShot=false;
    snap=null;
  }

  /**
   * Constructor:
   * @param hRoot The current HirRoot
   * @param iRoot The current IoRoot
   * @param spc The current compiler specification
   * @param snapshot The XML generator
   **/
  public MdfDriver(HirRoot hRoot,IoRoot iRoot,CompileSpecification spc,
                   SnapShot snapshot){
    hirRoot=hRoot;
    env=new MdfEnvironment(iRoot,spc);

    makeShot=true;
    snap=snapshot;
  }

  /**
   * Doing the macro data flow process.
   **/
  public void invoke() throws PassException{
//    env.println("START MDF",env.OptThr);
    MdfModule module=new MdfModule(env,hirRoot);

    module.generateMfg();
//    if(makeShot)
//      snap.shot(module,"Generated MFG");

//    module.changeStructure();
//    if(module.check()){
//      throw new PassException("MDF","NOT TREE in MdfDriver");
//    }
//    env.println("FINISH MDF",env.OptThr);
  }
//##74 BEGIN
  public void
  hir2OpenMP(HirRoot pHirRoot, SymRoot pSymRoot, IoRoot pIoRoot,
             LoopPara pLoopPara)
  throws IOException, PassException
{
  invoke();
  ((HIR)pHirRoot.programRoot).finishHir();
  pLoopPara.fstophir2c = true;
  pLoopPara.makeCSourceFromHirBase("cgparallel", pHirRoot, pSymRoot, pIoRoot);
} // hir2OpenMP
//##74 END
}
